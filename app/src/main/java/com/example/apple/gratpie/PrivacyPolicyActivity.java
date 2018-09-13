package com.example.apple.gratpie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class PrivacyPolicyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        TextView privacyTextView = findViewById(R.id.privacy_textview);
        privacyTextView.setText(Html.fromHtml("<p>By using <em>Gratitude Pie </em>you accept and agree to be bound by our Privacy Policy, the <u><a href=\"https://www.google.com/intl/en/policies/terms\">Google Terms of Service</a></u>, and <u><a href=\"https://www.google.com/policies/privacy\">Google Privacy Policy</a></u>.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<h1><strong>Our Privacy Policy</strong></h1>\n" +
                "<h2><strong>Types of Data collected</strong></h2>\n" +
                "<p><strong>Effective on 2018-09-16</strong></p>\n" +
                "<p>This privacy statement describes how we collects and uses the personal information you provide. It also describes the choices available to you regarding our use of your personal information and how you can access and update this information.</p>\n" +
                "<h2><strong>Collection and Use</strong></h2>\n" +
                "<p><strong>We collect the following personal information from you:</strong></p>\n" +
                "<ul>\n" +
                "<li>Contact Information such as email address</li>\n" +
                "<li>Device Information such as device model, device operating system version, total storage used in phone and total storage used in Google Drive.</li>\n" +
                "</ul>\n" +
                "<p><strong>We use this information to:</strong></p>\n" +
                "<ul>\n" +
                "<li>Conduct research and analysis for product improvement.</li>\n" +
                "</ul>\n" +
                "<h2><strong>Sharing Your Information</strong></h2>\n" +
                "<p>We will share your information with third parties only in the ways that are described in this privacy statement.</p>\n" +
                "<h2><strong>Security</strong></h2>\n" +
                "<p>The security of your personal information is important to us. We follow generally accepted industry standards to protect the personal information submitted to us, both during transmission and once we receive it.</p>\n" +
                "<p>We will retain your information for as long as your account is active or as needed to provide you services.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p><strong>Other Information</strong></p>\n" +
                "<p><strong>Notification of Privacy Statement Changes</strong></p>\n" +
                "<p><strong>&nbsp;</strong></p>\n" +
                "<p>We may update this privacy statement to reflect changes to our information practices. If we make any material changes we will notify you by email (sent to the e-mail address specified in your account) prior to the change becoming effective. We encourage you to periodically review this page for the latest information on our privacy practices.</p>\n" +
                "<h2><strong>&nbsp;</strong></h2>\n" +
                "<h2><strong>Contact</strong></h2>\n" +
                "<p>You can contact us about this privacy statement by emailing us at the address below:</p>\n" +
                "<p>Email: mygratitudepie@gmail.com</p>\n" +
                "<p>&nbsp;</p>"));
    }
}
