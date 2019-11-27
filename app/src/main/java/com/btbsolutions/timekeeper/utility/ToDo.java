package com.btbsolutions.timekeeper.utility;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "todo_table")
public class ToDo{

    //variable for id
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    //variable for todotask
    @NonNull
    @ColumnInfo(name = "todotask")
    private String todotask;

    //variable for date
    @NonNull
    @ColumnInfo(name = "date")
    private String date;

    //variable for priority
    @NonNull
    @ColumnInfo(name = "priority")
    private int priority;

    //variable for detailed_notes
    @ColumnInfo(name = "detailed_notes")
    private String detailed_notes;

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
    private boolean backup;

    //variable for useremail
    @NonNull
    @ColumnInfo(name = "useremail")
    private String useremail;

    public ToDo(@NonNull String todotask, @NonNull String date, @NonNull int priority,
                String detailed_notes, double duration, @NonNull int complete, boolean backup,
                @NonNull String useremail){

        this.todotask = todotask;
        this.date = date;
        this.priority = priority;
        this.detailed_notes = detailed_notes;
        this.duration = duration;
        this.complete = complete;
        this.backup = backup;
        this.useremail = useremail;
    }

    //getter setter for id
    public long getId() { return this.id; }

    public void setId(long id) { this.id = id; }

    //getter setter for todoTask
    @NonNull
    public String getTodotask() { return this.todotask; }

    public void setTodotask(@NonNull String todotask) { this.todotask = todotask; }

    //getter setter for date
    public String getDate() { return this.date; }

    public void setDate(@NonNull String date) { this.date = date; }

    //getter setter for priority
    public int getPriority() { return this.priority; }

    public void setPriority(int priority) { this.priority = priority; }

    //getter setter for detailed_notes
    public String getDetailed_notes() { return this.detailed_notes; }

    public void setDetailed_notes(String detailed_notes) {this.detailed_notes = detailed_notes; }

    //getter setter for duration
    public double getDuration() { return this.duration; }

    public void setDuration(double duration) { this.duration = duration; }

    //getter setter for complete
    public int getComplete() { return this.complete; }

    public void setComplete(int complete) { this.complete = complete; }

    //getter setter for backup
    public boolean getBackup() { return this.backup; }

    public void setBackup(boolean backup) { this.backup = backup; }

    //getter setter for useremail
    @NonNull
    public String getUseremail() { return this.useremail; }

    public void setUseremail(@NonNull String useremail) { this.useremail = useremail; }

}

