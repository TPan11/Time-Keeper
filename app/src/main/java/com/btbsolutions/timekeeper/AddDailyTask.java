package com.btbsolutions.timekeeper;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.btbsolutions.timekeeper.activities.DailyTaskActivity;
import com.btbsolutions.timekeeper.utility.DailyTasks;
import com.btbsolutions.timekeeper.utility.Helpers;
import com.btbsolutions.timekeeper.utility.ToDo;
import com.btbsolutions.timekeeper.utility.ToDoRepository;
import com.btbsolutions.timekeeper.utility.ToDoViewModel;
import com.btbsolutions.timekeeper.activities.CalendarActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddDailyTask extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener {

    /*
     *Declare all local variables
     */
    private String start_time, end_time, st_hrs, st_mins, st_am_pm, end_hrs, end_mins, end_am_pm, date;
    private ToDoRepository toDoRepository;
    /*
     *Declare all Views
     */
    private ToDoViewModel mToDoViewModel;
    private AutoCompleteTextView mAutoCompleteTask;
    private TextView addtaskdate_tv, taskduration_tv;
    private Spinner st_hour_spinner, st_min_spinner, st_am_pm_spinner, end_hour_spinner, end_min_spinner, end_am_pm_spinner, metricQ_spinner;
    private ImageButton btn_get_todos;
    /*
     *Get all the tasks for autocomplete view in a List object
     */
    private List<String> tasks = Helpers.getAllTasks();

    private long taskid;
    private Intent receiveIntent;

    private Dialog dialog;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm", Locale.US);
    SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a", Locale.US);

    double dur = 0.0d;
    long diff;
    private boolean isNew = true;

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.btbsolutions.timekeeper.sharedpreffile";
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily_task);

        //Initialize AutoCompleteView and set its adapter
        mAutoCompleteTask = (AutoCompleteTextView) findViewById(R.id.edit_Task);
        addtaskdate_tv = findViewById(R.id.addtaskdate_tv);
        taskduration_tv = findViewById(R.id.taskduration_tv);
        btn_get_todos = findViewById(R.id.btn_get_todos);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, tasks);

        diff = 0;
        mAutoCompleteTask.setAdapter(adapter);

        /*
         *Initialize and declare all the Spinner objects to get the start and end time
         */
        st_hour_spinner = findViewById(R.id.spinner_start_hours);
        ArrayAdapter<CharSequence> st_hour_adapter = ArrayAdapter.createFromResource(this,
                R.array.hours, android.R.layout.simple_spinner_dropdown_item);

        st_min_spinner = findViewById(R.id.spinner_start_mins);
        ArrayAdapter<CharSequence> st_min_adapter = ArrayAdapter.createFromResource(this,
                R.array.mins, android.R.layout.simple_spinner_dropdown_item);

        st_am_pm_spinner = findViewById(R.id.spinner_start_am_pm);
        ArrayAdapter<CharSequence> st_am_pm_adapter = ArrayAdapter.createFromResource(this,
                R.array.am_pm, android.R.layout.simple_spinner_dropdown_item);


        end_hour_spinner = findViewById(R.id.spinner_end_hours);
        ArrayAdapter<CharSequence> end_hour_adapter = ArrayAdapter.createFromResource(this,
                R.array.hours, android.R.layout.simple_spinner_dropdown_item);

        end_min_spinner = findViewById(R.id.spinner_end_mins);
        ArrayAdapter<CharSequence> end_min_adapter = ArrayAdapter.createFromResource(this,
                R.array.mins, android.R.layout.simple_spinner_dropdown_item);

        end_am_pm_spinner = findViewById(R.id.spinner_end_am_pm);
        ArrayAdapter<CharSequence> end_am_pm_adapter = ArrayAdapter.createFromResource(this,
                R.array.am_pm, android.R.layout.simple_spinner_dropdown_item);

        metricQ_spinner = findViewById(R.id.metricQ_spinner);
        ArrayAdapter<CharSequence> metricQ_adapter = ArrayAdapter.createFromResource(this,
                R.array.Q_metrics, android.R.layout.simple_spinner_dropdown_item);

        /*
         *Set all the Spinner adapters
         */
        if (st_hour_spinner != null) {
            st_hour_spinner.setAdapter(st_hour_adapter);
            st_hour_spinner.setOnItemSelectedListener(this);
        }
        if (st_min_spinner != null) {
            st_min_spinner.setAdapter(st_min_adapter);
            st_min_spinner.setOnItemSelectedListener(this);
        }
        if (st_am_pm_spinner != null) {
            st_am_pm_spinner.setAdapter(st_am_pm_adapter);
            st_am_pm_spinner.setOnItemSelectedListener(this);
        }

        if (end_hour_spinner != null) {
            end_hour_spinner.setAdapter(end_hour_adapter);
            end_hour_spinner.setOnItemSelectedListener(this);
        }
        if (end_min_spinner != null) {
            end_min_spinner.setAdapter(end_min_adapter);
            end_min_spinner.setOnItemSelectedListener(this);
        }
        if (end_am_pm_spinner != null) {
            end_am_pm_spinner.setAdapter(end_am_pm_adapter);
            end_am_pm_spinner.setOnItemSelectedListener(this);
        }

        if (metricQ_spinner != null) {
            metricQ_spinner.setAdapter(metricQ_adapter);
            metricQ_spinner.setOnItemSelectedListener(this);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar cal = Calendar.getInstance();
        date = sdf.format(cal.getTime());
        addtaskdate_tv.setText(date);

        taskduration_tv.setText("0.0");

        receiveIntent = getIntent();

        if (receiveIntent.getStringExtra("ActivityName").equals("CalendarLongClick")) {
            mAutoCompleteTask.setText(receiveIntent.getStringExtra("taskname"));
            taskid = receiveIntent.getLongExtra("taskid", 0);

            mAutoCompleteTask.setEnabled(false);

        }

        /*
         *Set ViewModel to access room database
         */
        mToDoViewModel = ViewModelProviders.of(this).get(ToDoViewModel.class);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_new_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_check) {
            String taskname = mAutoCompleteTask.getText().toString();
            String metric = metricQ_spinner.getSelectedItem().toString();
            String useremail = mPreferences.getString("email", null);
            String st_time = null;
            String ed_time = null;
            try {
                st_time = displayFormat.format(parseFormat.parse(start_time));
                ed_time = displayFormat.format(parseFormat.parse(end_time));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(!metric.isEmpty()) {
                if (!taskname.isEmpty()) {
                    if (diff > 0) {
                        long diffSeconds = diff / 1000 % 60;
                        long diffMinutes = diff / (60 * 1000) % 60;
                        long diffHours = diff / (60 * 60 * 1000);

                        dur = (double) diffHours + (double) (diffMinutes / 60.0d);

                        DailyTasks dt = new DailyTasks(taskname, date, st_time, ed_time, metric, dur, 0, false, useremail);
                        mToDoViewModel.insert(dt);

                        if (receiveIntent.getStringExtra("ActivityName").equals("CalendarLongClick")) {
                            mToDoViewModel.getItemById(taskid).observe(this, new Observer<ToDo>() {
                                @Override
                                public void onChanged(@Nullable ToDo toDo) {
                                    toDo.setComplete(1);
                                    toDo.setBackup(false);
                                    mToDoViewModel.update(toDo);
                                }
                            });
                            Intent goback = new Intent(this, CalendarActivity.class);
                            startActivity(goback);
                            finish();
                        }
                        else{
                            if(!isNew){
                                mToDoViewModel.getItemById(taskid).observe(this, new Observer<ToDo>() {
                                    @Override
                                    public void onChanged(@Nullable ToDo toDo) {
                                        toDo.setComplete(1);
                                        toDo.setBackup(false);
                                        mToDoViewModel.update(toDo);
                                    }
                                });
                            }
                            Intent goback = new Intent(this, DailyTaskActivity.class);
                            startActivity(goback);
                            finish();
                        }
                    } else {
                        Toast.makeText(this, "Please choose an end time after the start time", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Please Enter the task Name", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "Please choose a metric", Toast.LENGTH_SHORT).show();
            }

            return true;
        } else if (id == R.id.action_cancel) {
            if (receiveIntent.getStringExtra("ActivityName").equals("CalendarLongClick")) {
                Intent goback = new Intent(this, CalendarActivity.class);
                startActivity(goback);
                finish();
            } else {
                Intent goback = new Intent(this, DailyTaskActivity.class);
                startActivity(goback);
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int spinner_id = parent.getId();
        switch (spinner_id) {
            case R.id.spinner_start_hours:
                String tempstart = parent.getItemAtPosition(position).toString();
                if(tempstart.equals("12")){
                    st_hrs = "00";
                } else {
                    st_hrs = tempstart;
                }
                break;
            case R.id.spinner_start_mins:
                st_mins = parent.getItemAtPosition(position).toString();
                break;
            case R.id.spinner_start_am_pm:
                st_am_pm = parent.getItemAtPosition(position).toString();
                break;

            case R.id.spinner_end_hours:
                String tempend = parent.getItemAtPosition(position).toString();
                if(tempend.equals("12")){
                    end_hrs = "00";
                } else {
                    end_hrs = tempend;
                }
                break;
            case R.id.spinner_end_mins:
                end_mins = parent.getItemAtPosition(position).toString();
                break;
            case R.id.spinner_end_am_pm:
                end_am_pm = parent.getItemAtPosition(position).toString();
                break;
        }

        start_time = st_hrs + ":" + st_mins + " " + st_am_pm;
        end_time = end_hrs + ":" + end_mins + " " + end_am_pm;
        try {
            Date st_date = parseFormat.parse(start_time);
            Date end_date = parseFormat.parse(end_time);

            diff = end_date.getTime() - st_date.getTime();
            if (diff >= 0) {
                long diffSeconds = diff / 1000 % 60;
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000);

                dur = (double) diffHours + (double) (diffMinutes / 60.0d);
                taskduration_tv.setText(String.valueOf(dur));
            } else {
                taskduration_tv.setText(String.valueOf(0.0d));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onAllTodoTasksForDate(View view) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setTitle(R.string.dialog_title);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.show();

        RecyclerView rv_all_todo_tasks = dialog.findViewById(R.id.rv_all_todo_tasks);
        rv_all_todo_tasks.setLayoutManager(new LinearLayoutManager(this));
        final TodoDialogAdapter todoDialogAdapter = new TodoDialogAdapter();
        rv_all_todo_tasks.setAdapter(todoDialogAdapter);

        final LinearLayout ll_no_item = dialog.findViewById(R.id.ll_no_item);

        Calendar cal = Calendar.getInstance();
        String currentdate = simpleDateFormat.format(new Date(cal.getTimeInMillis()));

        String useremail = mPreferences.getString("email", null);;

        mToDoViewModel.getIncompleteTodoTask(currentdate, useremail).observe(this, new Observer<List<ToDo>>() {
            @Override
            public void onChanged(@Nullable List<ToDo> toDos) {
                if(toDos == null || toDos.size()==0){
                    ll_no_item.setVisibility(View.VISIBLE);
                }
                else{
                    todoDialogAdapter.setList(toDos);
                    ll_no_item.setVisibility(View.GONE);
                }
            }
        });

    }

    class TodoDialogAdapter extends RecyclerView.Adapter<TodoDialogAdapter.TodoViewHolder> {

        List<ToDo> mTodoList;

        TodoDialogAdapter(){
            mTodoList = new ArrayList<>();
        }

        @NonNull
        @Override
        public TodoDialogAdapter.TodoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater
                    .from(viewGroup.getContext())
                    .inflate(R.layout.recyclerview_todo_dialog, viewGroup, false);
            return new TodoViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull TodoViewHolder viewHolder, int i) {
            ToDo current = mTodoList.get(i);
            viewHolder.tv_todo_task_name.setText(current.getTodotask());
        }

        @Override
        public int getItemCount() {
            if(mTodoList!=null && mTodoList.size()!=0){
                return mTodoList.size();
            }
            return 0;
        }

        public void setList(List<ToDo> toDos)
        {
            mTodoList = toDos;
            notifyDataSetChanged();
        }

        class TodoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView tv_todo_task_name;

            public TodoViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_todo_task_name = itemView.findViewById(R.id.tv_todo_task_name);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                ToDo current = mTodoList.get(getAdapterPosition());

                mAutoCompleteTask.setText(current.getTodotask());
                taskid = current.getId();

                mAutoCompleteTask.setEnabled(false);
                isNew = false;
                dialog.dismiss();
            }
        }
    }
}
