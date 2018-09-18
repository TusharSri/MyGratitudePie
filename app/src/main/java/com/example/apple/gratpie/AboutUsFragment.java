package com.example.apple.gratpie;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

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
        Objects.requireNonNull(getActivity()).findViewById(R.id.sharing_imageview).setVisibility(View.GONE);

        TextView aboutUsTextView = getActivity().findViewById(R.id.about_us_textview);
        aboutUsTextView.setText(Html.fromHtml("<h1><strong>Why Gratitude?</strong></h1>\n" +
                "<p><span style=\"font-weight: 400;\">Gratitude is a positive emotion that promises Happiness and good Health.</span></p>\n" +
                "<h3><strong>The Personal Benefits of practicing Gratitude</strong></h3>\n" +
                "<p><span style=\"font-weight: 400;\">Many Scientific researchers already proved that practicing Gratitude -</span></p>\n" +
                "<ul>\n" +
                "<li style=\"font-weight: 400;\"><span style=\"font-weight: 400;\">Improves Well-being</span></li>\n" +
                "<li style=\"font-weight: 400;\"><span style=\"font-weight: 400;\">Improves Relationships</span></li>\n" +
                "<li style=\"font-weight: 400;\"><span style=\"font-weight: 400;\">Strengthen Immune system</span></li>\n" +
                "<li style=\"font-weight: 400;\"><span style=\"font-weight: 400;\">Improves Sleep quality</span></li>\n" +
                "<li style=\"font-weight: 400;\"><span style=\"font-weight: 400;\">Reduces stress and relaxes the mind</span></li>\n" +
                "</ul>\n" +
                "<p>&nbsp;</p>\n" +
                "<p><strong>Life will never be perfect for any of us but Gratitude encourages us to identify goodness in our Life.</strong></p>\n" +
                "<p><br /><br /><br /></p>\n" +
                "<h1><strong>What is \"Gratitude Pie\"?</strong></h1>\n" +
                "<p><span style=\"font-weight: 400;\">A Pie-style Gratitude Journal App which make memories easy and quick to save. It is very Interactive and share-friendly too.</span></p>\n" +
                "<p>&nbsp;</p>\n" +
                "<ol>\n" +
                "<li style=\"font-weight: 400;\"><span style=\"font-weight: 400;\">Add your grateful moments along with a picture on-the-go.</span></li>\n" +
                "</ol>\n" +
                "<p><span style=\"font-weight: 400;\">(Go to date and tap on I am grateful for button</span><span style=\"font-weight: 400;\">)</span></p>\n" +
                "<ol>\n" +
                "<li style=\"font-weight: 400;\"><span style=\"font-weight: 400;\">Edit them whenever you want.</span></li>\n" +
                "</ol>\n" +
                "<p><span style=\"font-weight: 400;\">(Tap on the Pie to edit your grateful moments)</span></p>\n" +
                "<ol>\n" +
                "<li style=\"font-weight: 400;\"><span style=\"font-weight: 400;\">See them whenever you feel sad and gloomy.</span></li>\n" +
                "</ol>\n" +
                "<p><span style=\"font-weight: 400;\">(Go to date and view your Pie for the day)</span></p>\n" +
                "<ol>\n" +
                "<li style=\"font-weight: 400;\"><span style=\"font-weight: 400;\">Share them with others to express the gratitude.</span></li>\n" +
                "</ol>\n" +
                "<p><span style=\"font-weight: 400;\">(Share your Gratitude Pie or your grateful moment)</span></p>\n" +
                "<p><br /><br /></p>\n" +
                "<p><strong>It is recommended that each night before going to bed, you should note down the things you were grateful for in a day.</strong></p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p><span style=\"font-weight: 400;\">With this App, </span><span style=\"font-weight: 400;\">you can add 5 grateful moments each day and view them as a colorful Pie. </span><span style=\"font-weight: 400;\">Your Gratitude </span><span style=\"font-weight: 400;\">Pies can be </span><span style=\"font-weight: 400;\">synchronized on Google drive and viewed on any Android device.</span></p>\n" +
                "<p><br /><br /></p>\n" +
                "<p><strong>Contact Us</strong></p>\n" +
                "<p><span style=\"font-weight: 400;\">To report Bugs, suggest new features or to say hi.</span></p>\n" +
                "<p><span style=\"font-weight: 400;\">Email- </span><a href=\"mailto:mygratitudepie@gmail.com\"><span style=\"font-weight: 400;\">mygratitudepie@gmail.com</span></a></p>\n" +
                "<p><br /><br /></p>\n" +
                "<p><strong>Connect with Us!</strong></p>\n" +
                "<p><a href=\"https://m.facebook.com/groups/Gratitudepie\"><span style=\"font-weight: 400;\">Join our Facebook Group to share your Gratitude Pie</span></a><span style=\"font-weight: 400;\">.</span></p>\n" +
                "<p><a href=\"https://twitter.com/gratitudepie\"><span style=\"font-weight: 400;\">Twitter</span></a></p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p><strong>Rate Us</strong></p>\n" +
                "<p><span style=\"font-weight: 400;\">If you enjoy using Gratitude Pie, kindly rate us in the store.</span></p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p><span style=\"font-weight: 400;\">Rate us Now (google play store link)</span></p>\n" +
                "<p><br /><br /></p>\n" +
                "<p><strong>@2018 Gratitude Pie</strong></p>\n"  +
                "<p><strong>Designed by Vipul Dhiman</strong></p>\n\n\n\n"));
    }

    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(getActivity()).findViewById(R.id.sharing_imageview).setVisibility(View.VISIBLE);

    }
}
