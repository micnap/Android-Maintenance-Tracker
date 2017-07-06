package com.mickeywilliamson.mickey.maintenance2;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by mickey on 7/5/17.
 */

public class ViewHolder {

    TextView task_name = null;
    TextView dayTV = null;
    TextView monthTV = null;
    TextView yearTV = null;
    ImageView task_wrench = null;
    ImageView divider = null;
    ImageView arrow = null;

    ViewHolder(View base) {
        this.task_name = (TextView) base.findViewById(R.id.task_name);
        this.dayTV = (TextView) base.findViewById(R.id.day);
        this.monthTV = (TextView) base.findViewById(R.id.month);
        this.yearTV = (TextView) base.findViewById(R.id.year);
        this.task_wrench = (ImageView) base.findViewById(R.id.task_wrench);
        this.divider = (ImageView) base.findViewById(R.id.divider);
        this.arrow = (ImageView) base.findViewById(R.id.task_arrow);
    }
}
