package com.btbsolutions.timekeeper.utility;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ToDoDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ToDo toDo);

    @Query("DELETE FROM todo_table")
    void deleteAll();

    @Delete
    void delete(ToDo toDo);

    @Update
    public void updateTodoItems(ToDo toDo);

    @Query("SELECT * FROM todo_table ORDER BY priority ASC, todotask ASC")
    LiveData<List<ToDo>> getAllToDoItems();

    @Query("SELECT * FROM todo_table ORDER BY date ASC, priority ASC")
    LiveData<List<ToDo>> getAllItemsByEndDate();

    @Query("SELECT * FROM todo_table WHERE todotask = :name")
    LiveData<List<ToDo>> getItemsByName(String name);

    @Query("SELECT * FROM todo_table WHERE id = :id")
    LiveData<ToDo> getItemById(long id);

    @Query("SELECT * FROM todo_table WHERE date Like :date and useremail like :useremail ORDER BY duration, priority ASC")
    LiveData<List<ToDo>> getAllItemsForEndDate(String date, String useremail);

    @Query("SELECT * FROM todo_table WHERE date < :date and :completion = 0")
    LiveData<List<ToDo>> getIncompleteItemsFromPreviousDate(String date, int completion);

    @Query("SELECT Distinct(todotask) FROM todo_table")
    LiveData<List<String>> getAllDistinctTasks();

    @Query("UPDATE todo_table SET complete = 0 where id = :id")
    void updatecompletion(long id);

    @Query("SELECT * FROM todo_table WHERE backup = 0")
    LiveData<List<ToDo>> getAllRowsToDoTableNotBackup();

    @Query("UPDATE todo_table SET backup = 1 where id = :id")
    void updateToDoTableBackup(long id);

    @Query("SELECT count(*) FROM todo_table WHERE date Like :date AND useremail like :useremail")
    LiveData<Integer> getCountOfAllTaskForDate(String date, String useremail);

    @Query("SELECT count(*) FROM todo_table WHERE date Like :date AND useremail like :useremail AND complete = 1")
    LiveData<Integer> getCountOfAllCompletedTaskForDate(String date, String useremail);

    @Query("SELECT * FROM todo_table WHERE complete = 0 and date = :date AND useremail like :useremail")
    LiveData<List<ToDo>> getIncompleteTodoTask(String date, String useremail);
}
