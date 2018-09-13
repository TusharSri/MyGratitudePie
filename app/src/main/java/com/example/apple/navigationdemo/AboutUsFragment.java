package com.example.apple.navigationdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutUsFragment extends Fragment {

    public AboutUsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView aboutUsTextView = getActivity().findViewById(R.id.about_us_textview);
        aboutUsTextView.setText(Html.fromHtml("<h2><strong>Contact</strong></h2>\n" +
                "<p>You can contact us about this privacy statement by emailing us at the address below:</p>\n" +
                "<p>Email: mygratitudepie@gmail.com</p>"));
    }
}
