package com.mygrat.apple.gratpie.Utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mygrat.apple.gratpie.notification.AlarmNotificationReceiver;

import java.util.Calendar;

public class ReminderUtils {

    public static final int REMINDER_REQUEST_CODE = 111;
    public static final String REMINDER_HOUR = "reminder_hour";
    public static final String REMINDER_MINUTE = "reminder_minute";
    public static final int DEFAULT_REMINDER_HOUR = 21;
    public static final int DEFAULT_REMINDER_MINUTE = 0;
    private static final long TWO_MINUTES = 120000L;
    public static final String TAG = ReminderUtils.class.getSimpleName();

    public static void setNotificationReminder(Context context){
        //Get the reminder time from shared preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int hour = sharedPreferences.getInt(REMINDER_HOUR, DEFAULT_REMINDER_HOUR);
        int minute = sharedPreferences.getInt(REMINDER_MINUTE, DEFAULT_REMINDER_MINUTE);
        Calendar reminderTime = Calendar.getInstance();
        reminderTime.set(Calendar.HOUR_OF_DAY, hour);
        reminderTime.set(Calendar.MINUTE, minute);

        Calendar now = Calendar.getInstance();
        if(now.getTimeInMillis()-reminderTime.getTimeInMillis()>=TWO_MINUTES){
            reminderTime.add(Calendar.DAY_OF_MONTH,1);
        }
        setAlarm(context,reminderTime);
    }

    public static void setNotificationReminder(Context context,Calendar time){
        setAlarm(context,time);
    }

    private static void setAlarm(Context context,Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,AlarmNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REMINDER_REQUEST_CODE,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        calendar.set(Calendar.SECOND,0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        }
        else{
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        }
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putLong("next_alarm",calendar.getTimeInMillis());
        editor.commit();
        Log.i(TAG, "Alarm set for "+calendar.getTimeInMillis());
    }

}
