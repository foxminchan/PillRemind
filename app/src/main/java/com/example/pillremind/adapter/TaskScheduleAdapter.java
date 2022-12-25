package com.example.pillremind.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pillremind.R;
import com.example.pillremind.model.domain.Task;

import java.util.List;

public class TaskScheduleAdapter extends BaseAdapter {

    private final List<Task> taskList;
    private final LayoutInflater layoutInflater;
    private final Context context;

    public TaskScheduleAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_task, parent, false);
            vh = new ViewHolder();
            vh.time = convertView.findViewById(R.id.tvTime);
            vh.pillHeader = convertView.findViewById(R.id.taskHeader);
            vh.decription = convertView.findViewById(R.id.taskDescription);
            vh.warning = convertView.findViewById(R.id.taskWarning);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }
        Task task = taskList.get(position);
        vh.time.setText(task.getTime());
        vh.pillHeader.setText(task.getPillHeader());
        vh.decription.setText(task.getPillDescription());
        vh.warning.setText(task.getPillWarning());
        return convertView;
    }

    private static class ViewHolder{
        TextView time;
        TextView pillHeader;
        TextView decription;
        TextView warning;
    }
}
