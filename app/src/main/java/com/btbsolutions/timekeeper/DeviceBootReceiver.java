package com.btbsolutions.timekeeper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class DeviceBootReceiver extends BroadcastReceiver {

    private static final int ALARM_NOTIFICATION = 987;
    private static final int ALARM_TIPS_NOTIFICATION = 479;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.d("TimekeeperBootStart", "Alarms Started");

            Calendar now = Calendar.getInstance();
            Calendar notificationTime = Calendar.getInstance();
            notificationTime.set(Calendar.HOUR_OF_DAY, 20);
            notificationTime.set(Calendar.MINUTE, 30);
            notificationTime.set(Calendar.SECOND, 0);

            if(now.after(notificationTime)){
                notificationTime.add(Calendar.DAY_OF_MONTH,1);
            }

            Calendar tipsTime = Calendar.getInstance();
            tipsTime.set(Calendar.HOUR_OF_DAY, 7);
            tipsTime.set(Calendar.MINUTE, 0);
            tipsTime.set(Calendar.SECOND, 0);

            if(now.after(tipsTime)){
                tipsTime.add(Calendar.DAY_OF_MONTH,1);
            }

            Intent notifyIntent = new Intent(context, NightNotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    ALARM_NOTIFICATION,
                    notifyIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );

            Intent tipsNotifyIntent = new Intent(context, NotificationTipsReceiver.class);
            PendingIntent tipsPendingIntent = PendingIntent.getBroadcast(
                    context,
                    ALARM_TIPS_NOTIFICATION,
                    tipsNotifyIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, notificationTime.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, tipsTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, tipsPendingIntent);
        }
    }
}
