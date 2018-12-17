package com.mygrat.apple.gratpie;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mygrat.apple.gratpie.Database.PieChartData;
import com.mygrat.apple.gratpie.Database.PieChartDatabase;
import com.mygrat.apple.gratpie.Utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.navigation.Navigation;

import static android.app.Activity.RESULT_OK;


/**
 * In this fagment we can edit or add a fragment
 */
public class EditMomentFragment extends Fragment implements View.OnClickListener {

    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private DatabaseReference dateRef;
    private DatabaseReference counterRef;
    private DatabaseReference momentRef;
    private DatabaseReference urlRef;
    private EditText momentTextView;
    private ImageView fileAddedPreviewImageview;
    private Button addFileButton;
    private int PICK_IMAGE_REQUEST = 100;
    private String date;
    private int counter = 0;
    private PieChartData[] pieChartData;
    private String attachFile = "";
    private String attachDesc = "";
    private boolean isFromShow = false;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.WRITE_CALENDAR,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.CAMERA
    };
    private long getTimeInMili;

    public EditMomentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_moment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (attachDesc != null && attachDesc.isEmpty()) {
            attachDesc = getArguments().getString(Constants.MOMENT_DESCRIPTION);
        }
        momentTextView = getActivity().findViewById(R.id.edit_text_moment);
        momentTextView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        if (attachDesc != null && !attachDesc.isEmpty()) {
            momentTextView.setText(attachDesc);
        }
        if (!hasPermissions(getActivity(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initViews();
        fetchDataFromDB();
    }

    private void initViews() {
        SharedPreferences prefs = getContext().getSharedPreferences(getString(R.string.user_id), getContext().MODE_PRIVATE);
        String userId = prefs.getString(getString(R.string.user_id), Constants.EMPTY_STRING);
        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child(userId);
        Objects.requireNonNull(getActivity()).findViewById(R.id.sharing_imageview).setVisibility(View.GONE);
        fileAddedPreviewImageview = getActivity().findViewById(R.id.imageview_file_added_preview);
        addFileButton = getActivity().findViewById(R.id.button_add_file);
        Button attachMomentButton = getActivity().findViewById(R.id.button_add_moment);
        addFileButton.setOnClickListener(this);
        attachMomentButton.setOnClickListener(this);
        TextView dayOfMonthTextView = getActivity().findViewById(R.id.day_edit_frag_textview);
        TextView formattedDateTextView = getActivity().findViewById(R.id.date_edit_frag_textview);
        assert getArguments() != null;
        String day = getArguments().getString(getString(R.string.day));
        String formattedDate = getArguments().getString(getString(R.string.formatted_date));
        date = getArguments().getString(getString(R.string.date));
        getTimeInMili = getArguments().getLong(getString(R.string.getTimeInMili));
        isFromShow = getArguments().getBoolean(getString(R.string.isComingFromShowFragment));
        if (attachFile != null && attachFile.isEmpty()) {
            attachFile = getArguments().getString(Constants.MOMENT_ATTACH_FILE);
        }
        if (attachDesc != null && attachDesc.isEmpty()) {
            attachDesc = getArguments().getString(Constants.MOMENT_DESCRIPTION);
        }
        if (attachDesc != null && !attachDesc.isEmpty()) {
            Glide.with(getActivity())
                    .load(attachFile)
                    .into(fileAddedPreviewImageview);
            fileAddedPreviewImageview.setVisibility(View.VISIBLE);
        }

        dayOfMonthTextView.setText(day);
        formattedDateTextView.setText(formattedDate);
    }

    public void fileAttachedClicked() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            ;
            startActivityForResult(getPickImageChooserIntent(), PICK_IMAGE_REQUEST);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()), android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(addFileButton, R.string.need_pemission_to_show_pic, Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.READ_EXTERNAL_STORAGE_CODE);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.READ_EXTERNAL_STORAGE_CODE);
            }
        }
    }

    public Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getContext().getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    private Uri getCaptureImageOutputUri() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";
        File f = new File(mPath);
        return Uri.fromFile(f);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.READ_EXTERNAL_STORAGE_CODE) {
            String permission = permissions[0];
            int grantResult = grantResults[0];

            if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(getPickImageChooserIntent(), PICK_IMAGE_REQUEST);
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void addMomentToDatabase() {
        final String momentAdded = momentTextView.getText().toString().trim();
        if (momentAdded.equals("")) {
            Snackbar.make(addFileButton, R.string.please_add_your_moment_first, Snackbar.LENGTH_SHORT).show();
        } else {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    counter++;
                    PieChartData pieChartData = new PieChartData(date, counter, momentAdded, attachFile);
                    PieChartDatabase.getInstance(getActivity())
                            .getPieChartDao()
                            .insert(pieChartData);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    try {
                        ContentResolver cr = Objects.requireNonNull(getContext()).getContentResolver();
                        ContentValues cv = new ContentValues();
                        cv.put(CalendarContract.Events.TITLE, momentAdded);
                        cv.put(CalendarContract.Events.DTSTART, getTimeInMili);
                        cv.put(CalendarContract.Events.DTEND, getTimeInMili + 60 * 60 * 1000);
                        cv.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().getID());
                        cv.put(CalendarContract.Events.CALENDAR_ID, 1);
                        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, cv);
                        ContentUris.parseId(uri);
                        Snackbar.make(addFileButton, "Successfully Added", Snackbar.LENGTH_SHORT).show();
                        Navigation.findNavController(addFileButton).popBackStack();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.execute();
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    private void updateMoment() {
        final String momentAdded = momentTextView.getText().toString().trim();
        if (momentAdded.equals("")) {
            Snackbar.make(addFileButton, R.string.please_add_your_moment_first, Snackbar.LENGTH_SHORT).show();
        } else {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    PieChartData pieChartData = new PieChartData(date, counter + 1, momentAdded, attachFile);
                    PieChartDatabase.getInstance(getActivity())
                            .getPieChartDao()
                            .updatePie(pieChartData);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    Snackbar.make(addFileButton, "Successfully Updated", Snackbar.LENGTH_SHORT).show();
                    Navigation.findNavController(addFileButton).popBackStack();
                }
            }.execute();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_file:
                fileAttachedClicked();
                break;
            case R.id.button_add_moment:
                dateRef = userRef.child(date);
                counterRef = dateRef.child(counter+1+"");
                urlRef = counterRef.child("Url");
                momentRef = counterRef.child("Moment");
                momentRef.setValue(momentTextView.getText().toString().trim());
                urlRef.setValue(attachFile);
                if (isFromShow) {
                    updateMoment();
                } else {
                    addMomentToDatabase();
                }
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void fetchDataFromDB() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPostExecute(Void aVoid) {
                if (isFromShow) {
                    if (getArguments() != null) {
                        counter = getArguments().getInt(getString(R.string.counter));
                    }
                } else {
                    for (PieChartData aPieChartData : pieChartData) {
                        counter = aPieChartData.getCounter();
                    }
                }
            }

            @Override
            protected Void doInBackground(Void... voids) {
                pieChartData = PieChartDatabase.getInstance(getActivity())
                        .getPieChartDao()
                        .getPieChartData(date);
                return null;
            }
        }.execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null) {
                try {
                Bitmap bitmap;
                Uri uri = null;
                if (data.getData() == null) {
                    bitmap = (Bitmap) data.getExtras().get("data");

                    try {
                        Date now = new Date();
                        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
                        String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";
                        File imageFile = new File(mPath);
                        FileOutputStream outputStream = new FileOutputStream(imageFile);
                        int quality = 100;
                        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                        outputStream.flush();
                        outputStream.close();
                        attachFile = String.valueOf(imageFile);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getContext()).getContentResolver(), data.getData());

                    try {
                        Date now = new Date();
                        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
                        String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";
                        File imageFile = new File(mPath);
                        FileOutputStream outputStream = new FileOutputStream(imageFile);
                        int quality = 100;
                        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                        outputStream.flush();
                        outputStream.close();
                        attachFile = String.valueOf(imageFile);
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    uri = data.getData();
                }

                fileAddedPreviewImageview.setVisibility(View.VISIBLE);
                    //handle image
                    if(uri != null){
                        bitmap = rotateImageIfRequired(bitmap, uri);
                    }
                    bitmap = getResizedBitmap(bitmap, 500);
                    fileAddedPreviewImageview.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private Bitmap rotateImageIfRequired(Bitmap bitmap, Uri selectedImage) throws IOException {

        int orientation = getOrientation(selectedImage);

        if (orientation <= 0) {
            return bitmap;
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(orientation);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);

        return bitmap;
    }

    private int getOrientation(Uri photoUri) {
        Cursor cursor = getContext().getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);

        if (cursor.getCount() != 1) {
            cursor.close();
            return -1;
        }

        cursor.moveToFirst();
        int orientation = 0;
        try {
            orientation = cursor.getInt(0);
        } catch (Exception e){
            e.printStackTrace();
        }
        cursor.close();
        cursor = null;
        return orientation;
    }

}