package com.btbsolutions.timekeeper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.btbsolutions.timekeeper.activities.SettingsActivity;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "I am running", Toast.LENGTH_SHORT).show();
        Log.d("AlarmCheck", "Next Alarm " + Calendar.getInstance().getTimeInMillis());

        /*Intent in = new Intent();
        in.setAction("START_BACKUP");
        context.sendBroadcast(in);*/

        SettingsActivity inst = SettingsActivity.instance();
        if(inst!=null)
            inst.startBackup();

    }
}