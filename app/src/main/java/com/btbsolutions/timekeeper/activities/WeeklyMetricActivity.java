package com.btbsolutions.timekeeper.activities;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.TooltipCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btbsolutions.timekeeper.AdvertisementActivity;
import com.btbsolutions.timekeeper.MetricValuesActivity;
import com.btbsolutions.timekeeper.R;
import com.btbsolutions.timekeeper.utility.Helpers;
import com.btbsolutions.timekeeper.utility.ToDoViewModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WeeklyMetricActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView metric_Q1_tasks_tv, metric_Q2_tasks_tv, metric_Q3_tasks_tv, metric_Q4_tasks_tv;
    private TextView tv_lower_date, tv_upper_date, result_matrix_textview, nav_bar_username, nav_bar_email;
    private ImageView mAdView;
    private int adNumber;
    private ToDoViewModel mToDoViewModel;

    double Q1size, Q2size, Q3size, Q4size;
    double weeklyQValues = 0.0f;

    private SharedPreferences mPreferences;
    SharedPreferences.Editor editor;

    private String lowerDate, currentDate;
    private Thread thread;
    private boolean thread_continue = true;

    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_metric);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String sharedPrefFile = "com.btbsolutions.timekeeper.sharedpreffile";
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        final String email = mPreferences.getString("email", null);
        String user = mPreferences.getString("username", null);

        mToDoViewModel = ViewModelProviders.of(this).get(ToDoViewModel.class);

        metric_Q1_tasks_tv = findViewById(R.id.metric_Q1_tasks_tv);
        metric_Q2_tasks_tv = findViewById(R.id.metric_Q2_tasks_tv);
        metric_Q3_tasks_tv = findViewById(R.id.metric_Q3_tasks_tv);
        metric_Q4_tasks_tv = findViewById(R.id.metric_Q4_tasks_tv);
        tv_lower_date = findViewById(R.id.tv_lower_date);
        tv_upper_date = findViewById(R.id.tv_upper_date);
        result_matrix_textview = findViewById(R.id.result_matrix_textview);

        metric_Q1_tasks_tv = findViewById(R.id.metric_Q1_tasks_tv);
        metric_Q2_tasks_tv = findViewById(R.id.metric_Q2_tasks_tv);
        metric_Q3_tasks_tv = findViewById(R.id.metric_Q3_tasks_tv);
        metric_Q4_tasks_tv = findViewById(R.id.metric_Q4_tasks_tv);

        TextView ib_q1_help = findViewById(R.id.tv_q1_head);
        TextView ib_q2_help = findViewById(R.id.tv_q2_head);
        TextView ib_q3_help = findViewById(R.id.tv_q3_head);
        TextView ib_q4_help = findViewById(R.id.tv_q4_head);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ib_q1_help.setTooltipText("Important and Urgent Tasks");
            ib_q2_help.setTooltipText("Important but not Urgent Tasks");
            ib_q3_help.setTooltipText("Urgent but not Important Tasks");
            ib_q4_help.setTooltipText("Neither Important nor Urgent Tasks");
        } else {
            TooltipCompat.setTooltipText(ib_q1_help, "Important and Urgent Tasks");
            TooltipCompat.setTooltipText(ib_q2_help, "Important but not Urgent Tasks");
            TooltipCompat.setTooltipText(ib_q3_help, "Urgent but not Important Tasks");
            TooltipCompat.setTooltipText(ib_q4_help, "Neither Important nor Urgent Tasks");
        }

        Calendar c = Calendar.getInstance();

        currentDate = simpleDateFormat.format(new Date(c.getTimeInMillis()));
        c.add(Calendar.DAY_OF_MONTH, -7);
        lowerDate = simpleDateFormat.format(new Date(c.getTimeInMillis()));
        tv_upper_date.setText(currentDate);
        tv_lower_date.setText(lowerDate);
        fillPage(currentDate, lowerDate, email);
        mAdView = findViewById(R.id.adView);

        /*MobileAds.initialize(this,
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
                                Random random = new Random();
                                adNumber = random.nextInt(3);
                                //mAdView.setImageResource(new Helpers().getAdImage(adNumber));
                                Glide.with(WeeklyMetricActivity.this)
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
            Intent intent = new Intent(WeeklyMetricActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_metric_activity) {
            Intent intent = new Intent(WeeklyMetricActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_daily_activity) {
            Intent intent = new Intent(WeeklyMetricActivity.this, DailyTaskActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_calendar) {
            Intent intent = new Intent(WeeklyMetricActivity.this, CalendarActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(WeeklyMetricActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
        } else if(id == R.id.nav_weekly_metric_activity){
            onBackPressed();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void fillPage(String date, String lowerdate, String email) {
        //Intent metricIntent = new Intent(this, MetricValuesActivity.class);

        mToDoViewModel.getAllQ1TasksForWeek(date, lowerdate, email).observe(this, new Observer<Double>() {
            @Override
            public void onChanged(@Nullable Double strings) {
                if (strings != null) {
                    Q1size = strings;
                } else {
                    Q1size = 0;
                }
                weeklyQValues = Q1size + Q2size + Q3size + Q4size;
                String val;
                if (weeklyQValues != 0) {
                    val = decimalFormat.format(((Q1size * 100) / weeklyQValues)) + "%";
                } else {
                    val = "0%";
                }
                metric_Q1_tasks_tv.setText(val);
                updateTable();
                updateResult();
            }
        });

        mToDoViewModel.getAllQ2TasksForWeek(date, lowerdate, email).observe(this, new Observer<Double>() {
            @Override
            public void onChanged(@Nullable Double strings) {
                if (strings != null) {
                    Q2size = strings;
                } else {
                    Q2size = 0;
                }
                weeklyQValues = Q1size + Q2size + Q3size + Q4size;
                String val;
                if (weeklyQValues != 0) {
                    double num = ((Q2size * 100) / weeklyQValues);
                    val = decimalFormat.format(num) + "%";
                } else {
                    val = "0%";
                }
                metric_Q2_tasks_tv.setText(val);
                updateTable();
                updateResult();
            }
        });

        mToDoViewModel.getAllQ3TasksForWeek(date, lowerdate, email).observe(this, new Observer<Double>() {
            @Override
            public void onChanged(@Nullable Double strings) {
                if (strings != null) {
                    Q3size = strings;
                } else {
                    Q3size = 0;
                }
                weeklyQValues = Q1size + Q2size + Q3size + Q4size;
                String val;
                if (weeklyQValues != 0) {
                    val = decimalFormat.format(((Q3size * 100) / weeklyQValues)) + "%";
                } else {
                    val = "0%";
                }
                metric_Q3_tasks_tv.setText(val);
                updateTable();
                updateResult();
            }
        });

        mToDoViewModel.getAllQ4TasksForWeek(date, lowerdate, email).observe(this, new Observer<Double>() {
            @Override
            public void onChanged(@Nullable Double strings) {
                if (strings != null) {
                    Q4size = strings;
                } else {
                    Q4size = 0;
                }
                weeklyQValues = Q1size + Q2size + Q3size + Q4size;
                String val;
                if (weeklyQValues != 0) {
                    val = decimalFormat.format(((Q4size * 100) / weeklyQValues)) + "%";
                } else {
                    val = "0%";
                }
                metric_Q4_tasks_tv.setText(val);
                updateTable();
                updateResult();
            }
        });
    }

    private void updateTable() {
        String val1, val2, val3, val4;
        if (weeklyQValues != 0) {
            val1 = decimalFormat.format((Q1size * 100) / weeklyQValues) + "%";
            val2 = decimalFormat.format((Q2size * 100) / weeklyQValues) + "%";
            val3 = decimalFormat.format((Q3size * 100) / weeklyQValues) + "%";
            val4 = decimalFormat.format((Q4size * 100) / weeklyQValues) + "%";
        } else {
            val1 = "0%";
            val2 = "0%";
            val3 = "0%";
            val4 = "0%";
        }
        metric_Q1_tasks_tv.setText(val1);
        metric_Q2_tasks_tv.setText(val2);
        metric_Q3_tasks_tv.setText(val3);
        metric_Q4_tasks_tv.setText(val4);
    }

    private void updateResult() {
        StringBuilder result = new StringBuilder();

        if(weeklyQValues != 0){
            if(Q2size>0.3){
                result.append("Excellent: You are managing time well and pursuing your goals effectively.\n");
            } else if(Q2size>0.2){
                result.append("Good: You are thinking long term and devoting time to planning, organisational development, improvement and self development.\n");
            } else {
                result.append("Not good:You are a crisis manager. As you are not spending enough time on important tasks, they become urgent and disturb your time plan.\n");
            }

            if(Q3size>0.35){
                result.append("Serious problem: You will be running all over the place and goals will still not be achieved.\n");
            } else if(Q3size>0.15){
                result.append("Caution: Need to be assertive, learn to say 'No' and delegate.\n");
            } else {
                result.append("Excellent control over urgent but not important matters.\n");
            }

            if(Q4size>0.1){
                result.append("Serious problem: Need to closely examine the trivial time wasters, and eliminate or minimise them.");
            } else if(Q4size>0.05){
                result.append("Caution: You are spending too much time on trivia.");
            } else {
                result.append("Excellent control over trivial time wasters.");
            }

            result_matrix_textview.setText(result.toString());
        }
        else {
            result.append("No activity data for these dates");
            result_matrix_textview.setText((result.toString()));
        }
    }
}
