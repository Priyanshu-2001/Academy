package com.sports.oscaracademy.service;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sports.oscaracademy.data.Attendance_list;
import com.sports.oscaracademy.data.Studentdata;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

public class attendanceService {
    public String date;
    public ArrayList<Attendance_list> list;
    public ArrayList<Studentdata> studentData;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    FirebaseFirestore store = FirebaseFirestore.getInstance();

    public void getStudent() {
        store.collection("students").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
//                    TODO start Here with data from firebase firestore
//                    String name = queryDocumentSnapshots.getDocuments().get(i).get()
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

            }
        });
    }
    public MutableLiveData<ArrayList<Attendance_list>> data = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Attendance_list>> getPreviousRecord(String getdate) {
        ArrayList<Attendance_list> templist = new ArrayList<>();
        db.getReference().child("attendance").child(getdate).child("attendance").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        String roll = snap.getKey();
                        String status = (String) snap.getValue();
                        Log.d("TAG", "onSuccess: " + status);
                        boolean isPresent = false;
                        boolean onLeave = false;
                        if (status.equals("P")) {
                            isPresent = true;
                        }
                        if (status.equals("L")) {
                            onLeave = true;
                        }
                        templist.add(new Attendance_list(roll, isPresent, onLeave));
                    }
                    data.setValue(templist);
                }else{
                    data.setValue(null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

            }
        });

//        db.getReference().get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//            @Override
//            public void onSuccess(DataSnapshot dataSnapshot) {
//                Log.e("service", "chilren COunt: " + dataSnapshot.getChildrenCount());
//                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
//                    date = dataSnapshot.child(getdate).;
//                    Log.e("Service", "date server: " + date);
//                    Log.e("Service", "date need: " + getdate);
//                    if (getdate.equals(date)) {
//
//                        for (int j = 0; j < dataSnapshot.child(date).child("attendance").getChildrenCount(); j++) {
//                            String rollNo = (String) dataSnapshot.child(date).child("attendance").getKey();
//                            String status = (String) dataSnapshot.child(date).child("attendance").getValue();
//                            boolean isPresent = false;
//                            boolean onLeave = false;
//                            if (status.equals("P")) {
//                                isPresent = true;
//                            }
//                            if (status.equals("L")) {
//                                onLeave = true;
//                            }
//                            templist.add(new studentAttendance(rollNo, isPresent, onLeave));
//                        }
//                    }
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull @NotNull Exception e) {
//                Log.e("TAG", "onFailure:  failed to get Attenddance");
//            }
//        });
        Log.d("TAG", "getPreviousRecord: " + templist);
        return data;
    }

    public void Updatedatabase(Map<String, Object> attend, int date, int month, int year, ProgressBar progressBar) {
        String updateDate = date + "-" + month + "-" + year;
        db.getReference().child("attendance")
                .child(updateDate)
                .child("attendance")
                .updateChildren(attend)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(progressBar.getContext(), "Attendance Submitted", Toast.LENGTH_SHORT).show();
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
