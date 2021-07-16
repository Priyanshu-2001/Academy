package com.sports.oscaracademy.service;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class attendanceService {
    public String date ;
    Boolean isPresent , onLeave;
    FirebaseDatabase db = FirebaseDatabase.getInstance();


    public void getData() {
    }

    public void Updatedatabase(Map<String, Object> attend, int date, int month, int year, ProgressBar progressBar) {
        String updateDate = date+"-"+month+"-"+year;

        db.getReference().child(updateDate).updateChildren(attend)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(progressBar.getContext(), "e.getLocalizedMessage()", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(progressBar.getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}
