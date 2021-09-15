package com.sports.oscaracademy.bottomSheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.sports.oscaracademy.R;
import com.sports.oscaracademy.data.feedsData;
import com.sports.oscaracademy.service.feedsService;

import java.util.Date;

public class newsFeed_b_sheet extends BottomSheetDialogFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_news_feed, container);
        TextView feed = v.findViewById(R.id.feeds);
        Button send = v.findViewById(R.id.post);
        feed.requestFocus();
        send.setOnClickListener(v1 -> {
            if (!feed.getText().toString().isEmpty()) {
                feedsService s = new feedsService();

                s.UpdateFeeds(new feedsData(new Date(), feed.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), new Date().getTime() / 1000), getContext());
                feed.setText("");
                this.dismiss();
            }
        });

        return v;
    }
}
