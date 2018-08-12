package com.example.apple.navigationdemo;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.apple.navigationdemo.Utils.Constants;
import com.example.apple.navigationdemo.Utils.ConvertUriToFilePath;

import java.io.File;

import androidx.navigation.Navigation;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowMomentFragment extends Fragment implements View.OnClickListener {

    TextView showDate;
    TextView showDay;
    TextView showDescription;
    ImageView showImageAdded;
    Button editMomentButton;
    private String day;
    private String date;
    private String formattedDate;
    private String attachFile;
    private String attachDesc;
    private int counter;
    private long timeInMiliSecond;

    public ShowMomentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_moment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        initViews();
    }

    private void initViews() {
        showDate = getActivity().findViewById(R.id.date_show_textview);
        showDay = getActivity().findViewById(R.id.day_show_textview);
        showDescription = getActivity().findViewById(R.id.description_textview);
        showImageAdded = getActivity().findViewById(R.id.imageview_file_show_added_preview);
        editMomentButton = getActivity().findViewById(R.id.button_edit_show_moment);
        editMomentButton.setOnClickListener(this);

        day = getArguments().getString(getString(R.string.day));
        date = getArguments().getString(getString(R.string.date));
        formattedDate = getArguments().getString(getString(R.string.formatted_date));
        attachFile = getArguments().getString(Constants.MOMENT_ATTACH_FILE);
        attachDesc = getArguments().getString(Constants.MOMENT_DESCRIPTION);
        counter = getArguments().getInt(getString(R.string.counter));
        timeInMiliSecond = getArguments().getLong(getString(R.string.getTimeInMili));

        showDate.setText(formattedDate);
        showDay.setText(day);
        showDescription.setText(attachDesc);

        Glide.with(getActivity())
                .load(attachFile)
                .into(showImageAdded);
        showImageAdded.setVisibility(View.VISIBLE);
        showImageAdded.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_edit_show_moment:
                editMoment();
                break;
            case R.id.imageview_file_show_added_preview:
                openImagePreviewDailog();
                break;
        }
    }

    private void openImagePreviewDailog() {
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.show_image), attachFile);
        Navigation.findNavController(editMomentButton).navigate(R.id.showImageFragment, bundle);
    }

    private void editMoment() {
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.day), day);
        bundle.putString(getString(R.string.formatted_date), formattedDate);
        bundle.putString(getString(R.string.date), date);
        bundle.putString(Constants.MOMENT_DESCRIPTION, attachDesc);
        bundle.putString(Constants.MOMENT_ATTACH_FILE, attachFile);
        bundle.putBoolean(getString(R.string.isComingFromShowFragment), true);
        bundle.putLong(getString(R.string.getTimeInMili),timeInMiliSecond);
        bundle.putInt(getString(R.string.counter), counter);
        Navigation.findNavController(editMomentButton).popBackStack();
        Navigation.findNavController(editMomentButton).navigate(R.id.editMomentFragment, bundle);
    }
}
