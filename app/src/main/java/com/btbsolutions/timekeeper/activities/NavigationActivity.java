package com.btbsolutions.timekeeper.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.btbsolutions.timekeeper.AddDailyTask;
import com.btbsolutions.timekeeper.AdvertisementActivity;
import com.btbsolutions.timekeeper.LoginForm;
import com.btbsolutions.timekeeper.NightNotificationReceiver;
import com.btbsolutions.timekeeper.NotificationTipsReceiver;
import com.btbsolutions.timekeeper.R;
import com.btbsolutions.timekeeper.activities.CalendarActivity;
import com.btbsolutions.timekeeper.activities.DailyTaskActivity;
import com.btbsolutions.timekeeper.activities.MainActivity;
import com.btbsolutions.timekeeper.activities.SettingsActivity;
import com.btbsolutions.timekeeper.activities.WeeklyMetricActivity;
import com.btbsolutions.timekeeper.utility.Helpers;
import com.btbsolutions.timekeeper.utility.ToDoViewModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NavigationActivity extends AppCompatActivity implements Runnable {

    private ImageView mAdView;
    private TextView tv_welcome;
    private int adNumber;

    private String currentDate;

    private SharedPreferences mPreferences;
    SharedPreferences.Editor editor;

    private ToDoViewModel mToDoViewModel;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    private static final int ALARM_TIPS_NOTIFICATION = 479;

    static PendingIntent tipsPendingIntent;
    private Thread thread;
    private boolean thread_continue = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        String sharedPrefFile = "com.btbsolutions.timekeeper.sharedpreffile";
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        editor = mPreferences.edit();

        tv_welcome = findViewById(R.id.tv_welcome);

        boolean firstLogin = mPreferences.getBoolean("firstLogin", false);
        if(!firstLogin){
            tv_welcome.setText(R.string.welcome_first_time);
            tv_welcome.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_welcome_green_60dp, 0, 0);
            editor.putBoolean("firstLogin", true);
            editor.apply();
        }
        else{
            tv_welcome.setText(R.string.welcome_subsequent);
            tv_welcome.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_wb_sunny_yellow_60dp, 0, 0);
        }

        mToDoViewModel = ViewModelProviders.of(this).get(ToDoViewModel.class);

        Calendar c = Calendar.getInstance();

        currentDate = simpleDateFormat.format(new Date(c.getTimeInMillis()));

        /*
         *Making/Resetting of the notifications
         */

        Calendar now = Calendar.getInstance();
        Calendar tipsTime = Calendar.getInstance();
        tipsTime.set(Calendar.HOUR_OF_DAY, 7);
        tipsTime.set(Calendar.MINUTE, 0);
        tipsTime.set(Calendar.SECOND, 0);

        if (now.after(tipsTime)) {
            tipsTime.add(Calendar.DAY_OF_MONTH, 1);
        }

        Log.d("AlarmTimes", "tips " + simpleDateTimeFormat.format(new Date(tipsTime.getTimeInMillis())));

        Intent tipsNotifyIntent = new Intent(this, NotificationTipsReceiver.class);
        tipsPendingIntent = PendingIntent.getBroadcast(this, ALARM_TIPS_NOTIFICATION, tipsNotifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager tipsAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        tipsAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, tipsTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, tipsPendingIntent);

        now.set(Calendar.MILLISECOND, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.HOUR, 0);
        now.set(Calendar.SECOND, 0);

        editor.putLong("last_access", now.getTimeInMillis());
        editor.apply();

        mAdView = findViewById(R.id.adView);
       /* MobileAds.initialize(this,
                getString(R.string.ad_appId));

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (thread_continue) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Log.d("ThreadRunning", "running");
                                Random random = new Random();
                                adNumber = random.nextInt(3);
                                //mAdView.setImageResource(new Helpers().getAdImage(adNumber));
                                Glide.with(NavigationActivity.this)
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

    @Override
    protected void onDestroy() {
        thread_continue = false;
        thread.interrupt();
        super.onDestroy();
    }

    public static void cancelIntent(){
        Log.d("AlarmTimes", "pending intent cancel");
        if(tipsPendingIntent!=null)
            tipsPendingIntent.cancel();
    }

    public void toSettings(View view) {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    public void toTodo(View view) {
        Intent todoIntent = new Intent(this, CalendarActivity.class);
        startActivity(todoIntent);
    }

    public void toDailyTask(View view) {
        Intent dailyTaskIntent = new Intent(this, DailyTaskActivity.class);
        startActivity(dailyTaskIntent);
    }

    public void toDailyMetric(View view) {
        Intent dailyMetricIntent = new Intent(this, MainActivity.class);
        startActivity(dailyMetricIntent);
    }

    public void toWeeklyMetric(View view) {
        Intent weeklyMetricIntent = new Intent(this, WeeklyMetricActivity.class);
        startActivity(weeklyMetricIntent);
    }

    public void toAddDailyTask(View view) {
        Intent addDailyTaskIntent = new Intent(this, AddDailyTask.class);
        addDailyTaskIntent.putExtra("ActivityName", "DailyTask");
        startActivity(addDailyTaskIntent);
    }

    @Override
    public void run() {
        try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Random random = new Random();
                        adNumber = random.nextInt(3);
                        //mAdView.setImageResource(new Helpers().getAdImage(adNumber));
                        Glide.with(NavigationActivity.this)
                                .load(new Helpers().getAdImage(adNumber))
                                .into(mAdView);
                    }
                });
                Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
