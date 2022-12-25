package com.example.pillremind.adapter;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.example.pillremind.R;

import java.util.List;

public class TimeDoseAdapter extends BaseAdapter {
    private final Context context;
    private final List<?> list;
    private LayoutInflater layoutInflater;

    public TimeDoseAdapter(Context context, List<?> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.time_dose, parent, false);
            vh = new ViewHolder();
            vh.time = convertView.findViewById(R.id.edtTime);
            vh.dose = convertView.findViewById(R.id.edtDose);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        int timePosition = vh.time.getId();
        vh.time.setId(timePosition);
        vh.time.setOnClickListener(e -> {
            @SuppressLint("SetTextI18n") TimePickerDialog timePickerDialog = new TimePickerDialog(context, (view, hourOfDay, minute) -> vh.time.setText(hourOfDay + ":" + minute), 0, 0, true);
            timePickerDialog.show();
        });
        int dosePosition = vh.dose.getId();
        vh.dose.setId(dosePosition);
        return convertView;
    }

    public static class ViewHolder {
        EditText time;
        EditText dose;
    }
}
