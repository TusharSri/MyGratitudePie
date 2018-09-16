package com.example.apple.gratpie;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Objects;

public class ShowImageFragment extends Fragment {

    public ShowImageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_image, container, false);
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
        String attachFile = null;
        if (getArguments() != null) {
            attachFile = getArguments().getString(getString(R.string.show_image));
        }
        Objects.requireNonNull(getActivity()).findViewById(R.id.sharing_imageview).setVisibility(View.GONE);
        ImageView previewImageView = getActivity().findViewById(R.id.image_preview);
        Glide.with(getContext())
                .load(attachFile)
                .into(previewImageView);
    }

    @Override
    public void onPause() {
        super.onPause();
        Objects.requireNonNull(getActivity()).findViewById(R.id.sharing_imageview).setVisibility(View.VISIBLE);
    }
}
