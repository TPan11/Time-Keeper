package com.btbsolutions.timekeeper.utility;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class ToDoRepository {

    private ToDoDao mToDoDao;
    private DailyTasksDao mDailyTaskDao;

    private LiveData<List<ToDo>> mAllToDoItems;
    private LiveData<List<ToDo>> mAllItemsByEndDate;
    private LiveData<List<ToDo>> mAllRowsNotBackup;
    private LiveData<List<DailyTasks>> mAllDAilyTaskRowsNotBackup;

    public ToDoRepository(Application application){
        ToDoRoomDatabase db = ToDoRoomDatabase.getDatabase(application);
        mToDoDao = db.toDoDao();
        mDailyTaskDao = db.dailyTasksDao();

        mAllToDoItems = mToDoDao.getAllToDoItems();
        mAllItemsByEndDate = mToDoDao.getAllItemsByEndDate();
        mAllRowsNotBackup = mToDoDao.getAllRowsToDoTableNotBackup();
        mAllDAilyTaskRowsNotBackup = mDailyTaskDao.getAllRowsDailyTaskTableNotBackup();
    }

    LiveData<List<ToDo>> getAllToDoItems(){
        return mAllToDoItems;
    }

    LiveData<List<DailyTasks>> getAllTasks(String selecteddate, String useremail) { return  mDailyTaskDao.getAllTasks(selecteddate, useremail); }

    LiveData<List<DailyTasks>> getAllRowsDailyTaskTableNotBackup() { return  mAllDAilyTaskRowsNotBackup; }

    LiveData<List<ToDo>> getAllItemsByEndDate(){
        return mAllItemsByEndDate;
    }

    LiveData<List<ToDo>> getIncompleteTodoTask(String date, String useremail) {
        return mToDoDao.getIncompleteTodoTask(date, useremail);
    }

    LiveData<List<ToDo>> getAllItemsByName(String name) { return mToDoDao.getItemsByName(name); }

    LiveData<ToDo>  getItemById(long id) { return mToDoDao.getItemById(id); }

    LiveData<List<ToDo>>  getItemForEndDate(String end_date, String useremail) { return mToDoDao.getAllItemsForEndDate(end_date, useremail); }

    LiveData<List<ToDo>> getIncompleteItemsFromPreviousDate(String end_date, int completion){
        return mToDoDao.getIncompleteItemsFromPreviousDate(end_date, completion);
    }

    LiveData<List<ToDo>> getAllRowsNotBackup(){ return mAllRowsNotBackup; }

    LiveData<Double> getAllQ1Tasks(String date, String useremail){ return mDailyTaskDao.getAllQ1Tasks(date, useremail); }

    LiveData<Double> getAllQ2Tasks(String date, String useremail){ return mDailyTaskDao.getAllQ2Tasks(date, useremail); }

    LiveData<Double> getAllQ3Tasks(String date, String useremail){ return mDailyTaskDao.getAllQ3Tasks(date, useremail); }

    LiveData<Double> getAllQ4Tasks(String date, String useremail){ return mDailyTaskDao.getAllQ4Tasks(date, useremail); }

    LiveData<Double> getAllQTasksForToday(String date, String useremail){ return mDailyTaskDao.getAllQTasksForToday(date, useremail); }


    LiveData<Double> getAllQ1TasksForWeek(String date, String lowerdate, String useremail){ return mDailyTaskDao.getAllQ1TasksForWeek(date, lowerdate, useremail); }

    LiveData<Double> getAllQ2TasksForWeek(String date, String lowerdate, String useremail){ return mDailyTaskDao.getAllQ2TasksForWeek(date, lowerdate,useremail); }

    LiveData<Double> getAllQ3TasksForWeek(String date, String lowerdate, String useremail){ return mDailyTaskDao.getAllQ3TasksForWeek(date, lowerdate, useremail); }

    LiveData<Double> getAllQ4TasksForWeek(String date, String lowerdate, String useremail){ return mDailyTaskDao.getAllQ4TasksForWeek(date, lowerdate, useremail); }

    LiveData<Double> getAllQTasksForWeek(String date, String lowerdate, String useremail){ return mDailyTaskDao.getAllQTasksForWeek(date, lowerdate, useremail); }

    LiveData<Integer> getCountOfAllTaskForDate(String date, String useremail){
        return mToDoDao.getCountOfAllTaskForDate(date, useremail);
    }

    LiveData<Integer> getCountOfAllCompletedTaskForDate(String date, String useremail){
        return mToDoDao.getCountOfAllCompletedTaskForDate(date, useremail);
    }

    public void insert(ToDo toDo){ new insertAsyncTask(mToDoDao).execute(toDo); }

    public void insert(DailyTasks dailyTasks){ new insertDailyTaskAsyncTask(mDailyTaskDao).execute(dailyTasks); }

    public void deleteAll(){
        new deleteAllAsyncTask(mToDoDao).execute();
    }

    public void delete(ToDo toDo){
        new deleteAsyncTask(mToDoDao).execute(toDo);
    }

    public void update(ToDo toDo){
        new updateAsyncTask(mToDoDao).execute(toDo);
    }

    public void updatecompletion(long id){
        new updateOneAsyncTask(mToDoDao).execute(id);
    }

    public void updateBackup(long id) { new updateBackupAsyncTask(mToDoDao).execute(id); }

    public void updateDailyTaskTableBackup(long id) { new updateDailyTaskBackupAsyncTask(mDailyTaskDao).execute(id); }

    private static class insertAsyncTask extends AsyncTask<ToDo, Void, Void>{
        private ToDoDao mAsyncTaskDao;

        insertAsyncTask(ToDoDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(ToDo... toDos) {
            mAsyncTaskDao.insert(toDos[0]);
            return null;
        }
    }

    private static class insertDailyTaskAsyncTask extends AsyncTask<DailyTasks, Void, Void>{
        private DailyTasksDao mAsyncTaskDao;

        insertDailyTaskAsyncTask(DailyTasksDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(DailyTasks... dailyTasks) {
            mAsyncTaskDao.insert(dailyTasks[0]);
            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<ToDo, Void, Void>{
        private ToDoDao mAsyncTaskDao;

        deleteAllAsyncTask(ToDoDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(ToDo... toDos) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<ToDo, Void, Void>{
        private ToDoDao mAsyncTaskDao;

        deleteAsyncTask(ToDoDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(ToDo... toDos) {
            mAsyncTaskDao.delete(toDos[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<ToDo, Void, Void>{
        private ToDoDao mAsyncTaskDao;

        updateAsyncTask(ToDoDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(ToDo... toDos) {
            mAsyncTaskDao.updateTodoItems(toDos[0]);
            return null;
        }
    }

    private static class updateOneAsyncTask extends AsyncTask<Long, Void, Void>{
        private ToDoDao mAsyncTaskDao;

        updateOneAsyncTask(ToDoDao dao){
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(Long... longs) {
            mAsyncTaskDao.updatecompletion(longs[0]);
            return null;
        }
    }

    private static class updateBackupAsyncTask extends AsyncTask<Long, Void, Void>{
        private ToDoDao mAsyncTaskDao;

        updateBackupAsyncTask(ToDoDao dao) { mAsyncTaskDao = dao; }

        @Override
        protected Void doInBackground(Long... longs) {
            mAsyncTaskDao.updateToDoTableBackup(longs[0]);
            return null;
        }
    }

    private static class updateDailyTaskBackupAsyncTask extends AsyncTask<Long, Void, Void>{
        private DailyTasksDao mAsyncTaskDao;

        updateDailyTaskBackupAsyncTask(DailyTasksDao dao) { mAsyncTaskDao = dao; }

        @Override
        protected Void doInBackground(Long... longs) {
            mAsyncTaskDao.updateDailyTaskTableBackup(longs[0]);
            return null;
        }
    }
}
