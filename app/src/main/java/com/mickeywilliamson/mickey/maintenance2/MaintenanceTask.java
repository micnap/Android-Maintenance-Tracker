package com.mickeywilliamson.mickey.maintenance2;

public class MaintenanceTask {

    private int mId;
    private String mTask;
    private String mNextDate;
    private String mFrequency;
    private String mAdditionalInfo;

    public MaintenanceTask() {}

    public MaintenanceTask(String task, String nextDate, String frequency, String additionalInfo) {
        this.mTask = task;
        this.mNextDate = nextDate;
        this.mFrequency = frequency;
        this.mAdditionalInfo = additionalInfo;
    }

    public MaintenanceTask(int id, String task, String nextDate, String frequency, String additionalInfo) {
        this.mId = id;
        this.mTask = task;
        this.mNextDate = nextDate;
        this.mFrequency = frequency;
        this.mAdditionalInfo = additionalInfo;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getTask() {
        return mTask;
    }

    public void setTask(String task) {
        this.mTask = task;
    }

    public String getNextDate() { return mNextDate; }

    public void setNextDate(String nextDate) { this.mNextDate = nextDate; }

    public String getFrequency() { return mFrequency; }

    public void setFrequency(String frequency) { this.mFrequency = frequency; }

    public String getAdditionalInfo() { return mAdditionalInfo; }

    public void setAdditionalInfo(String additionalInfo) { this.mAdditionalInfo = additionalInfo; }

}
