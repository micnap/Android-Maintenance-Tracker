package com.mickeywilliamson.mickey.maintenance2;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {

    private List<MaintenanceTask> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_activity_main_actionbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));

        DatabaseHandler db = new DatabaseHandler(this);
        tasks = db.getAllMaintenanceTasks();

        // If there are no maintenance items, populate the list with items for testing.
        if (tasks.size() == 0) {
            populateList();
            tasks = db.getAllMaintenanceTasks();
        }

        // Bind the list items to the activity's sole Listview.
        setListAdapter(new TaskAdapter(this, R.layout.task_row, tasks));
    }

    @Override
    public void onListItemClick(ListView l, View view, int position, long id) {
        super.onListItemClick(l, view, position, id);

        // Load the item in a new activity for viewing/updating.
        Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);

        // Pass along the id of the task to be updated.
        intent.putExtra("task_id", tasks.get(position).getId());

        // Open the item.
        startActivity(intent);
    }

    /**
     * onClick handler for the "Add new task" button.
     * Opens the same activity as the one used to updated existing tasks.
     */
    public void addTask(View view) {
        Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
        startActivity(intent);
    }

    /**
     * Reloads the activity with fresh data so that if the back button is clicked
     * after an item is updated, the new data shows in the list.
     */
    public void onResume() {
        super.onResume();
        DatabaseHandler db = new DatabaseHandler(this);
        tasks = db.getAllMaintenanceTasks();
        TaskAdapter<MaintenanceTask> taskAdapter = new TaskAdapter<>(this, R.layout.task_row, tasks);
        setListAdapter(taskAdapter);
    }

    /**
     * Populate the list with dummy content for testing.
     */
    private void populateList() {
        DatabaseHandler db = new DatabaseHandler(this);
        db.addMaintenanceTask(new MaintenanceTask("Clean chimney", "2017-07-01", "Every year", "Capitol Area Chimney Sweep\nphone:717-555-1212"));
        db.addMaintenanceTask(new MaintenanceTask("Change Reverse Osmosis Filters", "2017-12-06", "Every 6 months", null));
        db.addMaintenanceTask(new MaintenanceTask("Change whole house water filter", "2017-04-10", "Every 2 weeks", "Walmart's filters are cheapest"));
        db.addMaintenanceTask(new MaintenanceTask("Clean gutters", "2017-11-01", "Every year", null));
        db.addMaintenanceTask(new MaintenanceTask("Clean AC filters", "2017-08-01", "Every month", null));
        db.addMaintenanceTask(new MaintenanceTask("Pump septic tank", "2020-06-01", "Every 3 years", "Baymont On-call - 717-555-1212"));
        db.addMaintenanceTask(new MaintenanceTask("Clean oven", "2018-02-01", "Every 6 months", null));
        db.addMaintenanceTask(new MaintenanceTask("Flush hot water heater", "2017-08-01", "Every year", null));
        db.addMaintenanceTask(new MaintenanceTask("Service lawn mower", "2018-03-01", "Every year", "Mower guy - 717-555-1212"));
        db.addMaintenanceTask(new MaintenanceTask("Service chainsaw", "2018-03-01", "Every year", "Mower guy - 717-555-1212"));
        db.addMaintenanceTask(new MaintenanceTask("Service weed whacker", "2018-03-01", "Every year", "Mower guy - 717-555-1212"));
        db.addMaintenanceTask(new MaintenanceTask("Prune bushes", "2018-4-01", "Every year", null));
        db.addMaintenanceTask(new MaintenanceTask("Winterize AC", "2017-10-01", "Every year", null));

    }
}
