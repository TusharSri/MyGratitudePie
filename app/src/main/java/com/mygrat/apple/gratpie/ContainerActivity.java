package com.mygrat.apple.gratpie;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mygrat.apple.gratpie.Utils.Constants;
import com.mygrat.apple.gratpie.notification.AlarmNotificationReceiver;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


public class ContainerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView drawerIcon;
    private ImageView sharingImage;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).hide();

        SharedPreferences.Editor editor = getSharedPreferences(Constants.CURRENT_DATE_PREF, MODE_PRIVATE).edit();
        editor.putString("currentDate", new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()));
        editor.apply();

        sharingImage = findViewById(R.id.sharing_imageview);
        sharingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyStoragePermissions(ContainerActivity.this);
                sharingButtonClicked(view);
            }
        });

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        drawerIcon = findViewById(R.id.drawer_icon_imageview);
        drawerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        startAlarm(true, true);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_calendar) {
            startActivity(new Intent(this, ContainerActivity.class));
            finish();
        } else if (id == R.id.nav_connect) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.MAIL_TO + Constants.EMAIL_ID));
            intent.putExtra(Intent.EXTRA_SUBJECT, R.string.gratitude_pie_contact_team);
            intent.putExtra(Intent.EXTRA_TEXT, Constants.EMPTY_STRING);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            // Create new fragment and transaction
            FragmentManager manager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.my_nav_host_fragment, new AboutUsFragment());
            transaction.commit();
        } else if (id == R.id.nav_privacy_policy) {
            startActivity(new Intent(getApplicationContext(), PrivacyPolicyActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getUSerPhotoAndName();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getUSerPhotoAndName() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        SharedPreferences prefs = getSharedPreferences(getString(R.string.user_data), MODE_PRIVATE);
        String name = prefs.getString(getString(R.string.name), Constants.EMPTY_STRING);
        String email = prefs.getString(getString(R.string.email), Constants.EMPTY_STRING);
        Uri url = Uri.parse(prefs.getString(getString(R.string.url), Constants.EMPTY_STRING));
        NavigationView activity_dashboard = findViewById(R.id.nav_view);
        View headerView = activity_dashboard.getHeaderView(0);

        ImageView profileImageView = headerView.findViewById(R.id.profile_imageView);
        TextView nameTextView = headerView.findViewById(R.id.name_textView);
        TextView emailTextView = headerView.findViewById(R.id.email_textView);

        Glide.with(this).load(url)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profileImageView);

        nameTextView.setText(name);
        emailTextView.setText(email);
    }

    public void sharingButtonClicked(View view) {
        takeScreenshotAndShare();
        drawerIcon.setVisibility(View.VISIBLE);
        sharingImage.setVisibility(View.VISIBLE);
        if (findViewById(R.id.button_edit_show_moment) != null) {
            findViewById(R.id.button_edit_show_moment).setVisibility(View.VISIBLE);
        }
    }

    /**
     * Here er are creating bitmap of current activity and storing it into phone and shaing it via any app which support image
     */

    private void takeScreenshotAndShare() {
        if (null != drawerIcon) {
            drawerIcon.setVisibility(View.GONE);
        }
        if (sharingImage != null) {
            sharingImage.setVisibility(View.GONE);
        }
        if (findViewById(R.id.button_edit_show_moment) != null) {
            findViewById(R.id.button_edit_show_moment).setVisibility(View.GONE);
        }
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            Uri uri = Uri.fromFile(imageFile);
            Intent sharingIntent = new Intent();
            sharingIntent.setAction(Intent.ACTION_SEND);
            sharingIntent.setType("image/*");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.shared_content_subject));
            sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.shared_content_body));
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void startAlarm(boolean isNotification, boolean isRepeat) {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent;

        // SET TIME HERE
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Constants.HOUR);
        calendar.set(Calendar.MINUTE, Constants.MINUTE);


        myIntent = new Intent(ContainerActivity.this, AlarmNotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);


        if (!isRepeat)
            manager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 3000, pendingIntent);
        else
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
