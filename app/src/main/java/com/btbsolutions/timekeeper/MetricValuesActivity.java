package com.btbsolutions.timekeeper;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.btbsolutions.timekeeper.adapter.MetricQAdapter;
import com.btbsolutions.timekeeper.utility.ToDoViewModel;

import java.util.Calendar;

public class MetricValuesActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.btbsolutions.timekeeper.sharedpreffile";

    private MetricQAdapter adapter;
    private ToDoViewModel mToDoViewModel;

    private TextView no_entry_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mertic_values);

        final RecyclerView recyclerView = findViewById(R.id.recyclerview_metric_tasks);
        adapter = new MetricQAdapter(MetricValuesActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        no_entry_textview = findViewById(R.id.no_entry_textview);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        Calendar c = Calendar.getInstance();
        final int curr_year = c.get(Calendar.YEAR);
        int curr_month = c.get(Calendar.MONTH);
        int curr_dayofmonth = c.get(Calendar.DAY_OF_MONTH);

        String tempdate = curr_dayofmonth < 10 ? "0" + curr_dayofmonth : "" + curr_dayofmonth;
        String tempmonth = curr_month < 9 ? "0" + (curr_month + 1) : "" + (curr_month + 1);
        String currentdate = Integer.toString(curr_year) + "-" + tempmonth + "-" + tempdate;

        String email = mPreferences.getString("email", null);

        mToDoViewModel = ViewModelProviders.of(this).get(ToDoViewModel.class);

        Intent intent = getIntent();
        String metricType = intent.getStringExtra("metricValue");

        switch (metricType) {
            case "Q1":
                mToDoViewModel.getAllQ1Tasks(currentdate, email).observe(this, new Observer<Double>() {
                    @Override
                    public void onChanged(@Nullable Double integers) {
                        if (integers!=null && integers != 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            no_entry_textview.setVisibility(View.GONE);
                            //adapter.setTaskList(integers);
                            adapter.notifyDataSetChanged();
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            no_entry_textview.setVisibility(View.VISIBLE);
                            no_entry_textview.setText(getString(R.string.no_Q1_entry));
                        }
                    }
                });
                break;
            case "Q2":
                mToDoViewModel.getAllQ2Tasks(currentdate, email).observe(this, new Observer<Double>() {
                    @Override
                    public void onChanged(@Nullable Double integers) {
                        if (integers != null && integers != 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            no_entry_textview.setVisibility(View.GONE);
                            //adapter.setTaskList(integers);
                            adapter.notifyDataSetChanged();
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            no_entry_textview.setVisibility(View.VISIBLE);
                            no_entry_textview.setText(getString(R.string.no_Q2_entry));
                        }
                    }
                });
                break;
            case "Q3":
                mToDoViewModel.getAllQ3Tasks(currentdate, email).observe(this, new Observer<Double>() {
                    @Override
                    public void onChanged(@Nullable Double integers) {

                        if (integers != null && integers != 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            no_entry_textview.setVisibility(View.GONE);
                            //adapter.setTaskList(integers);
                            adapter.notifyDataSetChanged();
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            no_entry_textview.setVisibility(View.VISIBLE);
                            no_entry_textview.setText(getString(R.string.no_Q3_entry));
                        }
                    }
                });
                break;
            case "Q4":
                mToDoViewModel.getAllQ4Tasks(currentdate, email).observe(this, new Observer<Double>() {
                    @Override
                    public void onChanged(@Nullable Double integers) {

                        if (integers != null && integers != 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            no_entry_textview.setVisibility(View.GONE);
                            //adapter.setTaskList(integers);
                            adapter.notifyDataSetChanged();
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            no_entry_textview.setVisibility(View.VISIBLE);
                            no_entry_textview.setText(getString(R.string.no_Q4_entry));
                        }
                    }
                });
                break;
        }
    }
}
