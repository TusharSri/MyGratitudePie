package com.mygrat.apple.gratpie;


import android.os.Bundle;
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
import com.mygrat.apple.gratpie.Utils.Constants;

import java.util.Objects;

import androidx.navigation.Navigation;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowMomentFragment extends Fragment implements View.OnClickListener {

    private Button editMomentButton;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

    /**
     * To initialize the views
     */
    private void initViews() {
        Objects.requireNonNull(getActivity()).findViewById(R.id.sharing_imageview).setVisibility(View.VISIBLE);
        TextView showDate = getActivity().findViewById(R.id.date_show_textview);
        TextView showDay = getActivity().findViewById(R.id.day_show_textview);
        TextView showDescription = getActivity().findViewById(R.id.description_textview);
        ImageView showImageAdded = getActivity().findViewById(R.id.imageview_file_show_added_preview);
        editMomentButton = getActivity().findViewById(R.id.button_edit_show_moment);
        editMomentButton.setOnClickListener(this);

        if (getArguments() != null) {
            day = getArguments().getString(getString(R.string.day));
        }
        if (getArguments() != null) {
            date = getArguments().getString(getString(R.string.date));
        }
        if (getArguments() != null) {
            formattedDate = getArguments().getString(getString(R.string.formatted_date));
        }
        if (getArguments() != null) {
            attachFile = getArguments().getString(Constants.MOMENT_ATTACH_FILE);
        }
        if (getArguments() != null) {
            attachDesc = getArguments().getString(Constants.MOMENT_DESCRIPTION);
        }
        if (getArguments() != null) {
            counter = getArguments().getInt(getString(R.string.counter));
        }
        if (getArguments() != null) {
            timeInMiliSecond = getArguments().getLong(getString(R.string.getTimeInMili));
        }

        showDate.setText(formattedDate);
        showDay.setText(day);
        showDescription.setText(attachDesc);

        if(attachFile != null && !attachFile.isEmpty() && !attachFile.contains("null")) {
            Glide.with(getActivity())
                    .load(attachFile)
                    .into(showImageAdded);
            showImageAdded.setVisibility(View.VISIBLE);
            showImageAdded.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_edit_show_moment:
                editMoment();
                break;
            case R.id.imageview_file_show_added_preview:
                openImagePreviewActivity();
                break;
        }
    }

    private void openImagePreviewActivity() {
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
