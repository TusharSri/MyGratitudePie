package com.mygrat.apple.gratpie;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mygrat.apple.gratpie.Utils.Constants;
import com.mygrat.apple.gratpie.Utils.ReminderUtils;

import java.util.Calendar;

public class SetReminderActivity extends AppCompatActivity {

    Button btnSetReminder;
    TextView tvDailyReminderWarning;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_reminder);
        btnSetReminder = findViewById(R.id.btn_set_reminder);
        tvDailyReminderWarning = findViewById(R.id.tv_daily_reminder_warning);

        //Get the default notification reminder time
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final int hour = sharedPreferences.getInt(ReminderUtils.REMINDER_HOUR,ReminderUtils.DEFAULT_REMINDER_HOUR);
        final int minute = sharedPreferences.getInt(ReminderUtils.REMINDER_MINUTE,ReminderUtils.DEFAULT_REMINDER_MINUTE);

        String reminderTimeText = getFormattedTime(hour, minute);
        btnSetReminder.setText(reminderTimeText);
        btnSetReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the default notification reminder time
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SetReminderActivity.this);
                final int hour = sharedPreferences.getInt(ReminderUtils.REMINDER_HOUR,ReminderUtils.DEFAULT_REMINDER_HOUR);
                final int minute = sharedPreferences.getInt(ReminderUtils.REMINDER_MINUTE,ReminderUtils.DEFAULT_REMINDER_MINUTE);

                showTimePickerDialog(hour, minute);
            }
        });

        tvDailyReminderWarning.setText(Html.fromHtml(getString(R.string.daily_reminder_warning)));
        tvDailyReminderWarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openBlog = new Intent(Intent.ACTION_VIEW);
                openBlog.setData(Uri.parse(Constants.DAILY_REMINDER_BLOG_URL));
                startActivity(openBlog);
            }
        });
    }

    private void showTimePickerDialog(int hour, int minute) {
        new TimePickerDialog(SetReminderActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(ReminderUtils.REMINDER_HOUR,hourOfDay);
                editor.putInt(ReminderUtils.REMINDER_MINUTE,minute);
                editor.apply();

                Calendar reminderTime = Calendar.getInstance();
                reminderTime.set(Calendar.HOUR_OF_DAY,hourOfDay);
                reminderTime.set(Calendar.MINUTE,minute);

                ReminderUtils.setNotificationReminder(SetReminderActivity.this,reminderTime);
                String reminderTimeLabel = getFormattedTime(hourOfDay,minute);
                btnSetReminder.setText(reminderTimeLabel);

                Toast.makeText(SetReminderActivity.this,"Reminder set for "+reminderTimeLabel+" everyday!",Toast.LENGTH_LONG).show();
            }
        },hour,minute,false).show();
    }

    @NonNull
    private String getFormattedTime(int hour, int minute) {
        return String.format("%02d:%02d %s",hour>12?hour-12:hour,minute,hour>12?"PM":"AM");
    }
}
