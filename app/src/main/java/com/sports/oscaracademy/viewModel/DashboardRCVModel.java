package com.sports.oscaracademy.viewModel;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.AndroidViewModel;

import com.bumptech.glide.Glide;
import com.sports.oscaracademy.R;

import org.jetbrains.annotations.NotNull;

public class DashboardRCVModel extends AndroidViewModel {
    public DashboardRCVModel(@NonNull @NotNull Application application) {
        super(application);
    }



    @BindingAdapter("android:loadImage") // function name is independent of this xml loadImage :)
    public static void loadImage(ImageView imageView, String URL) {
        if(!URL.isEmpty()) {
            Glide.with(imageView)
                    .load(URL)
                    .into(imageView)
            ;
        }else{
        }
    }

}
