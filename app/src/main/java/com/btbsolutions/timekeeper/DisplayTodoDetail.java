package com.btbsolutions.timekeeper;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.btbsolutions.timekeeper.utility.Helpers;
import com.btbsolutions.timekeeper.utility.ToDo;
import com.btbsolutions.timekeeper.utility.ToDoViewModel;

import java.sql.Timestamp;
import java.util.Calendar;

public class DisplayTodoDetail extends AppCompatActivity {

    public static final int UPDATE_REQUEST = 2;

    private TextView items, priority, detail, date, complete, tv_duration_group;
    private ScrollView sv;
    private long Id;

    private ToDoViewModel ToDoVM;

    private Helpers helper;

    private String useremail;
    private double duration;
    private String[] group;

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.btbsolutions.timekeeper.sharedpreffile";
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_todo_detail);

        helper = new Helpers();

        ToDoVM = ViewModelProviders.of(this).get(ToDoViewModel.class);
        items = findViewById(R.id.textView_Item);
        priority = findViewById(R.id.textView_Priority);
        date = findViewById(R.id.textView_enddate);
        detail = findViewById(R.id.textView_details);
        sv = findViewById(R.id.display_scroll_view);
        complete = findViewById(R.id.textview_complete);
        tv_duration_group = findViewById(R.id.tv_duration_group);

        Intent intent = getIntent();

        String intentDetails = intent.getStringExtra("Details");

        Id = Long.parseLong(intent.getStringExtra("Id"));
        int p = Integer.parseInt(intent.getStringExtra("Priority"));
        items.setText(intent.getStringExtra("Item"));
        priority.setText(intent.getStringExtra("Priority"));
        date.setText(intent.getStringExtra("End_date"));
        if(intentDetails==null || intentDetails.isEmpty()){
            detail.setVisibility(View.GONE);
        }
        else {
            detail.setVisibility(View.VISIBLE);
            detail.setText(intentDetails);
        }
        complete.setText(intent.getStringExtra("Complete"));
        duration = intent.getDoubleExtra("Duration", 0.0d);
        group = getResources().getStringArray(R.array.duration_group);
        tv_duration_group.setText(group[(int)duration]);

        switch (p){
            case 1:
                sv.setBackgroundColor(getResources().getColor(R.color.display_priority1));
                break;
            case 2:
                sv.setBackgroundColor(getResources().getColor(R.color.display_priority2));
                break;
            case 3:
                sv.setBackgroundColor(getResources().getColor(R.color.display_priority3));
                break;
        }

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        editor = mPreferences.edit();

        useremail = mPreferences.getString("email", null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {

            Intent updateintent = new Intent(this, UpdateToDoItem.class);

            updateintent.putExtra("item", items.getText().toString());
            updateintent.putExtra("priority", priority.getText().toString());
            updateintent.putExtra("date", date.getText().toString());
            updateintent.putExtra("details", detail.getText().toString());
            updateintent.putExtra("duration", duration);

            startActivityForResult(updateintent, UPDATE_REQUEST);
        }

        return super.onOptionsItemSelected(item);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_REQUEST && resultCode == RESULT_OK) {
            String word = data.getStringExtra("Item");
            String priorities = data.getStringExtra("Priority");
            String endDate = data.getStringExtra("End Date");
            String detailnotes = data.getStringExtra("Details");
            double duration = data.getDoubleExtra("Duration", 0.0d);

            //Log.d("EndDateFromUpdate", endDate);
            priority.setText(priorities);
            date.setText(endDate);
            detail.setText(detailnotes);

            switch (Integer.parseInt(priorities)){
                case 1:
                    sv.setBackgroundColor(getResources().getColor(R.color.display_priority1));
                    break;
                case 2:
                    sv.setBackgroundColor(getResources().getColor(R.color.display_priority2));
                    break;
                case 3:
                    sv.setBackgroundColor(getResources().getColor(R.color.display_priority3));
                    break;
            }

            ToDo todoItem = new ToDo(word, endDate, Integer.parseInt(priorities), detailnotes,
                    duration, 0, false, useremail);
            todoItem.setId(Id);
            ToDoVM.update(todoItem);

            priority.setText(priorities);
            date.setText(endDate);
            if (detailnotes==null || detailnotes.isEmpty()){
                detail.setVisibility(View.GONE);
            }
            else {
                detail.setVisibility(View.VISIBLE);
                detail.setText(detailnotes);
            }
            tv_duration_group.setText(group[(int)duration]);

            Toast.makeText(
                    getApplicationContext(),
                    "Update Complete",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Update Canceled",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    String timestampToString(long timestamp, int mode){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        String str, day, month, year;

        if(mode == 0){
            year = Integer.toString(cal.get(Calendar.YEAR));
            month = cal.get(Calendar.MONTH)<9?"0"+(cal.get(Calendar.MONTH)+1):""+cal.get(Calendar.MONTH)+1;
            day = cal.get(Calendar.DAY_OF_MONTH)<10?"0"+cal.get(Calendar.DAY_OF_MONTH):""+cal.get(Calendar.DAY_OF_MONTH);
            str = year+"-"+month+"-"+day;
        }
        else{
            year = Integer.toString(cal.get(Calendar.YEAR));
            month = cal.get(Calendar.MONTH)<9?"0"+(cal.get(Calendar.MONTH)+1):""+cal.get(Calendar.MONTH)+1;
            day = cal.get(Calendar.DAY_OF_MONTH)<10?"0"+cal.get(Calendar.DAY_OF_MONTH):""+cal.get(Calendar.DAY_OF_MONTH);

            String hour = cal.get(Calendar.HOUR)<10?"0"+cal.get(Calendar.HOUR):""+cal.get(Calendar.HOUR);
            String min = cal.get(Calendar.MINUTE)<10?"0"+cal.get(Calendar.MINUTE):""+cal.get(Calendar.MINUTE);
            String sec = cal.get(Calendar.SECOND)<10?"0"+cal.get(Calendar.SECOND):""+cal.get(Calendar.SECOND);
            str = year+"-"+month+"-"+day+" "+hour+":"+min+":"+sec;
        }


        return str;
    }

    Long stringtoTimestamp(String date){
        Timestamp timestamp = Timestamp.valueOf(date);
        return timestamp.getTime();
    }
}
