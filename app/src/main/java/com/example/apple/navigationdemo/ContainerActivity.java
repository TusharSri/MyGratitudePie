package com.example.apple.navigationdemo;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.apple.navigationdemo.Utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;


public class ContainerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Calendar");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharingButtonClicked(view);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sync) {

        } else if (id == R.id.nav_calendar) {
            startActivity(new Intent(this,ContainerActivity.class));
            finish();
        } else if (id == R.id.nav_connect) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.MAIL_TO + Constants.EMAIL_ID));
            intent.putExtra(Intent.EXTRA_SUBJECT, R.string.gratitude_pie_contact_team);
            intent.putExtra(Intent.EXTRA_TEXT, Constants.EMPTY_STRING);
            startActivity(intent);
        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_help) {

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
            if (doubleBackToExitPressedOnce) {
                overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.please_click_again_to_exit, Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            takeScreenshotAndShare();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Snackbar.make(view, R.string.need_pemission_to_show_pic, Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(ContainerActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.WRITE_EXTERNAL_STORAGE_CODE);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.WRITE_EXTERNAL_STORAGE_CODE);
            }
        }
    }
    /**
     * Here er are creating bitmap of current activity and storing it into phone and shaing it via any app which support image
     */
    private void takeScreenshotAndShare() {
        String Slash = "/";
        String jpgExtension = ".jpg";
        Date currentDate = new Date();
        android.text.format.DateFormat.format(Constants.DATE_FORMAT, currentDate);
        // image naming and path  to include sd card  appending name you choose for file
        String mPath = Environment.getExternalStorageDirectory().toString() + Slash + currentDate + jpgExtension;
        mPath = mPath.replace(Constants.SPACE_STRING, Constants.EMPTY_STRING);
        // create bitmap screen capture
        View v1 = getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);

        File imageFile = new File(mPath);
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(imageFile);
            int totalRange = 100;
            bitmap.compress(Bitmap.CompressFormat.PNG, totalRange, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        Uri uri = Uri.fromFile(imageFile);
        Intent sharingIntent = new Intent();
        sharingIntent.setAction(Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.shared_content_subject));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.shared_content_body));
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));
    }
}
