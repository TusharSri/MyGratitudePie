package com.mygrat.apple.gratpie.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.mygrat.apple.gratpie.ContainerActivity;
import com.mygrat.apple.gratpie.R;
import com.mygrat.apple.gratpie.Utils.ReminderUtils;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static android.app.PendingIntent.FLAG_ONE_SHOT;

public class AlarmNotificationReceiver extends BroadcastReceiver {

    public static final String NOTIFICATION_CHANNEL_ID = "GRATITUDEPIE_REMINDER";
    public static final String NOTIFICATION_CHANNEL_NAME = "GRATITUDEPIE_REMINDER_CHANNEL";
    public static final String NOTIFICATION_CHANNEL_DESCRIPTION = "Reminder to create your gratitude pie";
    public static final String TAG = AlarmNotificationReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        setReminderForTomorrow(context);
        createNotificationChannel(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);

        Intent myIntent = new Intent(context, ContainerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                myIntent,
                FLAG_ONE_SHOT);

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_notification_icon)
                .setContentTitle("My Gratitude Pie")
                .setContentIntent(pendingIntent)
                .setContentText("Time to create your Gratitude Pie")
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo("Info")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    private void setReminderForTomorrow(Context context) {
        //Get the default reminder time
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int hour = sharedPreferences.getInt(ReminderUtils.REMINDER_HOUR,ReminderUtils.DEFAULT_REMINDER_HOUR);
        int minute = sharedPreferences.getInt(ReminderUtils.REMINDER_MINUTE,ReminderUtils.DEFAULT_REMINDER_MINUTE);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        ReminderUtils.setNotificationReminder(context, calendar);
    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
            channel.setDescription(NOTIFICATION_CHANNEL_DESCRIPTION);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}