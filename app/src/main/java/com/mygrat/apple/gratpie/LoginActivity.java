package com.mygrat.apple.gratpie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mygrat.apple.gratpie.Database.PieChartData;
import com.mygrat.apple.gratpie.Database.PieChartDatabase;
import com.mygrat.apple.gratpie.Utils.Constants;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 7;
    private static final String TAG = "LoginActivity";
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ImageView icon;
    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private DatabaseReference dateRef;
    private DatabaseReference counterRef;
    private DatabaseReference momentRef;
    private DatabaseReference urlRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        checkForUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForCrashes();
    }

    public void googleSignInStarts() {
        showProgressDialog();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void initViews() {
        icon = findViewById(R.id.heading_immageview);
        Animation aniSlide = AnimationUtils.loadAnimation(this, R.anim.splash_animation);
        icon.startAnimation(aniSlide);

        FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();
        // Creating and Configuring Google Sign In object.
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Creating and Configuring Google Api Client.
        mGoogleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
                .enableAutoManage(LoginActivity.this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Snackbar.make(icon, R.string.connection_failed, Snackbar.LENGTH_LONG).show();
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.user_id), MODE_PRIVATE).edit();
                    editor.putString(getString(R.string.user_id), user.getUid());
                    editor.apply();
                    hideProgressDialog();
                    startActivity(new Intent(getApplicationContext(), ContainerActivity.class));

                    finish();
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            googleSignInStarts();
                        }
                    }, 3000);
                }
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                } else {
                    hideProgressDialog();
                    Snackbar.make(icon, R.string.login_failed, Snackbar.LENGTH_LONG).show();
                }
            } else {
                hideProgressDialog();
                Snackbar.make(icon, R.string.login_failed, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        hideProgressDialog();
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), R.string.login_failed,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            String name = mAuth.getCurrentUser().getDisplayName();
                            String email = mAuth.getCurrentUser().getEmail();
                            Uri photoUrl = mAuth.getCurrentUser().getPhotoUrl();
                            SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.user_data), MODE_PRIVATE).edit();
                            editor.putString(getString(R.string.name), name);
                            editor.putString(getString(R.string.url), photoUrl.toString());
                            editor.putString(getString(R.string.email), email);
                            editor.apply();
                            //start firebase sync hee
                            fetchDataFromFirebaseAndSyncDB();
                        }
                    }
                });
    }

    private void fetchDataFromFirebaseAndSyncDB() {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.user_id), MODE_PRIVATE);
        String userId = prefs.getString(getString(R.string.user_id), Constants.EMPTY_STRING);
        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        String date = dataSnapshot1.getKey();
                        dateRef = userRef.child(date);
                        for (int j = 1; j <= dataSnapshot1.getChildrenCount(); j++) {
                            String momentDesc = dataSnapshot1.child(j+"").child("Moment").getValue(String.class);
                            String urlDesc = dataSnapshot1.child(j+"").child("Url").getValue(String.class);
                            if(null != momentDesc && !momentDesc.isEmpty()){
                                insertIntoDb(date,j,momentDesc,urlDesc);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Do Nothing
            }
        });
    }

    private void insertIntoDb(final String date, final int count, final String momentDesc, final String urlDesc) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                PieChartData pieChartData = new PieChartData(date, count, momentDesc, urlDesc);
                PieChartDatabase.getInstance(getApplicationContext())
                        .getPieChartDao()
                        .insert(pieChartData);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                try {
                    //ContentResolver cr = getContentResolver();
                    //ContentValues cv = new ContentValues();
                    //cv.put(CalendarContract.Events.TITLE, momentDesc);
                    //cv.put(CalendarContract.Events.DTSTART, getTimeInMili);
                    //cv.put(CalendarContract.Events.DTEND, getTimeInMili + 60 * 60 * 1000);
                    //cv.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().getID());
                    //cv.put(CalendarContract.Events.CALENDAR_ID, 1);
                    //if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    //return;
                    //}
                    //Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, cv);
                    //ContentUris.parseId(uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void showProgressDialog() {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(LoginActivity.this);
                mProgressDialog.setMessage("Loading");
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.show();
            }
        } catch (Exception e) {
            Log.e(TAG,e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {
            Log.e(TAG,e.getMessage());
        }
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void checkForCrashes() {
        CrashManager.register(this);
    }

    private void checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this);
    }
}
