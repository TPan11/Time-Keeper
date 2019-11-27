package com.btbsolutions.timekeeper;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.btbsolutions.timekeeper.asyncClasses.ChangePasswordAsync;
import com.btbsolutions.timekeeper.interfaces.ChangePasswordResponse;
import com.btbsolutions.timekeeper.asyncClasses.ForgotPasswordAsync;
import com.btbsolutions.timekeeper.interfaces.ForgotPasswordRespose;
import com.btbsolutions.timekeeper.utility.DatePickerFragment;
import com.btbsolutions.timekeeper.utility.Helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ForgotPassword extends AppCompatActivity implements ForgotPasswordRespose, ChangePasswordResponse {

    private EditText age_et,password_et, confirm_password_et, username_et, temp_password_et;
    private TextView passwordWarning;
    boolean checkConfirmPassword;
    private ImageView confirmPasswordImageButton;
    String dateMessage;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    Helpers helper = new Helpers();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        username_et = (EditText) findViewById(R.id.et_username);
        age_et = (EditText) findViewById(R.id.et_age);
        password_et = (EditText) findViewById(R.id.et_password);
        temp_password_et = (EditText) findViewById(R.id.et_temp_password);
        confirm_password_et = (EditText) findViewById(R.id.et_confirm_password);
        confirmPasswordImageButton = (ImageView) findViewById(R.id.confirmPasswordImageButton);
        passwordWarning = findViewById(R.id.passwordWarning);

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
    }

    public void captureDate(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        ((DatePickerFragment) newFragment).setChoice(2);
        newFragment.show(getSupportFragmentManager(),getString(R.string.datepicker));
    }

    public void processDatePickerResult(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, 0,0,0);
        dateMessage = format.format(new Date(c.getTimeInMillis()));
        age_et.setText(dateMessage);
    }

    public void onSubmit(View view) {
        String useremail = username_et.getText().toString();
        String bdate = age_et.getText().toString();

        if(helper.validateEmail(useremail)) {
            ForgotPasswordAsync forgotPasswordAsync = new ForgotPasswordAsync(this);
            forgotPasswordAsync.delegate = this;
            forgotPasswordAsync.execute(useremail, bdate);
        }
        else {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_LONG).show();
        }
    }

    public void onChangePassword(View view) {
        String useremail = username_et.getText().toString();
        String temppass = temp_password_et.getText().toString();
        String pass = password_et.getText().toString();

        if(checkConfirmPassword) {
            if(helper.validateEmail(useremail)) {
                if (helper.validatePassword(pass)) {
                    ChangePasswordAsync changePasswordAsync = new ChangePasswordAsync(this);
                    changePasswordAsync.delegate = this;
                    changePasswordAsync.execute(useremail, temppass, pass);
                }
                else {
                    passwordWarning.setVisibility(View.VISIBLE);
                }
            }
            else {
                Toast.makeText(this, "Invalid email address", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(this, "Confirm Password not same as Password entered", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void forgotPasswordProcessFinish(String output) {
        Toast.makeText(this, output, Toast.LENGTH_LONG).show();
    }

    @Override
    public void changePasswordProcessFinish(String output) {
        Toast.makeText(this, output, Toast.LENGTH_LONG).show();
        //Log.d("Query", output);
    }
}