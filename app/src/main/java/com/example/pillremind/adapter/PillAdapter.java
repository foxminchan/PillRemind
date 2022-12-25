package com.example.pillremind.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pillremind.R;
import com.example.pillremind.model.domain.PillItem;
import com.example.pillremind.view.PillEditActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PillAdapter extends RecyclerView.Adapter<PillAdapter.ViewHolder> implements Filterable {

    private List<PillItem> pillItemList;
    private List<PillItem> pillItemListFull;
    private Context context;

    @SuppressLint("NotifyDataSetChanged")
    public void setDatas(Context context, List<PillItem> pillItemList) {
        this.context = context;
        this.pillItemList = pillItemList;
        pillItemListFull = pillItemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pill_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PillItem pillItem = pillItemList.get(position);
        if (pillItem != null) {
            holder.pillName.setText(pillItem.getPillName());
            holder.pillFrequency.setText(pillItem.getPillFrequency());
            Picasso.get()
                    .load(pillItem.getPillImage())
                    .placeholder(R.drawable.loading_circle)
                    .error(R.drawable.default_pill)
                    .centerCrop()
                    .fit()
                    .into(holder.pillImage);
            holder.pillLayout.setOnClickListener(v -> {
                Intent intent = new Intent(context, PillEditActivity.class);
                intent.putExtra("Name", pillItem.getPillName());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        if (pillItemList != null) {
            return pillItemList.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchKey = constraint.toString();
                if (searchKey.isEmpty()) {
                    pillItemList = pillItemListFull;
                } else {
                    List<PillItem> filteredList = new ArrayList<>();
                    for (PillItem pillItem : pillItemListFull) {
                        if (pillItem.getPillName().toLowerCase().contains(searchKey.toLowerCase())) {
                            filteredList.add(pillItem);
                        }
                    }
                    pillItemList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = pillItemList;
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                pillItemList = (List<PillItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView pillName;
        public TextView pillFrequency;
        public ImageView pillImage;
        public RelativeLayout pillLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pillName = itemView.findViewById(R.id.tvNamePill);
            pillFrequency = itemView.findViewById(R.id.tvFrequencyPill);
            pillImage = itemView.findViewById(R.id.imgPill);
            pillLayout = itemView.findViewById(R.id.pillLayout);
        }
    }
}
