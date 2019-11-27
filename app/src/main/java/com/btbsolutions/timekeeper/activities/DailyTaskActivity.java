package com.btbsolutions.timekeeper.activities;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btbsolutions.timekeeper.AddDailyTask;
import com.btbsolutions.timekeeper.AdvertisementActivity;
import com.btbsolutions.timekeeper.adapter.DailyTaskAdapter;
import com.btbsolutions.timekeeper.R;
import com.btbsolutions.timekeeper.utility.DailyTasks;
import com.btbsolutions.timekeeper.utility.Helpers;
import com.btbsolutions.timekeeper.utility.ToDoViewModel;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class DailyTaskActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DailyTaskAdapter adapter;

    private ToDoViewModel mToDoViewModel;

    private View no_item_layout;
    private View container_layout;

    TextView nav_bar_username, nav_bar_email;

    private ImageView mAdView;
    private int adNumber;
    private Thread thread;
    private boolean thread_continue = true;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addtaskIntent = new Intent(DailyTaskActivity.this, AddDailyTask.class);
                addtaskIntent.putExtra("ActivityName", "DailyTask");
                startActivity(addtaskIntent);
                finish();
            }
        });

        String sharedPrefFile = "com.btbsolutions.timekeeper.sharedpreffile";
        SharedPreferences mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        String email = mPreferences.getString("email", null);
        String user = mPreferences.getString("username", null);

        mToDoViewModel = ViewModelProviders.of(this).get(ToDoViewModel.class);

        no_item_layout = findViewById(R.id.no_daily_task_item_layout);
        container_layout = findViewById(R.id.container_daily_task_layout);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new DailyTaskAdapter(DailyTaskActivity.this, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Calendar currentCalendarDate = Calendar.getInstance();
        currentCalendarDate.set(Calendar.HOUR_OF_DAY, 0);
        currentCalendarDate.set(Calendar.MINUTE, 0);
        currentCalendarDate.set(Calendar.SECOND, 0);

        String currentdate = simpleDateFormat.format(new Date(currentCalendarDate.getTimeInMillis()));

        fillListView(currentdate, email);

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
                                Glide.with(DailyTaskActivity.this)
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
            Intent intent = new Intent(DailyTaskActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if(id == R.id.action_help){
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

        if (id == R.id.nav_metric_activity){
            Intent intent = new Intent(DailyTaskActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_daily_activity) {
            onBackPressed();
        } else if (id == R.id.nav_calendar) {
            Intent intent = new Intent(DailyTaskActivity.this, CalendarActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(DailyTaskActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
        } else if(id == R.id.nav_weekly_metric_activity){
            Intent intent = new Intent(DailyTaskActivity.this, WeeklyMetricActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void fillListView(final String date, String useremail){

        mToDoViewModel.getAllTasks(date, useremail).observe(this, new Observer<List<DailyTasks>>() {
            @Override
            public void onChanged(@Nullable List<DailyTasks> dailyTasks) {
                if(dailyTasks !=null && dailyTasks.size() != 0) {
                    no_item_layout.setVisibility(View.GONE);
                    container_layout.setVisibility(View.VISIBLE);
                   // Log.d("timestampIsFromDate ", date);
                    //mTodoList = dailyTasks;
                    adapter.setItems(dailyTasks);
                    //datetext.setText(date);
                }
                else{
                    no_item_layout.setVisibility(View.VISIBLE);
                    container_layout.setVisibility(View.GONE);
                    //datetext.setText(date);
                }
            }
        });

        /*mToDoViewModel.getAllItemsForEndDate(date).observe(this, new Observer<List<ToDo>>() {
            @Override
            public void onChanged(List<ToDo> toDos) {
                if(toDos.size() != 0) {
                    no_item_layout.setVisibility(View.GONE);
                    container_layout.setVisibility(View.VISIBLE);
                    Log.d("timestampIsFromDate ", date);
                    mTodoList = toDos;
                    adapter.setToDoItems(mTodoList);
                    datetext.setText(date);
                }
                else{
                    no_item_layout.setVisibility(View.VISIBLE);
                    container_layout.setVisibility(View.GONE);
                    datetext.setText(date);
                }
            }
        });*/
    }

    private void showOverLay(){
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.activity_help_overlay);
        RelativeLayout layout = (RelativeLayout) dialog.findViewById(R.id.overlayLayout);
        FloatingActionButton fab = (FloatingActionButton) dialog.findViewById(R.id.fab);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.imageView1);
        Glide.with(this).load(R.drawable.dailytask_help).into(imageView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
