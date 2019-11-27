package com.btbsolutions.timekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.btbsolutions.timekeeper.utility.Helpers;

import java.util.List;


public class AddNewItem extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private AutoCompleteTextView mAutoCompleteToDoItemView;
    private EditText mEditTextDetailNote;
    private int itemPriority = 0;
    private Spinner spin_duration_group;
    private double duration = 0.0d;

    private List<String> tasks = Helpers.getAllTasks();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

        mAutoCompleteToDoItemView = findViewById(R.id.edit_todoItem);
        mEditTextDetailNote = findViewById(R.id.detailnote);
        spin_duration_group = findViewById(R.id.spin_duration_group);

        spin_duration_group.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, tasks);

        mAutoCompleteToDoItemView.setAdapter(adapter);

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
        Intent replyIntent = new Intent();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_check) {

            if (TextUtils.isEmpty(mAutoCompleteToDoItemView.getText()) || itemPriority==0) {
                Toast.makeText(this, "Please Enter Values", Toast.LENGTH_SHORT).show();
            }
            else {
                String word = mAutoCompleteToDoItemView.getText().toString();
                String details = mEditTextDetailNote.getText().toString();
                replyIntent.putExtra("Item", word);
                replyIntent.putExtra("Priority", Integer.toString(itemPriority));
                replyIntent.putExtra("End Date", getIntent().getStringExtra("date"));
                replyIntent.putExtra("Details", details);
                replyIntent.putExtra("Duration", duration);

                setResult(RESULT_OK, replyIntent);
                finish();
            }

            return true;
        }
        else if(id == R.id.action_cancel){
            setResult(RESULT_CANCELED, replyIntent);
            finish();
        }

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
