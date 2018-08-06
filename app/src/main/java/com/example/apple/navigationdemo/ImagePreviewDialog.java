package com.example.apple.navigationdemo;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImagePreviewDialog extends Dialog {


    public ImagePreviewDialog(@NonNull Context context, String attachFile) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.dialog_image_preview);
        ImageView previewImageView = findViewById(R.id.image_preview);
        Glide.with(context)
                .load(attachFile)
                .into(previewImageView);
    }
}
