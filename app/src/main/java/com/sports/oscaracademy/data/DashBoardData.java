package com.sports.oscaracademy.data;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class DashBoardData {
    public String ImageUrl , fieldname;

    public String getImageUrl() {
        return ImageUrl;
    }


    public String getFieldname() {
        return fieldname;
    }

    public DashBoardData(String imageUrl, String fieldname) {
        ImageUrl = imageUrl;
        this.fieldname = fieldname;
    }
    @BindingAdapter("android:loadImage") // function name is independent of this xml loadImage :)
    public static void loadImage(ImageView imageView, String URL) {
        Glide.with(imageView)
                .load(URL)
                .into(imageView)
        ;
    }
}
