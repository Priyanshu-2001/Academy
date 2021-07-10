package com.sports.oscaracademy.data;

import android.app.Application;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;

public class DashBoardData extends ViewModel {
    public String ImageUrl , fieldname;

    public String getImageUrl() {
        return ImageUrl;
    }


    public String getFieldname() {
        return fieldname;
    }

    public DashBoardData(String imageUrl, String fieldname) {
        ImageUrl = imageUrl ;
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
