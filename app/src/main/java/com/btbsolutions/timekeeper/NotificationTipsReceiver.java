package com.btbsolutions.timekeeper;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.Random;

public class NotificationTipsReceiver extends BroadcastReceiver {
    //private static int tipNumber = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        //ComponentName comp = new ComponentName(context.getPackageName(), NotificationTipsService.class.getName());

        Intent intent1 = new Intent(context, NotificationTipsService.class);
        Calendar calendar = Calendar.getInstance();

        intent1.putExtra("tip", findRandomTip(context));
        NotificationTipsService.enqueueWork(context, intent1);
        Log.d("Notification", "Tips notification");
    }

    private String findRandomTip(Context context) {
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        String arr[] = context.getResources().getStringArray(R.array.tips);
        int tipNumber = random.nextInt(arr.length);
        result.append(arr[tipNumber]);
        return result.toString();
    }
}
