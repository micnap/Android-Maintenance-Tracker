package com.mickeywilliamson.mickey.maintenance2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "maintenance_tracker";
    private static final String TABLE_TASKS = "tasks";
    private static final String KEY_ID = "id";
    private static final String KEY_TASK = "task_name";
    private static final String KEY_NEXTDATE = "next_date";
    private static final String KEY_FREQUENCY = "frequency";
    private static final String KEY_ADDINFO = "additional_info";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MAINTENANCE_TRACKER_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TASK + " TEXT,"
                + KEY_NEXTDATE + " TEXT,"
                + KEY_FREQUENCY + " TEXT," + KEY_ADDINFO + " TEXT" + ");";

        db.execSQL(CREATE_MAINTENANCE_TRACKER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    /**
     * Add a new maintenance task.
     */
    public long addMaintenanceTask(MaintenanceTask task){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_TASK, task.getTask());
        values.put(KEY_NEXTDATE, task.getNextDate());
        values.put(KEY_FREQUENCY, task.getFrequency());
        values.put(KEY_ADDINFO, task.getAdditionalInfo());

        long result = db.insert(TABLE_TASKS, null, values);
        db.close();

        return result;
    }

    /**
     * Retrieve a maintenance task.
     */
    public MaintenanceTask getMaintenanceTask(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TASKS,
                new String[] {KEY_ID, KEY_TASK, KEY_NEXTDATE, KEY_FREQUENCY, KEY_ADDINFO},
                KEY_ID + "=?",
                new String[] {String.valueOf(id)},
                null,
                null,
                null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        MaintenanceTask task = new MaintenanceTask(
                Integer.parseInt(cursor.getString(0)),  // Id
                cursor.getString(1),                    // Task name
                cursor.getString(2),                    // Next date
                cursor.getString(3),                    // Frequency
                cursor.getString(4)                     // Additional info
        );

        cursor.close();

        return task;
    }

    /**
     * Get all maintenance tasks ordered by date ascending.
     */
    public List<MaintenanceTask> getAllMaintenanceTasks() {

        List<MaintenanceTask> taskList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_TASKS + " ORDER BY " + KEY_NEXTDATE + " ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MaintenanceTask task = new MaintenanceTask();
                task.setId(Integer.parseInt(cursor.getString(0)));
                task.setTask(cursor.getString(1));
                task.setNextDate(cursor.getString(2));
                task.setFrequency(cursor.getString(3));
                task.setAdditionalInfo(cursor.getString(4));
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return taskList;
    }

    /**
     * Update maintenance task.
     */
    public int updateMaintenanceTask(MaintenanceTask task) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASK, task.getTask());
        values.put(KEY_NEXTDATE, task.getNextDate());
        values.put(KEY_FREQUENCY, task.getFrequency());
        values.put(KEY_ADDINFO, task.getAdditionalInfo());

        return db.update(TABLE_TASKS, values, KEY_ID + " = ?", new String[] {String.valueOf(task.getId())});
    }

    /**
     * Delete maintenance task.
     */
    public void deleteMaintenanceTask(MaintenanceTask task) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_ID  + " = ?", new String[] {String.valueOf(task.getId())});

        db.close();
    }
}

