package com.mickeywilliamson.mickey.maintenance2;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.app.DatePickerDialog;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    @Override
    public @NonNull Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), (AddTaskActivity)getActivity(), year, month, day);

    }
}
