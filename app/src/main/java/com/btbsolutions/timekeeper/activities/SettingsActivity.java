package com.btbsolutions.timekeeper.activities;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.btbsolutions.timekeeper.AdvertisementActivity;
import com.btbsolutions.timekeeper.BuildConfig;
import com.btbsolutions.timekeeper.HowToGuide;
import com.btbsolutions.timekeeper.LoginForm;
import com.btbsolutions.timekeeper.R;
import com.btbsolutions.timekeeper.asyncClasses.BackupDailyTaskAsync;
import com.btbsolutions.timekeeper.asyncClasses.BackupTodoAsync;
import com.btbsolutions.timekeeper.asyncClasses.LogoutAsync;
import com.btbsolutions.timekeeper.interfaces.BackupDailyTaskResponse;
import com.btbsolutions.timekeeper.interfaces.BackupTodoResponse;
import com.btbsolutions.timekeeper.utility.DailyTasks;
import com.btbsolutions.timekeeper.utility.Helpers;
import com.btbsolutions.timekeeper.utility.ToDo;
import com.btbsolutions.timekeeper.utility.ToDoViewModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class SettingsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BackupTodoResponse, BackupDailyTaskResponse {

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.btbsolutions.timekeeper.sharedpreffile";
    SharedPreferences.Editor editor;

    private static final int ALARM_BACKUP = 123;
    private static final int ALARM_FORWARD = 345;

    //private PendingIntent pendingIntent, completeAlarmPendingIntent;

    private ToDoViewModel mToDoViewModel;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    BroadcastReceiver broadcastReceiver, broadcastReceiver2;
    IntentFilter inf1, inf2;
    private Thread thread;
    private boolean thread_continue = true;

    List<ToDo> itemsToBackup;
    List<DailyTasks> itemsDailyTaskBackup;
    List<ToDo> itemsTodoIncomplete;
    TextView nav_bar_username, nav_bar_email, tv_VersionName, tv_VersionNo;

    private ImageView mAdView;
    private int adNumber;

    private static SettingsActivity inst;

    public static SettingsActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        editor = mPreferences.edit();

        mToDoViewModel = ViewModelProviders.of(this).get(ToDoViewModel.class);

        itemsToBackup = new ArrayList<>();
        itemsDailyTaskBackup = new ArrayList<>();
        itemsTodoIncomplete = new ArrayList<>();

        final String email = mPreferences.getString("email", null);
        String user = mPreferences.getString("username", null);

        tv_VersionName = findViewById(R.id.tv_VersionName);
        tv_VersionNo = findViewById(R.id.tv_VersionNo);

        String versionNo = String.valueOf(BuildConfig.VERSION_CODE);
        String versionName = BuildConfig.VERSION_NAME;

        tv_VersionName.setText(versionName);
        tv_VersionNo.setText(versionNo);

        mToDoViewModel.getAllRowsNotBackup().observe(this, new Observer<List<ToDo>>() {
            @Override
            public void onChanged(@Nullable List<ToDo> toDos) {
                //Log.d("check_backup", "Backup put in list");
                itemsToBackup.clear();
                //Log.d("check_backup", String.valueOf(toDos.size()));
                itemsToBackup.addAll(toDos);

            }
        });

        mToDoViewModel.getAllRowsDailyTaskTableNotBackup().observe(this, new Observer<List<DailyTasks>>() {
            @Override
            public void onChanged(@Nullable List<DailyTasks> dailyTasks) {
                //Log.d("check_backup", "Backup put in list");
                itemsDailyTaskBackup.clear();
                itemsDailyTaskBackup.addAll(dailyTasks);

            }
        });

        Calendar todaysDate = Calendar.getInstance();
        todaysDate.add(Calendar.DAY_OF_MONTH, -1);
        String currentDate = simpleDateFormat.format(todaysDate.getTime());

        mToDoViewModel.getIncompleteTodoTask(currentDate, email).observe(this, new Observer<List<ToDo>>() {
            @Override
            public void onChanged(@Nullable List<ToDo> toDos) {
                itemsTodoIncomplete.clear();
                itemsTodoIncomplete.addAll(toDos);
            }
        });

        /*MobileAds.initialize(this,
                getString(R.string.ad_appId));

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/
        mAdView = findViewById(R.id.adView);

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (thread_continue) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Random random = new Random();
                                adNumber = random.nextInt(3);
                                //mAdView.setImageResource(new Helpers().getAdImage(adNumber));
                                Glide.with(SettingsActivity.this)
                                        .load(new Helpers().getAdImage(adNumber))
                                        .into(mAdView);
                            }
                        });
                        Thread.sleep(10000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        final Intent adIntent = new Intent(this, AdvertisementActivity.class);

        mAdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adIntent.putExtra("adnumber",adNumber);
                startActivity(adIntent);
            }
        });
    }

    public void carryForward() {
        Calendar calendar = Calendar.getInstance();
        String currentDate = simpleDateFormat.format(new Date(calendar.getTimeInMillis()));
        for (ToDo toDo: itemsTodoIncomplete){
            toDo.setDate(currentDate);
            toDo.setId(0);
            toDo.setBackup(false);
            mToDoViewModel.insert(toDo);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            thread_continue = false;
            thread.interrupt();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, inf1);
        registerReceiver(broadcastReceiver2, inf2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        unregisterReceiver(broadcastReceiver2);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_metric_activity) {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_daily_activity) {
            Intent intent = new Intent(SettingsActivity.this, DailyTaskActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_calendar) {
            Intent intent = new Intent(SettingsActivity.this, CalendarActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_settings) {
            onBackPressed();
        } else if(id == R.id.nav_weekly_metric_activity){
            Intent intent = new Intent(SettingsActivity.this, WeeklyMetricActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openEmail(View view) {
        String mailto = "mailto:developerpande@gmail.com" +
                "?subject=" + Uri.encode("Bug Report") +
                "&body=" + Uri.encode("Bug Report For Time Management Application");

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse(mailto));

        /*emailIntent.putExtra(Intent.EXTRA_EMAIL, "developerpande@gmail.com");
        emailIntent.putExtra(Intent.EXTRA_CC, "");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Bug Report");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Bug Report For Time Management Application");*/

        try {
            if (emailIntent.resolveActivity(getPackageManager()) != null) {
                Toast.makeText(getApplicationContext(), "Sending Email", Toast.LENGTH_SHORT).show();
                startActivity(emailIntent);
            } else {
                Toast.makeText(getApplicationContext(), "No Email App Available", Toast.LENGTH_SHORT).show();
            }

        } catch (ActivityNotFoundException e) {
            //TODO: Handle case where no email app is available
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        /*String txt = "Hi,/n My Name is ";
        String mimeType = "text/plain";
        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle("Share this text with: ")
                .setText(txt)
                .startChooser();*/
    }

    public void onClickBackup(View view) {
        Toast.makeText(this, "Starting Backup", Toast.LENGTH_LONG).show();
        startBackup();
        Toast.makeText(this, "Backup Complete", Toast.LENGTH_LONG).show();
    }


    public void startBackup(){
        //Log.d("check_backup", "Backup Clicked");

        backupToDoHelper(itemsToBackup);
        backupDailyTaskHelper(itemsDailyTaskBackup);

    }

    void backupToDoHelper(List<ToDo> itemsToBackup) {

        String type = "backup";
        String useremail = mPreferences.getString("email", null);

        //Toast.makeText(this, "Starting backup...", Toast.LENGTH_SHORT).show();

        for (ToDo toDo : itemsToBackup) {

            //Log.d("check_backup", "Backing up " + toDo.getId());

            String id = String.valueOf(toDo.getId());
            String task = toDo.getTodotask();
            String date = toDo.getDate();
            String priority = String.valueOf(toDo.getPriority());
            String detailedNotes = toDo.getDetailed_notes();
            String duration = String.valueOf(toDo.getDuration());
            String complete = String.valueOf(toDo.getComplete());
            BackupTodoAsync backupAsync = new BackupTodoAsync();
            backupAsync.delegate = this;
            backupAsync.todoId = toDo.getId();
            backupAsync.execute(id, task, date, priority, detailedNotes, duration, complete, useremail);
        }
    }

    void backupDailyTaskHelper(List<DailyTasks> itemsToBackup) {

        String type = "backup";
        String useremail = mPreferences.getString("email", null);

        //Toast.makeText(this, "Starting backup...", Toast.LENGTH_SHORT).show();

        for (DailyTasks dailyTasks : itemsToBackup) {

       //     Log.d("check_backup", "Backing up " + dailyTasks.getId());

            String id = String.valueOf(dailyTasks.getId());
            String task = dailyTasks.getTasks();
            String date = dailyTasks.getDate();
            String start_time = dailyTasks.getStrtTime();
            String end_time = dailyTasks.getEndTime();
            String metricQ = dailyTasks.getMetricQ();
            String duration = String.valueOf(dailyTasks.getDuration());
            String complete = String.valueOf(dailyTasks.getComplete());

            BackupDailyTaskAsync backupDailyTaskAsync = new BackupDailyTaskAsync();
            backupDailyTaskAsync.delegate = this;
            backupDailyTaskAsync.taskId = dailyTasks.getId();
            backupDailyTaskAsync.execute(id, task, date, start_time, end_time, metricQ, duration, complete, useremail);

        }
    }

    public void onLogout(View view) {

        final String type = "logout";

     //   Log.d("check_backup", "Backup Clicked");

        final List<ToDo> itemsToBackup = new ArrayList<>();

        mToDoViewModel.getAllRowsNotBackup().observe(this, new Observer<List<ToDo>>() {
            @Override
            public void onChanged(@Nullable List<ToDo> toDos) {
    //            Log.d("check_backup", "Backup put in list");
                itemsToBackup.clear();
                itemsToBackup.addAll(toDos);
                backupToDoHelper(itemsToBackup);
            }
        });

        mToDoViewModel.getAllRowsDailyTaskTableNotBackup().observe(this, new Observer<List<DailyTasks>>() {
            @Override
            public void onChanged(@Nullable List<DailyTasks> dailyTasks) {
                Log.d("check_backup", "Backup put in list");
                itemsDailyTaskBackup.clear();
                itemsDailyTaskBackup.addAll(dailyTasks);
                backupDailyTaskHelper(itemsDailyTaskBackup);
            }
        });

        SharedPreferences.Editor editor = mPreferences.edit();

        editor.clear();
        editor.apply();

        Intent logoutintent = new Intent(this, LoginForm.class);
        logoutintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logoutintent);
        finish();

    }

    void logouthelper(List<ToDo> itemsToBackup) {
        String useremail = mPreferences.getString("email", null);

        //Toast.makeText(this, "Starting backup...", Toast.LENGTH_SHORT).show();

        for (ToDo toDo : itemsToBackup) {

     //       Log.d("check_backup", "Backing up " + toDo.getId());

            String id = String.valueOf(toDo.getId());
            String task = toDo.getTodotask();
            String date = toDo.getDate();
            String priority = String.valueOf(toDo.getPriority());
            String detailedNotes = toDo.getDetailed_notes();
            String duration = String.valueOf(toDo.getDuration());
            String complete = String.valueOf(toDo.getComplete());
            try {
                String temp = new LogoutAsync(this)
                        .execute(id, task, date, priority, detailedNotes, duration, complete, useremail).get();
                Toast.makeText(this, temp, Toast.LENGTH_LONG).show();
                if (temp.equals("Inserted Successfully")) {
                    mToDoViewModel.updateBackup(toDo.getId());
                } else {
                    Toast.makeText(this, "Back failed for " + toDo.getId(), Toast.LENGTH_SHORT).show();

                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        editor = mPreferences.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    public void backupTodoProcessFinish(String output, long id) {
        if (output.equals("Inserted Successfully")) {
            mToDoViewModel.updateBackup(id);
        } else {
            Toast.makeText(this, "Backup failed for " + id, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void backupDailyTaskProcessFinish(String output, long id) {
        if (output.equals("Inserted Successfully")) {
            mToDoViewModel.updateDailyTaskTableBackup(id);
        } else {
            Toast.makeText(this, "Backup failed for " + id, Toast.LENGTH_SHORT).show();
        }
    }

    public void onHowToGuide(View view) {
        Intent howToGuideIntent = new Intent(this, HowToGuide.class);
        startActivity(howToGuideIntent);
    }

    public void onAboutUs(View view) {
        String url = "http://betterthanbeforesolution.com/";
        Uri webpage = Uri.parse(url);
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        if (webIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(webIntent);
        }
    }

    public void onShare(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Download Time Keeper Now");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "I am using Time Keeper to improve personal " +
                "productivity. \nYou too can become better at managing time. \nDownload Time Keeper now!!!" +
                "\nhttps://play.google.com/store/apps/details?id=com.btbsolutions.timekeeper");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share with"));
    }

    public void onPrivacyPolicy(View view) {
        String url = "http://betterthanbeforesolution.com/TimeKeeper/PrivacyPolicy.html";
        Uri webpage = Uri.parse(url);
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        if (webIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(webIntent);
        }
    }
}
