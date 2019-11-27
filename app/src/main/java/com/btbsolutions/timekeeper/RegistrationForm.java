package com.btbsolutions.timekeeper;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.TooltipCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.btbsolutions.timekeeper.activities.MainActivity;
import com.btbsolutions.timekeeper.asyncClasses.RegisterAsync;
import com.btbsolutions.timekeeper.interfaces.RegisterResponse;
import com.btbsolutions.timekeeper.utility.DatePickerFragment;
import com.btbsolutions.timekeeper.utility.Helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RegistrationForm extends AppCompatActivity implements RegisterResponse{

    private EditText fname_et, lname_et, et_day, et_month, et_year, username_et, password_et, confirm_password_et;
    private TextView passwordWarning, tv_dob_title;
    private View mProgressView;
    private View mLoginFormView;
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.btbsolutions.timekeeper.sharedpreffile";
    SharedPreferences.Editor editor;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    String dateMessage;
    boolean checkConfirmPassword;

    private ImageView confirmPasswordImageButton;
    Helpers helper = new Helpers();

    private DatePickerDialog.OnDateSetListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);

        fname_et = (EditText) findViewById(R.id.et_fname);
        lname_et = (EditText) findViewById(R.id.et_lname);
        et_day = (EditText) findViewById(R.id.et_day);
        et_month = (EditText) findViewById(R.id.et_month);
        et_year = (EditText) findViewById(R.id.et_year);
        username_et = (EditText) findViewById(R.id.et_username);
        password_et = (EditText) findViewById(R.id.et_password);
        confirm_password_et = (EditText) findViewById(R.id.et_confirm_password);
        confirmPasswordImageButton = (ImageView) findViewById(R.id.confirmPasswordImageButton);
        passwordWarning = findViewById(R.id.passwordWarning);
        tv_dob_title = findViewById(R.id.tv_dob_title);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tv_dob_title.setTooltipText(getString(R.string.tooltip_dob_reason));
        } else {
            TooltipCompat.setTooltipText(tv_dob_title, getString(R.string.tooltip_dob_reason));
        }

        mProgressView = findViewById(R.id.registration_progress);
        mLoginFormView = findViewById(R.id.login_form);


        format.setLenient(false);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        confirm_password_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pass1 = password_et.getText().toString();
                String pass2 = confirm_password_et.getText().toString();
                passwordWarning.setVisibility(View.GONE);

                if(pass1.equals(pass2)){
                    confirmPasswordImageButton.setImageResource(R.drawable.ic_check_green_24dp);
                    checkConfirmPassword = true;
                } else {
                    confirmPasswordImageButton.setImageResource(R.drawable.ic_close_red_24dp);
                    checkConfirmPassword = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        password_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pass1 = password_et.getText().toString();
                String pass2 = confirm_password_et.getText().toString();
                passwordWarning.setVisibility(View.GONE);

                if(pass1.equals(pass2)){
                    confirmPasswordImageButton.setImageResource(R.drawable.ic_check_green_24dp);
                    checkConfirmPassword = true;
                } else {
                    confirmPasswordImageButton.setImageResource(R.drawable.ic_close_red_24dp);
                    checkConfirmPassword = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

         listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar dateCal = Calendar.getInstance();

                dateCal.set(Calendar.YEAR, year);
                dateCal.set(Calendar.MONTH, month);
                dateCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateCal.set(Calendar.HOUR_OF_DAY, 0);
                dateCal.set(Calendar.MINUTE, 0);
                dateCal.set(Calendar.SECOND, 0);

                et_day.setText(helper.addZeroIfLessThan9(dateCal.get(Calendar.DAY_OF_MONTH)));
                et_month.setText(helper.addZeroIfLessThan9(dateCal.get(Calendar.MONTH)+1));
                et_year.setText(helper.addZeroIfLessThan9(dateCal.get(Calendar.YEAR)));
            }
        };

    }

    public void onRegister(View view) {

        String fname = fname_et.getText().toString();
        String lname = lname_et.getText().toString();
        String date = et_day.getText().toString();
        String month = et_month.getText().toString();
        String year = et_year.getText().toString();
        String useremail = username_et.getText().toString();
        String password = password_et.getText().toString();
        String dateString = null;
        Date dt = null;
        try {
            dt = format.parse(year+"-"+month+"-"+date);
            dateString = format.format(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //backgroundWorker.execute(type, fname, lname, age, username, password);
        if(helper.validateEmail(useremail)) {
            if(helper.validatePassword(password)) {
                if(!isStringNull(dateString)){
                    if(!fname.isEmpty() && !lname.isEmpty()) {
                        RegisterAsync registerAsync = new RegisterAsync(this, mProgressView, mLoginFormView);
                        registerAsync.delegate = this;
                        registerAsync.execute(fname, lname, dateString, useremail, password);
                    } else {
                        Toast.makeText(this, "Please don't leave field Empty", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Please enter correct date", Toast.LENGTH_LONG).show();
                }
            }
            else {
                passwordWarning.setVisibility(View.VISIBLE);
            }
        }
        else {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_LONG).show();
        }
    }

    public void captureDate(View view) {
//        DialogFragment newFragment = new DatePickerFragment();
//        ((DatePickerFragment) newFragment).setChoice(1);
//        newFragment.show(getSupportFragmentManager(),getString(R.string.datepicker));

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(RegistrationForm.this,
                listener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    public void processDatePickerResult(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, 0,0,0);
        dateMessage = format.format(new Date(c.getTimeInMillis()));
        et_day.setText(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
    }

    @Override
    public void registerProcessFinish(String output) {
        String fname = fname_et.getText().toString();
        String lname = lname_et.getText().toString();
        String bdate = et_day.getText().toString();
        String useremail = username_et.getText().toString();
        String welcome_string = "Welcome "+ fname + " " + lname;
        if (output.equals("Inserted Successfully")) {
            Toast.makeText(this, welcome_string, Toast.LENGTH_LONG).show();
            editor = mPreferences.edit();
            editor.putBoolean("hasLoggedIn", true);
            editor.putString("username", fname);
            editor.putString("lastname", lname);
            editor.putString("email", useremail);
            editor.apply();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
        else {
            Toast.makeText(this, output, Toast.LENGTH_LONG).show();
        }
    }

    private boolean isStringNull(String in){
        return in==null;
    }
}
