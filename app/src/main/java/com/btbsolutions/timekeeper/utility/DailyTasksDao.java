package com.btbsolutions.timekeeper.utility;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DailyTasksDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(DailyTasks dailyTasks);

    @Query("DELETE FROM daily_task_table")
    void deleteAll();

    //@Query("SELECT * FROM daily_task_table")
    //LiveData<List<DailyTasks>> getAllTasks();

    @Query("SELECT * FROM daily_task_table WHERE date = :selecteddate AND useremail LIKE :useremail ORDER BY strtTime")
    LiveData<List<DailyTasks>> getAllTasks(String selecteddate, String useremail);

    @Query("SELECT * FROM daily_task_table WHERE backup = 0")
    LiveData<List<DailyTasks>> getAllRowsDailyTaskTableNotBackup();

    @Query("UPDATE daily_task_table SET backup = 1 where id = :id")
    void updateDailyTaskTableBackup(long id);

    @Query("SELECT SUM(duration) FROM daily_task_table WHERE date = :selecteddate AND useremail LIKE :useremail AND metricQ LIKE 'Q1 - Urgent And Important' ORDER BY strtTime")
    LiveData<Double> getAllQ1Tasks(String selecteddate, String useremail);

    @Query("SELECT SUM(duration) FROM daily_task_table WHERE date = :selecteddate AND useremail LIKE :useremail AND metricQ LIKE 'Q2 - Important But Not Urgent' ORDER BY strtTime")
    LiveData<Double> getAllQ2Tasks(String selecteddate, String useremail);

    @Query("SELECT SUM(duration) FROM daily_task_table WHERE date = :selecteddate AND useremail LIKE :useremail AND metricQ LIKE 'Q3 - Urgent But Not Important' ORDER BY strtTime")
    LiveData<Double> getAllQ3Tasks(String selecteddate, String useremail);

    @Query("SELECT SUM(duration) FROM daily_task_table WHERE date = :selecteddate AND useremail LIKE :useremail AND metricQ LIKE 'Q4 - Neither Urgent Nor Important' ORDER BY strtTime")
    LiveData<Double> getAllQ4Tasks(String selecteddate, String useremail);

    @Query("SELECT SUM(duration) FROM daily_task_table WHERE date = :selecteddate AND useremail LIKE :useremail ORDER BY strtTime")
    LiveData<Double> getAllQTasksForToday(String selecteddate, String useremail);



    @Query("SELECT SUM(duration) FROM daily_task_table WHERE (date between :lowerdate AND :selecteddate) AND useremail LIKE :useremail AND metricQ LIKE 'Q1 - Urgent And Important' ORDER BY strtTime")
    LiveData<Double> getAllQ1TasksForWeek(String selecteddate, String lowerdate, String useremail);

    @Query("SELECT SUM(duration) FROM daily_task_table WHERE (date between :lowerdate AND :selecteddate) AND useremail LIKE :useremail AND metricQ LIKE 'Q2 - Important But Not Urgent' ORDER BY strtTime")
    LiveData<Double> getAllQ2TasksForWeek(String selecteddate, String lowerdate, String useremail);

    @Query("SELECT SUM(duration) FROM daily_task_table WHERE (date between :lowerdate AND :selecteddate) AND useremail LIKE :useremail AND metricQ LIKE 'Q3 - Urgent But Not Important' ORDER BY strtTime")
    LiveData<Double> getAllQ3TasksForWeek(String selecteddate, String lowerdate, String useremail);

    @Query("SELECT SUM(duration) FROM daily_task_table WHERE (date between :lowerdate AND :selecteddate) AND useremail LIKE :useremail AND metricQ LIKE 'Q4 - Neither Urgent Nor Important' ORDER BY strtTime")
    LiveData<Double> getAllQ4TasksForWeek(String selecteddate, String lowerdate, String useremail);

    @Query("SELECT SUM(duration) FROM daily_task_table WHERE (date between date(:lowerdate) AND date(:selecteddate)) AND useremail LIKE :useremail ORDER BY strtTime")
    LiveData<Double> getAllQTasksForWeek(String selecteddate, String lowerdate, String useremail);
}
