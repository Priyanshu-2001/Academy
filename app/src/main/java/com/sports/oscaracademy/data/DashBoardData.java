package com.sports.oscaracademy.data;

import android.widget.ImageView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.sports.oscaracademy.R;

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
        int drawable = R.drawable.app_icon;
        switch (URL) {
            case "https://firebasestorage.googleapis.com/v0/b/oscar-academy-c95ef.appspot.com/o/dashBoard%2F6274.jpg?alt=media&token=009e9695-279b-42da-8602-dd3cf8ff7e7e":
                drawable = R.drawable.attendance;
                break;
            case "https://firebasestorage.googleapis.com/v0/b/oscar-academy-c95ef.appspot.com/o/dashBoard%2FSchedule.jpg?alt=media&token=07cc965f-a849-400b-8c31-196167f232cc":
                drawable = R.drawable.schedule;
                break;
            case "https://firebasestorage.googleapis.com/v0/b/oscar-academy-c95ef.appspot.com/o/dashBoard%2Ffees.jpg?alt=media&token=36a4eac1-0a36-4c80-9fa8-a57509d7f296":
                drawable = R.drawable.fees;
                break;
            case "https://firebasestorage.googleapis.com/v0/b/oscar-academy-c95ef.appspot.com/o/dashBoard%2Fstudent.png?alt=media&token=92d8678d-82fd-44a6-be45-a010e0224fca":
                drawable = R.drawable.student;
                break;
            case "https://firebasestorage.googleapis.com/v0/b/oscar-academy-c95ef.appspot.com/o/dashBoard%2Fupcoming_tournament.png?alt=media&token=aca89095-a9ba-45fd-b58b-64a7bcd51ad2":
                drawable = R.drawable.upcoming_tournament;
                break;
            case "https://firebasestorage.googleapis.com/v0/b/oscar-academy-c95ef.appspot.com/o/dashBoard%2Fuser.png?alt=media&token=2969bc51-4c74-442a-9082-e8f9ec2c28dd":
                drawable = R.drawable.user;
                break;
        }
        Glide.with(imageView.getContext())
                .load(URL)
                .into(imageView)
                .onLoadFailed(AppCompatResources.getDrawable(imageView.getContext(), drawable));
    }
}
