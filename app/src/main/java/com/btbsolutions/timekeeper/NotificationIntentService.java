package com.btbsolutions.timekeeper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

import com.btbsolutions.timekeeper.activities.DailyTaskActivity;
import com.btbsolutions.timekeeper.activities.MainActivity;

import java.util.Calendar;

public class NotificationIntentService extends JobIntentService {

    private static final int NOTIFICATION_ID = 4;
    private SharedPreferences mPreferences;
    private static final int ALARM_NOTIFICATION = 987;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * //@param name Used to name the worker thread, important only for debugging.
     */

    // Enqueuing work in to this service.
    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, NotificationIntentService.class, NOTIFICATION_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String sharedPrefFile = "com.btbsolutions.timekeeper.sharedpreffile";
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        onHandleIntent(intent);
    }

    protected void onHandleIntent(Intent intent) {
        long last_access = mPreferences.getLong("last_access", Calendar.getInstance().getTimeInMillis());
        Calendar endDateCalendar = Calendar.getInstance();
        endDateCalendar.set(Calendar.MILLISECOND, 0);
        endDateCalendar.set(Calendar.SECOND, 0);
        endDateCalendar.set(Calendar.MINUTE, 0);
        endDateCalendar.set(Calendar.HOUR, 0);
        long remainingDays = Math.round((float) (endDateCalendar.getTimeInMillis() - last_access) / (24 * 60 * 60 * 1000));
        String text;
        if(remainingDays<14) {
            if(remainingDays<7) {
                text = "Procrastination is the Enemy of Progress!!! \nFill your daily log now.";
            } else {
                text = "You have not logged in for more than a week!!! \nRestart your daily log now.";
            }

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                String CHANNEL_ID = "Incomplete Daily Task Notification";
                CharSequence name = "Incomplete Daily Task";
                String Description = "This is the Incomplete Daily Task Channel";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;

                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                mChannel.setDescription(Description);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mChannel.setShowBadge(false);
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                AudioAttributes att = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build();
                mChannel.setSound(alarmSound, att);
                mNotificationManager.createNotificationChannel(mChannel);

                Notification.Builder builder = new Notification.Builder(getApplicationContext(), CHANNEL_ID);
                builder.setContentTitle("Daily Log Not Filled");
                builder.setContentText(text);
                builder.setSmallIcon(R.drawable.ic_notification_icon);
                builder.setAutoCancel(true);
                builder.setStyle(new Notification.BigTextStyle().bigText(text));
                builder.setWhen(System.currentTimeMillis());
                builder.setShowWhen(true);

                Intent notifyIntent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                //to be able to launch your activity from the notification

                builder.setContentIntent(pendingIntent);
                Notification notificationCompat = builder.build();
                //NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);

                if (MainActivity.todaysTotalQ == 0) {
                    mNotificationManager.notify(NOTIFICATION_ID, notificationCompat);
                }
            } else {
                Notification.Builder builder = new Notification.Builder(this);
                builder.setContentTitle("Daily Log Not Filled");
                builder.setContentText(text);
                builder.setSmallIcon(R.drawable.ic_notification_icon);
                builder.setPriority(Notification.PRIORITY_DEFAULT);
                builder.setAutoCancel(true);
                builder.setStyle(new Notification.BigTextStyle().bigText(text));
                builder.setDefaults(Notification.DEFAULT_ALL);
                builder.setWhen(System.currentTimeMillis());
                builder.setShowWhen(true);

                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                builder.setSound(alarmSound).setOnlyAlertOnce(true);

                Intent notifyIntent = new Intent(this, DailyTaskActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                //to be able to launch your activity from the notification

                builder.setContentIntent(pendingIntent);
                Notification notificationCompat = builder.build();
                //NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);

                if (MainActivity.todaysTotalQ == 0) {
                    mNotificationManager.notify(NOTIFICATION_ID, notificationCompat);
                }
            }
        } else{
            MainActivity.cancelIntent();
            stopSelf();
        }
    }
}
