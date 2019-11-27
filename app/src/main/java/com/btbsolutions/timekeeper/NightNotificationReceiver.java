package com.btbsolutions.timekeeper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.btbsolutions.timekeeper.activities.MainActivity;

public class NightNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Notification", "Night notification");

        if(MainActivity.todaysTotalQ == 0){
            Intent intent1 = new Intent(context, NotificationIntentService.class);
            NotificationIntentService.enqueueWork(context, intent1);
            //context.startService(intent1);
        }
    }
}
