package com.btbsolutions.timekeeper.utility;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

//import com.btbsolutions.timemanagementapp.UpdateToDoItem;

import com.btbsolutions.timekeeper.ForgotPassword;
import com.btbsolutions.timekeeper.RegistrationForm;
import com.btbsolutions.timekeeper.UpdateToDoItem;

import java.util.Calendar;

/**
 * A simple {@link } subclass.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    private int choice;
    public void setChoice(int choice){
        this.choice = choice;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        switch (choice){
            case 0:
                UpdateToDoItem upactivity = (UpdateToDoItem) getActivity();
                upactivity.processDatePickerResult(year, month, day);
                break;
            case 1:
                RegistrationForm regactivity = (RegistrationForm) getActivity();
                regactivity.processDatePickerResult(year, month, day);
                break;
            case 2:
                ForgotPassword forgotactivity = (ForgotPassword) getActivity();
                forgotactivity.processDatePickerResult(year, month, day);
                break;
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it.
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
}
