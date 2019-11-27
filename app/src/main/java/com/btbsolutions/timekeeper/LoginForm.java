package com.btbsolutions.timekeeper;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.btbsolutions.timekeeper.activities.MainActivity;
import com.btbsolutions.timekeeper.activities.NavigationActivity;
import com.btbsolutions.timekeeper.asyncClasses.GetAllDailyTaskDataAsync;
import com.btbsolutions.timekeeper.asyncClasses.GetAllToDoDataAsync;
import com.btbsolutions.timekeeper.asyncClasses.LoginAsync;
import com.btbsolutions.timekeeper.interfaces.GetAllDailyTaskDataResponse;
import com.btbsolutions.timekeeper.interfaces.GetAllToDoDataResponse;
import com.btbsolutions.timekeeper.interfaces.LoginResponse;
import com.btbsolutions.timekeeper.utility.DailyTasks;
import com.btbsolutions.timekeeper.utility.Helpers;
import com.btbsolutions.timekeeper.utility.ToDo;
import com.btbsolutions.timekeeper.utility.ToDoViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginForm extends AppCompatActivity implements GetAllToDoDataResponse,
        GetAllDailyTaskDataResponse, LoginResponse {

    private EditText username_et, password_et;
    private View mProgressView;
    private View mLoginFormView;
    private ToDoViewModel mToDoViewModel;

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.btbsolutions.timekeeper.sharedpreffile";
    SharedPreferences.Editor editor;
    LoginAsync loginAsync = null;

    Helpers helper = new Helpers();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        username_et = (EditText) findViewById(R.id.et_username);
        password_et = (EditText) findViewById(R.id.et_password);

        mProgressView = findViewById(R.id.login_progress);
        mLoginFormView = findViewById(R.id.login_form);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        mToDoViewModel = ViewModelProviders.of(this).get(ToDoViewModel.class);

        editor = mPreferences.edit();

        boolean hasLoggedIn = mPreferences.getBoolean("hasLoggedIn", false);
        if (hasLoggedIn) {
                Intent intent = new Intent(this, NavigationActivity.class);
                startActivity(intent);
                finish();
        }
    }

    public void OnLogin(View view) {
        String useremail = username_et.getText().toString();
        String password = password_et.getText().toString();

        if (helper.validateEmail(useremail)) {
            if (helper.validatePassword(password)) {
                loginAsync = new LoginAsync(this, mProgressView, mLoginFormView);
                loginAsync.delegate = this;
                loginAsync.execute(useremail, password);
            } else {
                Toast.makeText(this, "Invalid password.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_LONG).show();
        }
    }

    public void openRegisterForm(View view) {
        Intent intent = new Intent(this, RegistrationForm.class);
        startActivity(intent);
    }

    public void onCancel(View view) {
        loginAsync.cancel(true);
    }


    @Override
    public void getAllToDoDataProcessFinish(String output) {
        String useremail = mPreferences.getString("email", null);
        //     Log.d("conn_chk_todo", output);
        try {
            JSONArray arr = new JSONObject(output).getJSONArray("posts");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject post = arr.getJSONObject(i).getJSONObject("post");

                long id = post.getLong("Id");
                String task = post.getString("Task");
                String date = post.getString("Date");
                int priority = post.getInt("Priority");
                double duration = post.getDouble("Duration");
                String detailed_notes = post.getString("Detailed_Notes");
                int complete = post.getInt("Complete");
                ToDo toDo = new ToDo(task, date, priority, detailed_notes, duration, complete, true, useremail);
                toDo.setId(id);

                mToDoViewModel.insert(toDo);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        GetAllDailyTaskDataAsync getAllDailyTaskDataAsync = new GetAllDailyTaskDataAsync(this, mProgressView, mLoginFormView);
        getAllDailyTaskDataAsync.delegate = this;
        getAllDailyTaskDataAsync.execute(useremail);
    }

    @Override
    public void loginProcessFinish(String output) {

        //    Log.d("conn_chk", output);
        /*String[] data = output.split(";");

        //Toast.makeText(this, data[0], Toast.LENGTH_LONG).show();

        if (data[0].equals("Login success")) {
            String fname = data[1];
            String lname = data[2];
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString("username", fname);
            editor.putString("lastname", lname);
            editor.putString("email", useremail);
            editor.putBoolean("hasLoggedIn", true);
            editor.apply();

            GetAllToDoDataAsync getAllToDoDataAsync = new GetAllToDoDataAsync(this);
            getAllToDoDataAsync.delegate = this;
            getAllToDoDataAsync.execute(useremail);
        }*/
        try {
            JSONArray arr = new JSONObject(output).getJSONArray("posts");
            Log.d("JSON_length", String.valueOf(arr.length()));
            for (int i = 0; i < arr.length(); i++) {
                JSONObject post = arr.getJSONObject(i).getJSONObject("post");

                String email = post.getString("UserEmail");
                String fname = post.getString("FirstName");
                String lname = post.getString("LastName");
                String bdate = post.getString("Birthdate");

                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putString("username", fname);
                editor.putString("lastname", lname);
                editor.putString("email", email);
                editor.putBoolean("hasLoggedIn", true);
                editor.apply();

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, output, Toast.LENGTH_SHORT).show();
            return;
        }
        String useremail = mPreferences.getString("email", null);
        GetAllToDoDataAsync getAllToDoDataAsync = new GetAllToDoDataAsync(this, mProgressView, mLoginFormView);
        getAllToDoDataAsync.delegate = this;
        getAllToDoDataAsync.execute(useremail);
    }

    @Override
    public void getAllDailyTaskDataProcessFinish(String output) {
        String useremail = mPreferences.getString("email", null);
        //   Log.d("conn_chk_daily_task", output);
        try {
            JSONArray arr = new JSONObject(output).getJSONArray("posts");
            //      Log.d("conn_chk_daily_task", "Inside Json try");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject post = arr.getJSONObject(i).getJSONObject("post");
                //           Log.d("conn_chk_daily_task", "Inside Json for");

                long id = post.getLong("Id");
                String task = post.getString("Task");
                String date = post.getString("Date");
                String start_time = post.getString("Start_Time");
                String end_time = post.getString("End_Time");
                String metric = post.getString("MetricQ");
                double duration = post.getDouble("Duration");
                int complete = post.getInt("Complete");

                DailyTasks dailyTasks = new DailyTasks(task, date, start_time, end_time, metric,
                        duration, complete, true, useremail);
                dailyTasks.setId(id);

                mToDoViewModel.insert(dailyTasks);
            }
        } catch (JSONException e) {
            //    Log.d("conn_chk_daily_task", "Inside Json catch");
            e.printStackTrace();
        }
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
        finish();
    }

    public void onForgotPassword(View view) {
        Intent intent = new Intent(this, ForgotPassword.class);
        startActivity(intent);
    }
}

