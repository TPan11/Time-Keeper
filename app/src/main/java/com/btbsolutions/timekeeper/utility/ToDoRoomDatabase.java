package com.btbsolutions.timekeeper.utility;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

@Database(entities = {ToDo.class, DailyTasks.class}, version = 2, exportSchema = false)
public abstract class ToDoRoomDatabase extends RoomDatabase {

    public abstract ToDoDao toDoDao();
    public abstract DailyTasksDao dailyTasksDao();
    private static ToDoRoomDatabase INSTANCE;

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE daily_task_table ADD COLUMN useremail TEXT NOT NULL DEFAULT 'pant@345.com'");
        }
    };

    static ToDoRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (ToDoRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ToDoRoomDatabase.class, "todo_database")
                            .addMigrations(MIGRATION_1_2)
                            //.fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            //new PopulatedbAsync(INSTANCE).execute();
        }
    };

    private static class PopulatedbAsync extends AsyncTask<Void, Void, Void>{

        private final ToDoDao mTodoDao;
        private final DailyTasksDao mDailyTaskDao;
        String[] toDoItems = {"Work", "Sleep", "Eat"};
        int[] priorities = {2, 3, 1};
        long[] end_date = {1551033000, 1550082600, 1550082600};

        List<String> tasks = new ArrayList<>();

        String[] details = {
                "Lorem Ipsum",
                "Do re mi sa",
                "The sound of music"
        };

        PopulatedbAsync(ToDoRoomDatabase db){
            mTodoDao = db.toDoDao();
            mDailyTaskDao = db.dailyTasksDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //mTodoDao.deleteAll();
            //mDailyTaskDao.deleteAll();
            for (int i=0; i < toDoItems.length; i++){
                //ToDo toDo = new ToDo(toDoItems[i], priorities[i], end_date[i], details[i], null);
                //mTodoDao.insert(toDo);
                //Log.d("enteredDate", Long.toString(toDo.getTimestamp()));
            }
            return null;
        }
    }
}
