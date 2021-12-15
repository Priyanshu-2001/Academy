package com.sports.oscaracademy.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sports.oscaracademy.data.feedsData;

import java.util.ArrayList;
import java.util.Collections;

public class feedsViewModel extends AndroidViewModel {
    ArrayList<feedsData> feedsData = new ArrayList<>();
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    private MutableLiveData<ArrayList<com.sports.oscaracademy.data.feedsData>> feedsData1;
    private final ValueEventListener feedsListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            feedsData.clear();
            for (DataSnapshot s : snapshot.getChildren()) {
                feedsData data = s.getValue(feedsData.class);
                data.setFeedID(s.getKey());
                feedsData.add(data);
            }
            Collections.reverse(feedsData);
            feedsData1.setValue(feedsData);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public feedsViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        db.getReference()
                .child("newsfeeds")
                .orderByChild("sortingTime")
                .removeEventListener(feedsListener);
    }

    public LiveData<ArrayList<com.sports.oscaracademy.data.feedsData>> getData() {
        if (feedsData1 == null) {
            feedsData1 = new MutableLiveData<>();
            db.getReference()
                    .child("newsfeeds")
                    .orderByChild("sortingTime")
                    .addValueEventListener(feedsListener);
        }
        return feedsData1;
    }
}
