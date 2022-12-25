package com.example.pillremind.view;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pillremind.R;
import com.example.pillremind.adapter.PillAdapter;
import com.example.pillremind.model.domain.PillItem;
import com.example.pillremind.presenter.PillPresenter;
import com.example.pillremind.presenter.base.IPill;
import com.example.pillremind.service.AlarmReceiver;
import com.example.pillremind.service.AlarmService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class PillFragment extends Fragment implements IPill.View, View.OnClickListener {

    private PillPresenter pillPresenter;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private SearchView searchView;
    private ImageView refresh;
    private PillAdapter pillAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pill, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        registerListeners();
        initPresenter();
        creatListPill();
    }


    public void creatListPill() {
        pillPresenter.getPillList();
    }

    public void initViews(View view) {
        fab = view.findViewById(R.id.fab);
        emptyView = view.findViewById(R.id.textView);
        recyclerView = view.findViewById(R.id.listView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        searchView = view.findViewById(R.id.searchView);
        refresh = view.findViewById(R.id.refresh);
    }

    public void registerListeners() {
        fab.setOnClickListener(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                pillAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pillAdapter.getFilter().filter(newText);
                return false;
            }
        });
        refresh.setOnClickListener(this);
    }

    public void initPresenter() {
        pillPresenter = new PillPresenter(this);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onClick(@NonNull View v) {
        if (v.getId() == fab.getId()) {
            Intent intent = new Intent(getActivity(), PillActivity.class);
            startActivity(intent);
        } else if (v.getId() == refresh.getId()) {
            pillPresenter.getPillList();
        }
    }

    @Override
    public void showPillList(@NonNull List<PillItem> pillItems) {
        if (pillItems.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            pillAdapter = new PillAdapter();
            pillAdapter.setDatas(requireContext(), pillItems);
            recyclerView.setAdapter(pillAdapter);
            RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(itemDecoration);
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    if (viewHolder instanceof PillAdapter.ViewHolder) {
                        String name = pillItems.get(viewHolder.getAdapterPosition()).getPillName();
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setTitle("Xoá thuốc");
                        builder.setMessage("Bạn có chắc chắn muốn xoá thuốc này không?");
                        builder.setPositiveButton("Có", (dialog, which) -> {
                            pillPresenter.deletePill(pillItems.get(viewHolder.getAdapterPosition()));
                            pillItems.remove(viewHolder.getAdapterPosition());
                            pillAdapter.notifyDataSetChanged();
                            Toast.makeText(requireContext(), "Đã xoá thuốc " + name, Toast.LENGTH_SHORT).show();
                            stopAlarm();
                            if (pillItems.size() == 0) {
                                recyclerView.setVisibility(View.GONE);
                                emptyView.setVisibility(View.VISIBLE);
                            }
                        });
                        builder.setNegativeButton("Không", (dialog, which) -> pillAdapter.notifyDataSetChanged());
                        builder.create().show();
                    }
                }

                @Override
                public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                    if (viewHolder != null) {
                        View foregroundView = ((PillAdapter.ViewHolder) viewHolder).pillLayout;
                        getDefaultUIUtil().onSelected(foregroundView);
                    }
                }

                @Override
                public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    View foregroundView = ((PillAdapter.ViewHolder) viewHolder).pillLayout;
                    getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
                }

                @Override
                public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    View foregroundView = ((PillAdapter.ViewHolder) viewHolder).pillLayout;
                    getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
                }

                @Override
                public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                    View foregroundView = ((PillAdapter.ViewHolder) viewHolder).pillLayout;
                    getDefaultUIUtil().clearView(foregroundView);
                }
            });
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }
    }

    private void stopAlarm() {
        Intent intent = new Intent(requireContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        Intent serviceIntent = new Intent(requireContext(), AlarmService.class);
        requireContext().stopService(serviceIntent);
    }
}