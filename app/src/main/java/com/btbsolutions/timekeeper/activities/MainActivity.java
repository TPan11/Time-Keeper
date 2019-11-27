package com.btbsolutions.timekeeper.activities;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.TooltipCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btbsolutions.timekeeper.AdvertisementActivity;
import com.btbsolutions.timekeeper.MetricValuesActivity;
import com.btbsolutions.timekeeper.NightNotificationReceiver;
import com.btbsolutions.timekeeper.R;
import com.btbsolutions.timekeeper.utility.Helpers;
import com.btbsolutions.timekeeper.utility.ToDoViewModel;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences mPreferences;

    private TextView metric_Q1_tasks_tv, metric_Q2_tasks_tv, metric_Q3_tasks_tv, metric_Q4_tasks_tv;
    private TextView result_todo_progress_textview, selected_date_textview, result_matrix_textview;
    private ToDoViewModel mToDoViewModel;

    //private AlarmManager manager;

    TextView nav_bar_username, nav_bar_email, todo_progress_tv, tv_tips_main;
    View progress_max;
    SharedPreferences.Editor editor;

    int total_todo_task_for_date = 0, completed_todo_task_for_date = 0, max_width;
    double Q1size, Q2size, Q3size, Q4size;
    double totalQValues = 0.0f;
    public static double todaysTotalQ = 0.0f;
    double percent_complete;
    private String selectedDate, currentDate;

    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    ImageButton back_date, forward_date;
    StringBuilder dailytip;
    private ImageView mAdView;
    private int adNumber;

    private static final int ALARM_NOTIFICATION = 987;

    static PendingIntent pendingIntent;
    private Thread thread;
    private boolean thread_continue = true;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        mToDoViewModel = ViewModelProviders.of(this).get(ToDoViewModel.class);

        todo_progress_tv = findViewById(R.id.todo_progress_tv);
        progress_max = findViewById(R.id.progress_max);
        result_todo_progress_textview = findViewById(R.id.result_todo_progress_textview);

        back_date = findViewById(R.id.back_date);
        forward_date = findViewById(R.id.forward_date);
        selected_date_textview = findViewById(R.id.selected_date_textview);
        result_matrix_textview = findViewById(R.id.result_matrix_textview);

        tv_tips_main = findViewById(R.id.tv_tips_main);

        String sharedPrefFile = "com.btbsolutions.timekeeper.sharedpreffile";
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        editor = mPreferences.edit();

        final String email = mPreferences.getString("email", null);
        String user = mPreferences.getString("username", null);

        metric_Q1_tasks_tv = findViewById(R.id.metric_Q1_tasks_tv);
        metric_Q2_tasks_tv = findViewById(R.id.metric_Q2_tasks_tv);
        metric_Q3_tasks_tv = findViewById(R.id.metric_Q3_tasks_tv);
        metric_Q4_tasks_tv = findViewById(R.id.metric_Q4_tasks_tv);

        TextView ib_q1_help = findViewById(R.id.tv_q1_head);
        TextView ib_q2_help = findViewById(R.id.tv_q2_head);
        TextView ib_q3_help = findViewById(R.id.tv_q3_head);
        TextView ib_q4_help = findViewById(R.id.tv_q4_head);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ib_q1_help.setTooltipText(getString(R.string.metric_tooltip_book_link));
            ib_q2_help.setTooltipText(getString(R.string.metric_tooltip_book_link));
            ib_q3_help.setTooltipText(getString(R.string.metric_tooltip_book_link));
            ib_q4_help.setTooltipText(getString(R.string.metric_tooltip_book_link));
        } else {
            TooltipCompat.setTooltipText(ib_q1_help, getString(R.string.metric_tooltip_book_link));
            TooltipCompat.setTooltipText(ib_q2_help, getString(R.string.metric_tooltip_book_link));
            TooltipCompat.setTooltipText(ib_q3_help, getString(R.string.metric_tooltip_book_link));
            TooltipCompat.setTooltipText(ib_q4_help, getString(R.string.metric_tooltip_book_link));
        }

        Calendar c = Calendar.getInstance();

        currentDate = simpleDateFormat.format(new Date(c.getTimeInMillis()));

        selectedDate = currentDate;
        fillPage(currentDate, email);

        back_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Date dt = simpleDateFormat.parse(selectedDate);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dt);
                    cal.add(Calendar.DAY_OF_MONTH, -1);
                    selectedDate = simpleDateFormat.format(new Date(cal.getTimeInMillis()));
                    fillPage(selectedDate, email);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

        forward_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectedDate.equals(currentDate)) {
                    try {
                        Date dt = simpleDateFormat.parse(selectedDate);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(dt);
                        cal.add(Calendar.DAY_OF_MONTH, 1);
                        selectedDate = simpleDateFormat.format(new Date(cal.getTimeInMillis()));
                        fillPage(selectedDate, email);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Calendar now = Calendar.getInstance();
        Calendar notificationTime = Calendar.getInstance();
        notificationTime.set(Calendar.HOUR_OF_DAY, 20);
        notificationTime.set(Calendar.MINUTE, 30);
        notificationTime.set(Calendar.SECOND, 0);

        if (now.after(notificationTime)) {
            notificationTime.add(Calendar.DAY_OF_MONTH, 1);
        }

        Log.d("AlarmTimes", "current " + simpleDateTimeFormat.format(new Date(now.getTimeInMillis())));
        Log.d("AlarmTimes", "notify " + simpleDateTimeFormat.format(new Date(notificationTime.getTimeInMillis())));

        mToDoViewModel.getAllQTasksForToday(currentDate, email).observe(this, new Observer<Double>() {
            @Override
            public void onChanged(@Nullable Double aDouble) {
                if (aDouble != null)
                    todaysTotalQ = aDouble;
                else
                    todaysTotalQ = 0.0f;
            }
        });
        Intent notifyIntent = new Intent(this, NightNotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, ALARM_NOTIFICATION, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, notificationTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        /*
         * Set the daily random tip
         */
        tv_tips_main.setText(findRandomTip());

        now.set(Calendar.MILLISECOND, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.HOUR, 0);
        now.set(Calendar.SECOND, 0);

        editor.putLong("last_access", now.getTimeInMillis());
        editor.apply();
        mAdView = findViewById(R.id.adView);
        /*
         * Initialize ads
         */
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
                                Random random = new Random();
                                adNumber = random.nextInt(3);
                                //mAdView.setImageResource(new Helpers().getAdImage(adNumber));
                                Glide.with(MainActivity.this)
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

    public static void cancelIntent(){
        if(pendingIntent != null)
            pendingIntent.cancel();
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
        getMenuInflater().inflate(R.menu.help_settings_menu, menu);
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
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.action_help) {
            showOverLay();
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
            onBackPressed();
        } else if (id == R.id.nav_daily_activity) {
            Intent intent = new Intent(MainActivity.this, DailyTaskActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_calendar) {
            Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_weekly_metric_activity) {
            Intent intent = new Intent(MainActivity.this, WeeklyMetricActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void fillPage(String date, String email) {
        Intent metricIntent = new Intent(this, MetricValuesActivity.class);
        selected_date_textview.setText(date);

        if (date.equals(currentDate)) {
            forward_date.setEnabled(false);
            forward_date.setImageResource(R.drawable.ic_chevron_right_disable_24dp);
        } else {
            forward_date.setEnabled(true);
            forward_date.setImageResource(R.drawable.ic_chevron_right_black_24dp);
        }

        mToDoViewModel.getAllQ1Tasks(date, email).observe(this, new Observer<Double>() {
            @Override
            public void onChanged(@Nullable Double strings) {
                if (strings != null) {
                    Q1size = strings;
                } else {
                    Q1size = 0;
                }
                totalQValues = Q1size + Q2size + Q3size + Q4size;
                String val;
                if (totalQValues != 0) {
                    val = decimalFormat.format(((Q1size * 100) / totalQValues)) + "%";
                } else {
                    val = "0%";
                }
                metric_Q1_tasks_tv.setText(val);
                updateTable();
                updateResult();
            }
        });

        mToDoViewModel.getAllQ2Tasks(date, email).observe(this, new Observer<Double>() {
            @Override
            public void onChanged(@Nullable Double strings) {
                if (strings != null) {
                    Q2size = strings;
                } else {
                    Q2size = 0;
                }
                totalQValues = Q1size + Q2size + Q3size + Q4size;
                String val;
                if (totalQValues != 0) {
                    double num = ((Q2size * 100) / totalQValues);
                    val = decimalFormat.format(num) + "%";
                } else {
                    val = "0%";
                }
                metric_Q2_tasks_tv.setText(val);
                updateTable();
                updateResult();
            }
        });

        mToDoViewModel.getAllQ3Tasks(date, email).observe(this, new Observer<Double>() {
            @Override
            public void onChanged(@Nullable Double strings) {
                if (strings != null) {
                    Q3size = strings;
                } else {
                    Q3size = 0;
                }
                totalQValues = Q1size + Q2size + Q3size + Q4size;
                String val;
                if (totalQValues != 0) {
                    val = decimalFormat.format(((Q3size * 100) / totalQValues)) + "%";
                } else {
                    val = "0%";
                }
                metric_Q3_tasks_tv.setText(val);
                updateTable();
                updateResult();
            }
        });

        mToDoViewModel.getAllQ4Tasks(date, email).observe(this, new Observer<Double>() {
            @Override
            public void onChanged(@Nullable Double strings) {
                if (strings != null) {
                    Q4size = strings;
                } else {
                    Q4size = 0;
                }
                totalQValues = Q1size + Q2size + Q3size + Q4size;
                String val;
                if (totalQValues != 0) {
                    val = decimalFormat.format(((Q4size * 100) / totalQValues)) + "%";
                } else {
                    val = "0%";
                }
                metric_Q4_tasks_tv.setText(val);
                updateTable();
                updateResult();
            }
        });

        mToDoViewModel.getCountOfAllTaskForDate(date, email).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer == null) {
                    total_todo_task_for_date = 0;
                } else {
                    total_todo_task_for_date = integer;
                }
                //       Log.d("Todo_count_all", String.valueOf(total_todo_task_for_date));
                setCompletionProgressBar();
                updateResult();
            }
        });

        mToDoViewModel.getCountOfAllCompletedTaskForDate(date, email).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer == null) {
                    completed_todo_task_for_date = 0;
                } else {
                    completed_todo_task_for_date = integer;
                }
                //    Log.d("Todo_count_completed", String.valueOf(completed_todo_task_for_date));
                setCompletionProgressBar();
                updateResult();
            }
        });

        ViewTreeObserver vto = progress_max.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                progress_max.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                max_width = progress_max.getMeasuredWidth();

                setCompletionProgressBar();

            }
        });
    }

    void setCompletionProgressBar() {
        ViewGroup.LayoutParams params = todo_progress_tv.getLayoutParams();
        String completion;
        if (total_todo_task_for_date != 0) {
            percent_complete = (completed_todo_task_for_date * 1.0) / total_todo_task_for_date;
            if (percent_complete != 0.0) {
                completion = decimalFormat.format(percent_complete * 100) + "%";
                params.width = (int) (percent_complete * max_width);
            } else {
                completion = "0%";
                params.width = 50;
            }
        } else {
            percent_complete = 0;
            completion = "0%";
            params.width = 50;
        }

        todo_progress_tv.setLayoutParams(params);
        todo_progress_tv.setText(completion);
    }

    private void updateTable() {
        String val1, val2, val3, val4;
        if (totalQValues != 0) {
            val1 = decimalFormat.format((Q1size * 100) / totalQValues) + "%";
            val2 = decimalFormat.format((Q2size * 100) / totalQValues) + "%";
            val3 = decimalFormat.format((Q3size * 100) / totalQValues) + "%";
            val4 = decimalFormat.format((Q4size * 100) / totalQValues) + "%";
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
        if (total_todo_task_for_date != 0) {
            if (percent_complete >= 0.8) {
                result.append("Excellent: You are good in planning your day and execution");
            } else if (percent_complete >= 0.5) {
                result.append("Good: Plan is fine; execution to be improved");
            } else {
                result.append("Improvement Needed: Probably you are planning too many tasks for the day.");
            }
        } else {
            result.append("No data for this date");
        }
        result_todo_progress_textview.setText(result.toString());

        result = new StringBuilder();

        if (totalQValues != 0) {
            if (Q2size > 0.3) {
                result.append("Excellent: You are managing time well and pursuing your goals effectively.\n");
            } else if (Q2size > 0.2) {
                result.append("Good: You are thinking long term and devoting time to planning, organisational development, improvement and self development.\n");
            } else {
                result.append("Not good:You are a crisis manager. As you are not spending enough time on important tasks, they become urgent and disturb your time plan.\n");
            }

            if (Q3size > 0.35) {
                result.append("Serious problem: You will be running all over the place and goals will still not be achieved.\n");
            } else if (Q3size > 0.15) {
                result.append("Caution: Need to be assertive, learn to say 'No' and delegate.\n");
            } else {
                result.append("Excellent control over urgent but not important matters.\n");
            }

            if (Q4size > 0.1) {
                result.append("Serious problem: Need to closely examine the trivial time wasters, and eliminate or minimise them.");
            } else if (Q4size > 0.05) {
                result.append("Caution: You are spending too much time on trivia.");
            } else {
                result.append("Excellent control over trivial time wasters.");
            }

            result_matrix_textview.setText(result.toString());
        } else {
            result.append("No daily activity data for this date");
            result_matrix_textview.setText((result.toString()));
        }
    }

    private String findRandomTip() {
        Calendar current = Calendar.getInstance();
        current.set(Calendar.HOUR_OF_DAY, 0);
        current.set(Calendar.MINUTE, 0);
        current.set(Calendar.SECOND, 0);
        current.set(Calendar.MILLISECOND, 0);
        Calendar tipDate = Calendar.getInstance();
        long temp = mPreferences.getLong("tipdate", -1);
        tipDate.setTimeInMillis(temp);
        if (temp == -1 || current.after(tipDate)) {
            Random random = new Random();
            String arr[] = getResources().getStringArray(R.array.tips);
            int tipNumber = random.nextInt(arr.length);
            dailytip = new StringBuilder();
            dailytip.append(arr[tipNumber]);
            editor.putLong("tipdate", current.getTimeInMillis());
            editor.putString("tip", dailytip.toString());
            editor.commit();
            //Log.d("tip", dailytip.toString());
        } else {
            dailytip = new StringBuilder();
            dailytip.append(mPreferences.getString("tip", null));
            //Log.d("tip", dailytip.toString());
        }
        return dailytip.toString();
    }

    private void showOverLay() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.activity_help_overlay);
        RelativeLayout layout = (RelativeLayout) dialog.findViewById(R.id.overlayLayout);
        FloatingActionButton fab = (FloatingActionButton) dialog.findViewById(R.id.fab);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.imageView1);
        Glide.with(this).load(R.drawable.metrics_help).into(imageView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
