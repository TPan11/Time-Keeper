package com.btbsolutions.timekeeper.activities;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.btbsolutions.timekeeper.AddNewItem;
import com.btbsolutions.timekeeper.AdvertisementActivity;
import com.btbsolutions.timekeeper.R;
import com.btbsolutions.timekeeper.adapter.ToDoListAdapter;
import com.btbsolutions.timekeeper.utility.HeaderItem;
import com.btbsolutions.timekeeper.utility.Helpers;
import com.btbsolutions.timekeeper.utility.ListItem;
import com.btbsolutions.timekeeper.utility.ToDo;
import com.btbsolutions.timekeeper.utility.ToDoItem;
import com.btbsolutions.timekeeper.utility.ToDoViewModel;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class CalendarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int ADD_NEW_ITEM_ACTIVITY_REQUEST_CODE = 1;
    public String selectedDate;
    private ToDoViewModel mToDoViewModel;
    private ToDoListAdapter adapter;
    private TextView datetext;
    private FloatingActionButton editbutton;
    private View no_item_layout;
    private View container_layout;

    private boolean check_ScrollingUp;

    List<ListItem> mTodoList;

    private String useremail;
    TextView nav_bar_username, nav_bar_email;

    SharedPreferences.Editor editor;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private Calendar currentCalendarDate;

    private ImageView mAdView;
    private int adNumber;
    private Thread thread;
    private boolean thread_continue = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        String sharedPrefFile = "com.btbsolutions.timekeeper.sharedpreffile";
        SharedPreferences mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        editor = mPreferences.edit();

        mTodoList = new ArrayList<>();

        useremail = mPreferences.getString("email", null);
        String user = mPreferences.getString("username", null);

        mToDoViewModel = ViewModelProviders.of(this).get(ToDoViewModel.class);

        CalendarView calendar = findViewById(R.id.calendarView);
        datetext = findViewById(R.id.dateText);
        editbutton = findViewById(R.id.New);

        no_item_layout = findViewById(R.id.no_item_layout);
        container_layout = findViewById(R.id.container_layout);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new ToDoListAdapter(CalendarActivity.this, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        currentCalendarDate = Calendar.getInstance();
        currentCalendarDate.set(Calendar.HOUR_OF_DAY, 0);
        currentCalendarDate.set(Calendar.MINUTE, 0);
        currentCalendarDate.set(Calendar.SECOND, 0);
        currentCalendarDate.set(Calendar.MILLISECOND, 0);

        String currentdate = simpleDateFormat.format(new Date(currentCalendarDate.getTimeInMillis()));

        fillListView(currentdate);

        selectedDate = currentdate;

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(year, month, dayOfMonth, 0, 0, 0);
                selectedDate = simpleDateFormat.format(new Date(c.getTimeInMillis()));

                if (c.getTimeInMillis() < currentCalendarDate.getTimeInMillis()) {
                    editbutton.setEnabled(false);
                    editbutton.setBackgroundTintList(null);
                } else {
                    editbutton.setEnabled(true);
                    editbutton.setBackgroundTintList(getResources().getColorStateList(R.color.edit_button_color_list));
                }

                fillListView(selectedDate);
            }
        });

        editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, AddNewItem.class);
                intent.putExtra("date", selectedDate);
                startActivityForResult(intent, ADD_NEW_ITEM_ACTIVITY_REQUEST_CODE);
            }
        });


        //registerForContextMenu(recyclerView);
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
                                Glide.with(CalendarActivity.this)
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
            Intent intent = new Intent(CalendarActivity.this, SettingsActivity.class);
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

        if (id == R.id.nav_metric_activity) {
            Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_daily_activity) {
            Intent intent = new Intent(CalendarActivity.this, DailyTaskActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_calendar) {
            onBackPressed();
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(CalendarActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
        } else if(id == R.id.nav_weekly_metric_activity){
            Intent intent = new Intent(CalendarActivity.this, WeeklyMetricActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void fillListView(final String date) {
        mToDoViewModel.getAllItemsForEndDate(date, useremail).observe(this, new Observer<List<ToDo>>() {
            @Override
            public void onChanged(List<ToDo> toDos) {
                if (toDos.size() != 0) {
                    no_item_layout.setVisibility(View.GONE);
                    container_layout.setVisibility(View.VISIBLE);
                    //Log.d("timestampIsFromDate ", date);

                    mTodoList.clear();

                    Map<Double, List<ToDo>> todos = toMap(toDos);
                    for (Double duration : todos.keySet()) {
                        Log.d("Durations", String.valueOf(duration));
                        HeaderItem header = new HeaderItem(duration);
                        mTodoList.add(header);
                        for (ToDo toDo : todos.get(duration)) {
                            ToDoItem item = new ToDoItem(toDo);
                            mTodoList.add(item);
                        }
                    }

                    adapter.setToDoItems(mTodoList);
                    datetext.setText(date);
                } else {
                    no_item_layout.setVisibility(View.VISIBLE);
                    container_layout.setVisibility(View.GONE);
                    datetext.setText(date);
                }
            }

            private Map<Double, List<ToDo>> toMap(@NonNull List<ToDo> toDos) {
                Map<Double, List<ToDo>> map = new TreeMap<>();
                for (ToDo toDo : toDos) {
                    List<ToDo> value = map.get(toDo.getDuration());
                    if (value == null) {
                        value = new ArrayList<>();
                        map.put(toDo.getDuration(), value);
                    }
                    value.add(toDo);
                }
                return map;
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_NEW_ITEM_ACTIVITY_REQUEST_CODE) {
                String word = data.getStringExtra("Item");
                String priority = data.getStringExtra("Priority");
                String date = data.getStringExtra("End Date");
                String detail = data.getStringExtra("Details");
                double duration = data.getDoubleExtra("Duration", 0.0d);

                //   Log.d("returnedDate", date);
                ToDo todoItem = new ToDo(word, date, Integer.parseInt(priority),
                        detail, duration, 0, false, useremail);
                mToDoViewModel.insert(todoItem);
                adapter.notifyDataSetChanged();
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == ADD_NEW_ITEM_ACTIVITY_REQUEST_CODE) {
                Toast.makeText(getApplicationContext(), "Please enter data", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showOverLay(){
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.activity_help_overlay);
        RelativeLayout layout = (RelativeLayout) dialog.findViewById(R.id.overlayLayout);
        FloatingActionButton fab = (FloatingActionButton) dialog.findViewById(R.id.fab);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.imageView1);
        Glide.with(this).load(R.drawable.calendar_help).into(imageView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
