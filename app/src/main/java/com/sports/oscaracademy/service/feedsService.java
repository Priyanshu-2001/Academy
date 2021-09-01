package com.sports.oscaracademy.service;

import com.google.firebase.database.FirebaseDatabase;
import com.sports.oscaracademy.data.feedsData;

import java.util.Date;

public class feedsService {
    FirebaseDatabase db = FirebaseDatabase.getInstance();

    public void UpdateFeeds(Date timestamp, feedsData data) {
        String randomKey = db.getReference().push().getKey();
        String d = timestamp.getDate() + "-" + timestamp.getMonth() + "-" + (timestamp.getYear() + 1900);
        db.getReference()
                .child("newsfeeds")
                .child(d)
                .child(randomKey)
                .setValue(data).addOnSuccessListener(unused -> {
//                    sendNotification();
        });
    }

    public void delete_feeds(feedsData data) {
        db.getReference().child("newsfeeds")
                .child(data.getOnlyDate())
                .child(data.getFeedID())
                .removeValue();
    }

    public void update_feeds(feedsData data, feedsData newData) {
        db.getReference().child("newsfeeds")
                .child(data.getOnlyDate())
                .child(data.getFeedID())
                .setValue(newData);
    }
}