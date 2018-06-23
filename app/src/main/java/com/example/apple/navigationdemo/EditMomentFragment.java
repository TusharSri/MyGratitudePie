package com.example.apple.navigationdemo;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.Navigation;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditMomentFragment extends Fragment {

    EditText momentTextView;
    ImageView fileAddedPreviewImageview;
    Button addFileButton;
    Button attachMomentButton;
    TextView dayOfMonthTextView;
    TextView formattedDateTextView;
    private int PICK_IMAGE_REQUEST = 100;
    String url = "empty";
    private int totalRange = 100;

    public EditMomentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_moment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().findViewById(R.id.button_add_moment).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.pieFragment));
        initViews();
    }

    private void initViews() {
        momentTextView = getActivity().findViewById(R.id.edit_text_moment);
        fileAddedPreviewImageview = getActivity().findViewById(R.id.imageview_file_added_preview);
        addFileButton = getActivity().findViewById(R.id.button_add_file);
        attachMomentButton = getActivity().findViewById(R.id.button_attach_moment);
        dayOfMonthTextView = getActivity().findViewById(R.id.day_textview);
        formattedDateTextView = getActivity().findViewById(R.id.date_textview);
        String day = getArguments().getString(getString(R.string.day));
        String formattedDate = getArguments().getString(getString(R.string.formatted_date));
        dayOfMonthTextView.setText(day);
        formattedDateTextView.setText(formattedDate);
    }
}
