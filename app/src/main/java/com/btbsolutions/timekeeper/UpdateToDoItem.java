package com.btbsolutions.timekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.btbsolutions.timekeeper.utility.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class UpdateToDoItem extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String dateMessage;

    private AutoCompleteTextView mEditToDoItemView;
    private TextView mTextViewDatePicker;
    private EditText mEditTextDetailNote;
    private int itemPriority;
    private RadioButton radiob;
    private Spinner spin_duration_group;
    private double duration;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_todo_item);

        mEditToDoItemView = findViewById(R.id.edit_todoItem);
        mTextViewDatePicker = findViewById(R.id.finaldate);
        mEditTextDetailNote = findViewById(R.id.detailnote);
        spin_duration_group = findViewById(R.id.spin_duration_group);

        /*actv = findViewById(R.id.actv);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);

        actv.setAdapter(adapter);*/

        Intent intent = getIntent();

        mEditToDoItemView.setText(intent.getStringExtra("item"));
        mTextViewDatePicker.setText(intent.getStringExtra("date"));
        mEditTextDetailNote.setText(intent.getStringExtra("details"));
        itemPriority = Integer.parseInt(intent.getStringExtra("priority"));
        duration = intent.getDoubleExtra("duration", 0.0d);
        spin_duration_group.setSelection((int)duration);

        spin_duration_group.setOnItemSelectedListener(this);

        if(itemPriority==1){
            radiob= findViewById(R.id.priority1);
            radiob.setChecked(true);
        }
        else if(itemPriority == 2){
            radiob= findViewById(R.id.priority2);
            radiob.setChecked(true);
        }
        else if (itemPriority == 3){
            radiob= findViewById(R.id.priority3);
            radiob.setChecked(true);
        }
    }

    public void onRadioButtonClick(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()){
            case R.id.priority1:
                if(checked)
                    itemPriority = 1;
                break;
            case R.id.priority2:
                if(checked)
                    itemPriority = 2;
                break;
            case R.id.priority3:
                if(checked)
                    itemPriority = 3;
                break;
            default:
                itemPriority = 0;
        }
    }

    public void showDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        ((DatePickerFragment) newFragment).setChoice(0);
        newFragment.show(getSupportFragmentManager(),getString(R.string.datepicker));
    }

    public void processDatePickerResult(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, 0,0,0);
        dateMessage = simpleDateFormat.format(new Date(c.getTimeInMillis()));
        mTextViewDatePicker.setText(dateMessage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.update_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent replyIntent = new Intent();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_update) {
            String word = mEditToDoItemView.getText().toString();
            String date = mTextViewDatePicker.getText().toString();
            String details = mEditTextDetailNote.getText().toString();

            replyIntent.putExtra("Item", word);
            replyIntent.putExtra("Priority", Integer.toString(itemPriority));
            replyIntent.putExtra("End Date", date);
            replyIntent.putExtra("Details", details);
            replyIntent.putExtra("Duration", duration);
            setResult(RESULT_OK, replyIntent);
        }

        else if (id == R.id.action_cancel) {
            setResult(RESULT_CANCELED, replyIntent);
        }

        finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        duration = (double)position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
