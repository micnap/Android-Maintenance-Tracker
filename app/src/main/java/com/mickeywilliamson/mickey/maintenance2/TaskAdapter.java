package com.mickeywilliamson.mickey.maintenance2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class TaskAdapter<T> extends ArrayAdapter<MaintenanceTask> {

    private final ArrayList<MaintenanceTask> tasks;

    public TaskAdapter(Context context, int resource, List<MaintenanceTask> objects) {
        super(context, resource, objects);
        this.tasks = (ArrayList<MaintenanceTask>) objects;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public MaintenanceTask getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View v = convertView;

        // First check to see if the view is null. If so, we have to render it.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.task_row, parent, false);
        }

        ViewHolder holder = (ViewHolder) v.getTag();

        if (holder == null) {
            holder = new ViewHolder(v);
            v.setTag(holder);
        }

        // Set the alternating background color for the task list.
        if (position % 2 == 0) {
            v.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.even_bg));
        } else {
            v.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.odd_bg));
        }

        // Get dynamic data.
        if (tasks.get(position).getTask() != null) {
            holder.task_name.setText(String.valueOf(tasks.get(position).getTask()));
        }
        if (tasks.get(position).getNextDate() != null) {
            holder.dayTV.setText(datePieces(tasks.get(position).getNextDate(), "day"));
            holder.monthTV.setText(datePieces(tasks.get(position).getNextDate(), "month"));
            holder.yearTV.setText(datePieces(tasks.get(position).getNextDate(), "year"));
        }

        // Get static fields.
        holder.task_wrench.setImageResource(R.mipmap.ic_launcher);
        holder.divider.setImageResource(R.drawable.divider);
        holder.arrow.setImageResource(R.drawable.arrow3);

        return v;
    }

    private static String datePieces(String date, String piece) {

        if (date == null) {
            return null;
        }

        String format1 = "yyyy-MM-dd";
        String format2;
        String formattedDate = null;

        switch (piece) {
            case "day":
                format2 = "d";
                break;
            case "month":
                format2 = "MMM";
                break;
            default:
                format2 = "yyyy";
        }

        try {
            DateFormat originalFormat = new SimpleDateFormat(format1);
            DateFormat targetFormat = new SimpleDateFormat(format2);
            Date parsedDate = originalFormat.parse(date);
            formattedDate = targetFormat.format(parsedDate);  // 20120821
        } catch(Exception e) {
            //e.printStackTrace();
        }

        return formattedDate;
    }
}
