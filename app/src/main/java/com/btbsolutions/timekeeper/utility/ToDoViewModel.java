package com.btbsolutions.timekeeper.utility;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class ToDoViewModel extends AndroidViewModel {

    private ToDoRepository mRepository;

    private LiveData<List<ToDo>> mAllToDoItems;

    private LiveData<List<ToDo>> mAllItemsByEndDate;

    private LiveData<List<String>> mGetAllDailyTasks;

    public ToDoViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ToDoRepository(application);
        mAllToDoItems = mRepository.getAllToDoItems();
        mAllItemsByEndDate = mRepository.getAllItemsByEndDate();
    }

    public LiveData<List<DailyTasks>> getAllTasks(String selecteddate, String useremail) { return mRepository.getAllTasks(selecteddate, useremail); }

    public LiveData<List<DailyTasks>> getAllRowsDailyTaskTableNotBackup() { return mRepository.getAllRowsDailyTaskTableNotBackup(); }

    public LiveData<List<ToDo>> getIncompleteTodoTask(String date, String useremail) {
        return mRepository.getIncompleteTodoTask(date, useremail);
    }

    /*public LiveData<List<ToDo>> getAllToDoItems(){
        return mAllToDoItems;
    }*/

    /*public LiveData<List<ToDo>> getAllItemsByEndDate(){
        return mAllItemsByEndDate;
    }*/

    /*public LiveData<List<ToDo>> getAllItemsByName(String name) { return  mRepository.getAllItemsByName(name); }*/

    public LiveData<ToDo> getItemById(long id) { return  mRepository.getItemById(id); }

    public LiveData<List<ToDo>> getAllItemsForEndDate(String end_date, String useremail){
        return mRepository.getItemForEndDate(end_date, useremail);
    }

    public LiveData<List<ToDo>> getAllRowsNotBackup() { return mRepository.getAllRowsNotBackup(); }

    /*public LiveData<List<ToDo>> getIncompleteItemsFromPreviousDate(String end_date, boolean completion){
        return mRepository.getIncompleteItemsFromPreviousDate(end_date, completion);
    }*/

    public LiveData<Double> getAllQ1Tasks(String date, String useremail){ return mRepository.getAllQ1Tasks(date, useremail); }

    public LiveData<Double> getAllQ2Tasks(String date, String useremail){ return mRepository.getAllQ2Tasks(date, useremail); }

    public LiveData<Double> getAllQ3Tasks(String date, String useremail){ return mRepository.getAllQ3Tasks(date, useremail); }

    public LiveData<Double> getAllQ4Tasks(String date, String useremail){ return mRepository.getAllQ4Tasks(date, useremail); }

    public LiveData<Double> getAllQTasksForToday(String date, String useremail){ return mRepository.getAllQTasksForToday(date, useremail); }


    public LiveData<Double> getAllQ1TasksForWeek(String date, String lowerdate, String useremail){
        return mRepository.getAllQ1TasksForWeek(date, lowerdate, useremail);
    }

    public LiveData<Double> getAllQ2TasksForWeek(String date, String lowerdate, String useremail){
        return mRepository.getAllQ2TasksForWeek(date, lowerdate, useremail);
    }

    public LiveData<Double> getAllQ3TasksForWeek(String date, String lowerdate, String useremail){
        return mRepository.getAllQ3TasksForWeek(date, lowerdate, useremail);
    }

    public LiveData<Double> getAllQ4TasksForWeek(String date, String lowerdate, String useremail){
        return mRepository.getAllQ4TasksForWeek(date, lowerdate, useremail);
    }

    public LiveData<Double> getAllQTasksForWeek(String date, String lowerdate, String useremail){
        return mRepository.getAllQTasksForWeek(date, lowerdate, useremail);
    }

    public LiveData<Integer> getCountOfAllTaskForDate(String date, String useremail){
        return mRepository.getCountOfAllTaskForDate(date, useremail);
    }

    public LiveData<Integer> getCountOfAllCompletedTaskForDate(String date, String useremail){
        return mRepository.getCountOfAllCompletedTaskForDate(date, useremail);
    }

    public void insert(ToDo toDo){
        mRepository.insert(toDo);
    }

    public void insert(DailyTasks dailyTasks) { mRepository.insert(dailyTasks);}

    public void deleteAll(){
        mRepository.deleteAll();
    }

    public void delete(ToDo toDo){
        mRepository.delete(toDo);
    }

    public void update(ToDo toDo) { mRepository.update(toDo);}

    public void updatecompletion(long id) { mRepository.updatecompletion(id);}

    public void updateBackup(long id) { mRepository.updateBackup(id); }

    public void updateDailyTaskTableBackup(long id) { mRepository.updateDailyTaskTableBackup(id); }
}
