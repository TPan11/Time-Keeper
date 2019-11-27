package com.btbsolutions.timekeeper.utility;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "daily_task_table")
public class DailyTasks {

    //variable for id
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    //variable for tasks
    @NonNull
    @ColumnInfo(name = "tasks")
    private String tasks;

    //variable for date
    @NonNull
    @ColumnInfo(name = "date")
    private String date;

    //variable for strtTime
    @ColumnInfo(name = "strtTime")
    private String strtTime;

    //variable for endTime
    @ColumnInfo(name = "endTime")
    private String endTime;

    //variable for metricQ
    @ColumnInfo(name = "metricQ")
    private String metricQ;

    //variable for duration
    @ColumnInfo(name = "duration")
    private double duration;

    //variable for complete
    @NonNull
    @ColumnInfo(name = "complete")
    private int complete;

    //variable for backup
    @NonNull
    @ColumnInfo(name = "backup")
    private  boolean backup;

    //variable for useremail
    @NonNull
    @ColumnInfo(name = "useremail")
    private String useremail;

    public DailyTasks(String tasks, @NonNull String date, String strtTime, String endTime,
                      String metricQ, double duration, @NonNull int complete, boolean backup,
                      @NonNull String useremail){
        this.tasks = tasks;
        this.date = date;
        this.strtTime = strtTime;
        this.endTime = endTime;
        this.metricQ = metricQ;
        this.duration = duration;
        this.complete = complete;
        this.backup = backup;
        this.useremail = useremail;
    }

    //getter setter for id
    public long getId() { return this.id; }

    public void setId(long id) { this.id = id; }

    //getter setter for task
    @NonNull
    public String getTasks() { return this.tasks; }

    public void setTasks(@NonNull String tasks) { this.tasks = tasks; }

    //getter setter for strtTime
    public String getStrtTime() { return this.strtTime; }

    public void setStrtTime(String strtTime) { this.strtTime = strtTime; }

    //getter setter for endTime
    public String getEndTime() { return this.endTime; }

    public void setEndTime(String endTime) { this.endTime = endTime; }

    //getter setter for metricQ
    public String getMetricQ() { return this.metricQ; }

    public void setMetricQ(String metricQ) { this.metricQ = metricQ; }

    //getter setter for duration
    public double getDuration() { return this.duration; }

    public void setDuration(double duration) { this.duration = duration; }

    //getter setter for complete
    public int getComplete() { return this.complete; }

    public void setComplete(int complete) { this.complete = complete; }

    //getter setter for backup
    public boolean getBackup() { return this.backup; }

    public void setBackup(boolean backup) { this.backup = backup; }

    //getter setter for date
    @NonNull
    public String getDate() { return date; }

    public void setDate(@NonNull String date) { this.date = date; }

    //getter setter for useremail
    @NonNull
    public String getUseremail() { return this.useremail; }

    public void setUseremail(@NonNull String useremail) { this.useremail = useremail; }
}
