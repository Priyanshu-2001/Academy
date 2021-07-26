package com.sports.oscaracademy.service;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sports.oscaracademy.data.Studentdata;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class studentsList {
    private MutableLiveData<ArrayList<Studentdata>> data = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Studentdata>> users = new MutableLiveData<>();
    private FirebaseFirestore store = FirebaseFirestore.getInstance();
    private FirebaseDatabase db= FirebaseDatabase.getInstance();
    private Context mContext;
    private MutableLiveData<String> roll;
    private String userID;


    public MutableLiveData<String> getRoll(String userID){
        if (roll==null){
            roll = new MutableLiveData<>();
            loadRoll();
        }
        this.userID = userID;
        return roll;
    }

    private void loadRoll() {
        db.getReference().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                String rollNo = task.getResult().child("Last_RollNo").getValue().toString();
                Log.e("ROllNumber", "onComplete: last "+rollNo );
                updateRollList(Integer.parseInt(rollNo));
            }
        });
    }

    private void updateRollList(int rollNo) {
        Map<String, Object > map = new HashMap<>();
        map.put("Last_RollNo", (rollNo+1));
        db.getReference().updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                Map<String , Object> updateROll = new HashMap<>();
                updateROll.put("isStudent","true");
                FirebaseFirestore.getInstance().collection("user").document(userID).update(updateROll).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        Log.e("ROllNumber", "onComplete: updated user "+rollNo );
                        roll.setValue(String.valueOf(rollNo));
                    }
                });
            }
        });
    }

    public studentsList(Context c) {
        this.mContext = c;
    }

    public MutableLiveData<ArrayList<Studentdata>> getStudents(){
        store.collection("students").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<Studentdata> tempData = new ArrayList<>();
                    String name, rollno,phone, userId, email,sex,Age;
                    Timestamp Dob;
                    try {
                        for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                            name = task.getResult().getDocuments().get(i).getString("name");
                            phone = task.getResult().getDocuments().get(i).getString("phone number");
                            userId = task.getResult().getDocuments().get(i).getString("userID");
                            rollno = task.getResult().getDocuments().get(i).getString("RollNo");
                            sex = task.getResult().getDocuments().get(i).getString("Sex");
                            email = task.getResult().getDocuments().get(i).getString("Email");
                            Dob = task.getResult().getDocuments().get(i).getTimestamp("Dob");
                            Age = task.getResult().getDocuments().get(i).getString("Age");
                            Map<String, Object> feesObj = (Map<String, Object>) task.getResult().getDocuments().get(i).get("fees");
                            Timestamp startDate = (Timestamp) feesObj.get("valid from");
                            Timestamp EndDate = (Timestamp) feesObj.get("valid to");
                            Log.e("TAG", "onComplete: "+ startDate);
                            Log.e("TAG", "onComplete: "+ EndDate);
                            assert EndDate != null;
                            Date d=  startDate.toDate();
                            SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                            Log.e("TAG", "onComplete: "+ startDate.toDate());// Fri Jul 16 00:00:00 GMT+05:30 2021
                            Log.e("TAG", "onComplete: "+ sfd.format(d)); //16-02-2021

                            tempData.add(new Studentdata(name,rollno,phone,userId,email,Dob,sex,Age,startDate,EndDate));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    data.setValue(tempData);
                }else{
                    data.setValue(null);
                    Toast.makeText(mContext, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.d("TAG", "getStudents: " + data);
        return data;
    }

    public MutableLiveData<ArrayList<Studentdata>> getUsers(){
        store.collection("user").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<Studentdata> tempData = new ArrayList<>();
                    String name, phone , userId , email, sex , Age , isStudent;
                    Timestamp Dob;
                    try {
                        for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                            name = task.getResult().getDocuments().get(i).getString("name");
                            phone = task.getResult().getDocuments().get(i).getString("Phone Number");
                            userId = task.getResult().getDocuments().get(i).getString("userID");
                            sex = task.getResult().getDocuments().get(i).getString("Sex");
                            email = task.getResult().getDocuments().get(i).getString("email");
                            Age = task.getResult().getDocuments().get(i).getString("Age");
                            if(task.getResult().getDocuments().get(i).getString("isStudent").equals("false"))
                                tempData.add(new Studentdata(name,phone,userId,email,sex,Age));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    users.setValue(tempData);
                }else{
                    Toast.makeText(mContext, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.d("TAG", "getStudents: " + data);
        return users;
    }

    public void deleteUser(){

    }
}
