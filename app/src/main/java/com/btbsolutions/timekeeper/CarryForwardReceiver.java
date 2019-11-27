package com.btbsolutions.timekeeper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.btbsolutions.timekeeper.activities.SettingsActivity;

import java.util.Calendar;

public class CarryForwardReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "I am running", Toast.LENGTH_SHORT).show();
        Log.d("AlarmCheck", "Carry forward " + Calendar.getInstance().get(Calendar.HOUR_OF_DAY));

        //context.sendBroadcast(new Intent("START_CARRY_OVER"));
        //settingsActivity.startBackup();

        SettingsActivity inst = SettingsActivity.instance();

        if (inst != null)
            inst.carryForward();
    }
}
