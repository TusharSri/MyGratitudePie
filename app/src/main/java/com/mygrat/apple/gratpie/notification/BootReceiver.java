package com.mygrat.apple.gratpie.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mygrat.apple.gratpie.Utils.ReminderUtils;

import java.util.Calendar;

/**
 * Created by azem on 11/3/17.
 */

public class BootReceiver extends BroadcastReceiver {

    public static final String TAG = BootReceiver.class.getSimpleName();

    public void onReceive(Context context, Intent intent) {
        // Your code to execute when Boot Completed
        if(intent!=null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            ReminderUtils.setNotificationReminder(context);
        }
    }

}