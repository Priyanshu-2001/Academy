package com.sports.oscaracademy.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sports.oscaracademy.data.feedsData;
import com.sports.oscaracademy.service.feedsService;

import java.util.ArrayList;
import java.util.Collections;

public class feedsViewModel extends AndroidViewModel {
    ArrayList<feedsData> feedsData = new ArrayList<>();
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    feedsService service = new feedsService();
    private MutableLiveData<ArrayList<com.sports.oscaracademy.data.feedsData>> feedsData1;

    public feedsViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<ArrayList<com.sports.oscaracademy.data.feedsData>> getData() {
        if (feedsData1 == null) {
            feedsData1 = new MutableLiveData<>();
            db.getReference().child("newsfeeds").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    feedsData.clear();
                    for (DataSnapshot snap : snapshot.getChildren()) {

                        for (DataSnapshot s : snap.getChildren()) {
                            feedsData data = s.getValue(feedsData.class);
                            data.setFeedID(s.getKey());
                            data.setOnlyDate(snap.getKey());
                            feedsData.add(data);
                        }
                        Collections.reverse(feedsData);
                    }
                    Log.e("TAG", "onDataChange: " + feedsData);
                    feedsData1.setValue(feedsData);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        return feedsData1;
    }
}
