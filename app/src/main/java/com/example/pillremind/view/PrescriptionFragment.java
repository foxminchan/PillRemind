package com.example.pillremind.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pillremind.R;
import com.example.pillremind.adapter.TaskScheduleAdapter;
import com.example.pillremind.model.domain.Task;
import com.example.pillremind.presenter.PresciptionPresenter;
import com.example.pillremind.presenter.base.IPresciption;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class PrescriptionFragment extends Fragment implements IPresciption.View {

    private CalendarView calendarView;
    private PresciptionPresenter presciptionPresenter;
    private TextView tvNoTask;
    private ListView lvTask;
    private List<Task> taskList;

    public PrescriptionFragment() {

    }

    @NonNull
    public static PrescriptionFragment newInstance(String param1, String param2) {
        PrescriptionFragment fragment = new PrescriptionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_prescription, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initPresenter();
        setListeners();
        loadTodayTask();
    }

    public void loadTodayTask(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        String date = day + "/" + (month + 1) + "/" + year;
        presciptionPresenter.getPillList(date);
    }


    public void initViews(View view) {
        calendarView = view.findViewById(R.id.cvTask);
        tvNoTask = view.findViewById(R.id.tvNoData);
        lvTask = view.findViewById(R.id.listView);
    }

    public void setListeners() {
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String date = dayOfMonth + "/" + month + "/" + year;
            Toast.makeText(getContext(), date, Toast.LENGTH_SHORT).show();
            presciptionPresenter.getPillList(date);
        });
    }

    public void initPresenter() {
        presciptionPresenter = new PresciptionPresenter(this);
    }

    @Override
    public void showPillList(List<Task> tasks) {
        Log.d("ErrorDD", "showPillList: " + tasks.size());
        taskList = new ArrayList<>();
        taskList.addAll(tasks);
        if (taskList.size() == 0) {
            tvNoTask.setVisibility(View.VISIBLE);
            lvTask.setVisibility(View.GONE);
        } else {
            tvNoTask.setVisibility(View.GONE);
            lvTask.setVisibility(View.VISIBLE);
            lvTask.setAdapter(new TaskScheduleAdapter(getContext(), taskList));
        }
    }
}