package com.sports.oscaracademy.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.sports.oscaracademy.R;
import com.sports.oscaracademy.data.Studentdata;
import com.sports.oscaracademy.dialog.dialogs;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class studentsList {
    private final MutableLiveData<ArrayList<Studentdata>> data = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Studentdata>> filterData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Studentdata>> users = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Studentdata>> filterUsers = new MutableLiveData<>();
    private final FirebaseFirestore store = FirebaseFirestore.getInstance();
    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private final Context mContext;
    private MutableLiveData<Integer> roll;
    private String userID;

    ArrayList<String> adminID = new ArrayList<>();
    ArrayList<String> coachesID = new ArrayList<>();

    public MutableLiveData<Integer> getRoll(String userID) {
        if (roll == null) {
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
                Log.e("ROllNumber", "onComplete: last " + rollNo);
                updateRollList(Integer.parseInt(rollNo));
            }
        });
    }

    private void updateRollList(int rollNo) {
        Map<String, Object> map = new HashMap<>();
        map.put("Last_RollNo", (rollNo + 1));
        db.getReference().updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                Map<String, Object> updateROll = new HashMap<>();
                updateROll.put("isStudent", "true");
                FirebaseFirestore.getInstance().collection("user").document(userID).update(updateROll).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        Log.e("ROllNumber", "onComplete: updated user " + rollNo);
                        roll.setValue(rollNo);
                    }
                });
            }
        });
    }

    public studentsList(Context c) {
        this.mContext = c;
    }

    public MutableLiveData<ArrayList<Studentdata>> filteredStudentList(String filterType, Object filter) {
        store.collection("students")
                .whereGreaterThanOrEqualTo(filterType, filter)
//                .orderBy("RollNo", Query.Direction.ASCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Studentdata> tempData = new ArrayList<>();
                    String name, phone, userId, email, sex, Age, session, membership;
                    Integer rollno;
                    try {
                        for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                            name = task.getResult().getDocuments().get(i).getString("name");
                            phone = task.getResult().getDocuments().get(i).getString("phone number");
                            userId = task.getResult().getDocuments().get(i).getString("userID");
                            rollno = task.getResult().getDocuments().get(i).get("RollNo", Integer.class);
                            sex = task.getResult().getDocuments().get(i).getString("Sex");
                            email = task.getResult().getDocuments().get(i).getString("email");
                            Age = task.getResult().getDocuments().get(i).getString("Age");
                            membership = task.getResult().getDocuments().get(i).getString("membership");
                            try {
                                session = task.getResult().getDocuments().get(i).getString("session");
                            } catch (Exception e) {
                                session = "N/A";
                            }
                            tempData.add(new Studentdata(name, rollno, phone, userId, email, sex, Age, session, membership));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.e("TAG", "onComplete: " + tempData);
                    filterData.setValue(tempData);
                } else {
                    filterData.setValue(null);
                    Toast.makeText(mContext, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.d("TAG", "getStudents: filter service" + filterData);
        return filterData;
    }

    public MutableLiveData<ArrayList<Studentdata>> filteredUserList(String filterType, Object filter) {
        store.collection("user")
                .whereGreaterThanOrEqualTo(filterType, filter)
//                .orderBy("RollNo", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Studentdata> tempData = new ArrayList<>();
                    String name, phone, userId, email, sex, Age, session, membershipValidity;
                    Integer RollNo;
                    try {
                        for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                            name = task.getResult().getDocuments().get(i).getString("name");
                            phone = task.getResult().getDocuments().get(i).getString("phone number");
                            userId = task.getResult().getDocuments().get(i).getString("userID");
                            sex = task.getResult().getDocuments().get(i).getString("Sex");
                            email = task.getResult().getDocuments().get(i).getString("email");
                            Age = task.getResult().getDocuments().get(i).getString("Age");
                            Studentdata data = new Studentdata(name, phone, userId, email, sex, Age);
                            try {
                                RollNo = task.getResult().getDocuments().get(i).get("RollNo", Integer.class);
                            } catch (Exception e) {
                                RollNo = null;
                            }
                            try {
                                session = task.getResult().getDocuments().get(i).getString("session");
                            } catch (Exception e) {
                                session = "N/A";
                            }
                            try {
                                membershipValidity = task.getResult().getDocuments().get(i).getString("membership");
                            } catch (Exception e) {
                                membershipValidity = "inactive";
                            }
                            data.setMemberShip(membershipValidity);
                            data.setSession(session);
                            if (RollNo != null) {
                                data.setRollno(RollNo);
                            }
                            if (task.getResult().getDocuments().get(i).getString("isStudent").equals("false"))
                                tempData.add(data);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    filterUsers.setValue(tempData);
                } else {
                    Toast.makeText(mContext, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.d("TAG", "getStudents: service filter " + filterUsers);
        return filterUsers;
    }


    public MutableLiveData<ArrayList<Studentdata>> getStudents() {
        store.collection("students")
                .orderBy("RollNo", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Studentdata> tempData = new ArrayList<>();
                            String name, phone, userId, email, sex, Age, session, membership;
                            Integer rollno;
                            try {
                                for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                                    name = task.getResult().getDocuments().get(i).getString("name");
                                    phone = task.getResult().getDocuments().get(i).getString("phone number");
                                    userId = task.getResult().getDocuments().get(i).getString("userID");
                                    rollno = task.getResult().getDocuments().get(i).get("RollNo", Integer.class);
                                    sex = task.getResult().getDocuments().get(i).getString("Sex");
                                    email = task.getResult().getDocuments().get(i).getString("email");
                                    Age = task.getResult().getDocuments().get(i).getString("Age");
                                    membership = task.getResult().getDocuments().get(i).getString("membership");
                                    try {
                                        session = task.getResult().getDocuments().get(i).getString("session");
                                    } catch (Exception e) {
                                        session = "N/A";
                            }
                            tempData.add(new Studentdata(name, rollno, phone, userId, email, sex, Age, session, membership));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    data.setValue(tempData);
                } else {
                    data.setValue(null);
                    Toast.makeText(mContext, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.d("TAG", "getStudents: " + data.getValue());
        return data;
    }

    public MutableLiveData<ArrayList<Studentdata>> getUsers() {
        store.collection("user")
//                .orderBy("name",Oue )
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Studentdata> tempData = new ArrayList<>();
                    String name, phone, userId, email, sex, Age, session, membershipValidity;
                    Integer RollNo;
                    Timestamp Dob;
                    try {
                        for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                            name = task.getResult().getDocuments().get(i).getString("name");
                            phone = task.getResult().getDocuments().get(i).getString("phone number");
                            userId = task.getResult().getDocuments().get(i).getString("userID");
                            sex = task.getResult().getDocuments().get(i).getString("Sex");
                            email = task.getResult().getDocuments().get(i).getString("email");
                            Age = task.getResult().getDocuments().get(i).getString("Age");
                            Studentdata data = new Studentdata(name, phone, userId, email, sex, Age);
                            try {
                                RollNo = task.getResult().getDocuments().get(i).get("RollNo", Integer.class);
                            } catch (Exception e) {
                                RollNo = null;
                            }
                            try {
                                session = task.getResult().getDocuments().get(i).getString("session");
                            } catch (Exception e) {
                                session = "N/A";
                            }
                            try {
                                membershipValidity = task.getResult().getDocuments().get(i).getString("membership");
                            } catch (Exception e) {
                                membershipValidity = "inactive";
                            }
                            data.setMemberShip(membershipValidity);
                            data.setSession(session);
                            if (RollNo != null) {
                                data.setRollno(RollNo);
                            }
                            if (task.getResult().getDocuments().get(i).getString("isStudent").equals("false"))
                                tempData.add(data);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    users.setValue(tempData);
                } else {
                    Toast.makeText(mContext, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.d("TAG", "getStudents: service " + data);
        return users;
    }

    public void deleteStudent(String userIDs, @NonNull Map<String, Object> profile) {
        dialogs dialogs = new dialogs();
        ProgressDialog pd = new ProgressDialog(mContext, R.style.AlertDialog);
        dialogs.alertDialogLogin(pd, "Removing Student");
        profile.put("isStudent", "false");
        store.collection("user").document(userIDs).set(profile, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialogs.dismissDialog(pd);
                deleteStudentfromStudentList(userIDs, dialogs);
            }
        });
    }

    public void deleteStudentfromStudentList(String id, dialogs dialogs) {
        store.collection("students").document(id).delete().addOnCompleteListener(
                task -> {
                    dialogs.dismissDialog(new ProgressDialog(mContext, R.style.AlertDialog));
                    if (task.isSuccessful()) {
                        ((Activity) mContext).finish();
                    } else {
                        dialogs.displayDialog(task.getException().getLocalizedMessage(), mContext);
                    }
                }
        );
    }

    public void updateProfile(String isStudent, SpinKitView progress, String userId, Map<String, Object> dataFromTxtViews) {
        if (isStudent.equals("student")) {
            store.collection("students").document(userId).set(dataFromTxtViews, SetOptions.merge()).addOnCompleteListener(task -> {
                dialogs dialogs = new dialogs();
                if (task.isSuccessful()) {
                    dialogs.displayDialog("Update Sucessfull..", mContext);
                    progress.setVisibility(View.GONE);
                } else {
                    dialogs.displayDialog("Update Failed", mContext);
                    progress.setVisibility(View.GONE);
                }
            });
        } else {
            store.collection("user").document(userId).set(dataFromTxtViews, SetOptions.merge()).addOnCompleteListener(task -> {
                dialogs dialogs = new dialogs();
                if (task.isSuccessful()) {
                    dialogs.displayDialog("Update Sucessfull..", mContext);
                    progress.setVisibility(View.GONE);
                } else {
                    dialogs.displayDialog("Update Failed !", mContext);
                    progress.setVisibility(View.GONE);
                }
            });
        }
    }

    MutableLiveData<Map<String, ArrayList<String>>> finalList = new MutableLiveData<>();

    public MutableLiveData<Map<String, ArrayList<String>>> getchatusers() {
        Map<String, ArrayList<String>> temp = new HashMap<>();
        store.collection("chatResponders").document("Admin").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DocumentSnapshot document = task.getResult();
                        adminID = (ArrayList<String>) document.get("adminID");
                        temp.put("admin", adminID);
                    }
                }

                store.collection("chatResponders").document("coaches").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                DocumentSnapshot document = task.getResult();
                                coachesID = (ArrayList<String>) document.get("coachesList");
                                temp.put("coach", coachesID);
                            }
                            finalList.setValue(temp);
                        }
                    }
                });
            }
        });

        return finalList;
    }
}
