package com.mygrat.apple.gratpie;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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
                "<p>Gratitude is a positive emotion that promises Happiness and good Health.</p>\n" +
                "<h3><strong>The Personal Benefits of practicing Gratitude</strong></h3>\n" +
                "<p>Many Scientific researchers already proved that practicing Gratitude -</p>\n" +
                "<ul>\n" +
                "<li>Improves Well-being</li>\n" +
                "<li>Improves Relationships</li>\n" +
                "<li>Strengthen Immune system</li>\n" +
                "<li>Improves Sleep quality</li>\n" +
                "<li>Reduces stress and relaxes the mind</li>\n" +
                "</ul>\n" +
                "<p><em>&nbsp;</em></p>\n" +
                "<p><strong>Life will never be perfect for any of us but Gratitude encourages us to identify goodness in our Life.</strong></p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<h1><strong>What is \"Gratitude Pie\"?</strong></h1>\n" +
                "<p>A Pie-style Gratitude Journal App which make memories easy and quick to save. It is very Interactive and share-friendly too.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<ol>\n" +
                "<li>Add your grateful moments along with a picture on-the-go.</li>\n" +
                "</ol>\n" +
                "<p>(Go to date and tap on I am grateful for button)</p>\n" +
                "<ol>\n" +
                "<li>Edit them whenever you want.</li>\n" +
                "</ol>\n" +
                "<p>(Tap on the Pie to edit your grateful moments)</p>\n" +
                "<ol>\n" +
                "<li>See them whenever you feel sad and gloomy.</li>\n" +
                "</ol>\n" +
                "<p>(Go to date and view your Pie for the day)</p>\n" +
                "<ol>\n" +
                "<li>Share them with others to express the gratitude.</li>\n" +
                "</ol>\n" +
                "<p>(Share your Gratitude Pie or your grateful moment)</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p><strong>It is recommended that each night before going to bed, you should note down the things you were grateful for in a day.</strong></p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>With this App, you can add 5 grateful moments each day and view them as a colorful Pie. Your Gratitude Pies can be synchronized on Google drive and viewed on any Android device.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p><strong>Contact Us</strong></p>\n" +
                "<p>To report Bugs, suggest new features or to say hi.</p>\n" +
                "<p>Email- <u><a href=\"mailto:mygratitudepie@gmail.com\">mygratitudepie@gmail.com</a></u></p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p><strong>Connect with Us!</strong></p>\n" +
                "<p><u><a href=\"https://m.facebook.com/groups/Gratitudepie\">Join our Facebook Group to share your Gratitude Pie</a></u>.</p>\n" +
                "<p><u><a href=\"https://twitter.com/gratitudepie\">Twitter</a></u></p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p><strong>Rate Us</strong></p>\n" +
                "<p>If you enjoy using Gratitude Pie, kindly rate us in the store.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p><a href=\"https://play.google.com/store/apps/details?id=com.mygrat.apple.gratpie\">Rate us Now </a></p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p><strong>@2018 Gratitude Pie</strong></p>\n" +
                "<p><strong>Developed by Kamal Vaid</strong></p>\n" +
                "<p><strong>Designed by Vipul Dhiman</strong></p>"));
        aboutUsTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(getActivity()).findViewById(R.id.sharing_imageview).setVisibility(View.VISIBLE);

    }
}
