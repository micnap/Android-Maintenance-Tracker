package com.mickeywilliamson.mickey.maintenance2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class AddTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    final static int DB_TO_DISPLAY = 0;
    final static int DISPLAY_TO_DB = 1;
    final static String DATE_DB_FORMAT = "yyyy-MM-dd";
    final static String DATE_DISPLAY_FORMAT = "MM/dd/yyyy";

    // @TODO - make list user editable.
    String[] frequency = {"Choose", "Every week", "Every 2 weeks", "Every month", "Every 3 months", "Every 6 months", "Every year", "Every 3 years"};

    EditText mTaskName;
    EditText mNextDate;
    Spinner mFrequency;
    EditText mAdditionalInfo;
    Button mAddUpdate;
    LinearLayout mLinearLayout;
    MaintenanceTask task;
    Button mDelete;
    int mTaskId;

    private DatePickerDialog mDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Intent mIntent = getIntent();
        mTaskId = mIntent.getIntExtra("task_id", -1);

        mTaskName = (EditText) findViewById(R.id.editText_task_name);
        mNextDate = (EditText) findViewById(R.id.nextDate);
        mFrequency = (Spinner) findViewById(R.id.spinner_frequency);
        mAdditionalInfo = (EditText) findViewById(R.id.additional_info);
        mAddUpdate = (Button) findViewById(R.id.submit);
        mLinearLayout = (LinearLayout) findViewById(R.id.buttons);
        mDelete = (Button) findViewById(R.id.delete);

        // Only enabled and shown on task update, not when a new task is created.
        mDelete.setEnabled(false);
        mDelete.setVisibility(View.GONE);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, frequency);
        mFrequency.setAdapter(adapter);

        mNextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        // If a valid task ID exists, an item is being updated and data gets pulled into the fields.
        if (mTaskId != -1) {
            DatabaseHandler db = new DatabaseHandler(this);
            task = db.getMaintenanceTask(mTaskId);

            mTaskName.setText(task.getTask());
            mNextDate.setText(formatDate(task.getNextDate(), DB_TO_DISPLAY));
            int index = Arrays.asList(frequency).indexOf(task.getFrequency());
            if (index != -1) {
                mFrequency.setSelection(index);
            }

            //mFrequency.set;
            mAdditionalInfo.setText(task.getAdditionalInfo());

            // Enable and show delete button since we have a valid task to delete.
            mDelete.setEnabled(true);
            mDelete.setVisibility(View.VISIBLE);

            // Override the button's default text for adding a new task.
            mAddUpdate.setText("Update");
        }
    }

    public void deleteTask(View view) {
        DialogFragment newFragment = new DeleteDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", mTaskId);
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "deleteTask");
    }



    public void addTask(View view) {
        if (mTaskName != null) {
            DatabaseHandler db = new DatabaseHandler(this);
            long result;
            boolean error = false;

            String taskName = mTaskName.getText().toString().trim();
            if (taskName.equals("")) {
                mTaskName.setError(getResources().getString(R.string.valid_name));
                error = true;
            }
            String frequencyValue = mFrequency.getSelectedItem().toString();
            if (Arrays.asList(frequency).indexOf(frequencyValue) < 1) {
                ((TextView)mFrequency.getSelectedView()).setError(getResources().getString(R.string.valid_frequency));
                error = true;
            }
            String nextDate = mNextDate.getText().toString();

            // @TODO Needs better validation.  Currently just brute force checking.
            if (!isValidDate(nextDate)) {
                mNextDate.setError(getResources().getString(R.string.valid_date));
                error = true;
            }
            if (error) {
                return;
            }

            // If a valid id exists, update the item.  Otherwise, add a new item.
            if (mTaskId == -1) {
                result = db.addMaintenanceTask(new MaintenanceTask(taskName, formatDate(mNextDate.getText().toString(), DISPLAY_TO_DB), mFrequency.getSelectedItem().toString(), mAdditionalInfo.getText().toString()));
            } else {
                result = db.updateMaintenanceTask(new MaintenanceTask(mTaskId, mTaskName.getText().toString(), formatDate(mNextDate.getText().toString(), DISPLAY_TO_DB), mFrequency.getSelectedItem().toString(), mAdditionalInfo.getText().toString()));
            }


            // Give feedback on success/failure of action.
            if (result > -1) {
                Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.failure, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Makes sure date is in the future but <= 10 years into the future.
     */
    public boolean isValidDate(String date) {
        if (date.equals("")) {
            return false;
        }

        Date enteredDate;
        String parsedEnteredDate = null;
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_DB_FORMAT);

        // Convert the entered date to a form that can be compared with other days (MM/dd/yyyy to yyyy-MM-dd).
        try {
            enteredDate = new SimpleDateFormat(DATE_DISPLAY_FORMAT).parse(date);

            parsedEnteredDate = formatter.format(enteredDate);
        } catch (Exception e){

        }

        // Get the current date in yyyy-MM-dd format.
        Date nowDate = new Date();
        String parsedNowDate = formatter.format(nowDate);

        // Get the date 10 years from today in yyyy-MM-dd format.
        // Putting this limitation on the date so someone can't enter some crazy date like 99/99/9999.
        Date nowPlusTen = addYear(nowDate, 10);
        String parsedNowPlusTen = formatter.format(nowPlusTen);

        // Make sure it's a valid day and month.
        int month = Integer.parseInt(date.substring(0,2));
        int day = Integer.parseInt(date.substring(3,5));

        if ((parsedEnteredDate.compareTo(parsedNowDate) >= 0) && (parsedEnteredDate.compareTo(parsedNowPlusTen) <= 0) && (month <= 12) && (day <= 31)) {
            return true;
        } else {
            return false;
        }

    }

    public static Date addYear(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, i);
        Date time = cal.getTime();
        return time;
    }

    public static String formatDate(String date, int direction) {

        if (date == null) {
            return null;
        }

        String format1 = null;
        String format2 = null;
        String formattedDate = null;

        if (direction == DB_TO_DISPLAY) {
            format1 = DATE_DB_FORMAT;
            format2 = DATE_DISPLAY_FORMAT;
        } else if (direction == DISPLAY_TO_DB) {
            format1 = DATE_DISPLAY_FORMAT;
            format2 = DATE_DB_FORMAT;
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


    @Override
    /**
     * Passes date back from fragment.
     */
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = sdf.format(cal.getTime());
        ((EditText) findViewById(R.id.nextDate)).setText(formattedDate);
    }
}

