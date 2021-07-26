package com.sports.oscaracademy.drawerFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.provider.ContactsContract;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.sports.oscaracademy.R;
import com.sports.oscaracademy.data.Studentdata;
import com.sports.oscaracademy.databinding.FragmentProfileBinding;
import com.sports.oscaracademy.service.studentsList;

import org.jetbrains.annotations.NotNull;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private static final String UID = "param1";
    private static final String EDITABLE = "editable";
    private String userID;
    private String editable = "false";
    private SharedPreferences prefs;
    private MutableLiveData<Studentdata> currentStudent = new MutableLiveData<>();
    private FragmentProfileBinding binding;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    public ProfileFragment() {
    }


    public static Fragment newInstance(String userID, String editable) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(UID, userID);
        args.putString(EDITABLE, editable);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userID = getArguments().getString(UID);
            try {
                editable = getArguments().getString(EDITABLE);
            } catch (Exception e) {
                editable = "false";
            }
        }
        prefs = requireActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("Profile", "onCreateView: "+editable );
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        if (editable.equals("false")) { // list of present student is shown
            binding.deleteStudent.setVisibility(View.VISIBLE);
        }
        if (editable.equals("true")) { // if its in add student sectoin i.e. users list is shown
            binding.addStudent.setVisibility(View.VISIBLE);
//                    binding.feesVaildity.setEnabled(true);
        }
        if (editable.equals("user")) { //if clicked in profile
        }

        binding.progress.setVisibility(View.GONE);
        binding.editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editbtn.setVisibility(View.GONE);
                binding.savebtn.setVisibility(View.VISIBLE);
            }
        });
        binding.savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editbtn.setVisibility(View.VISIBLE);
                binding.savebtn.setVisibility(View.GONE);
            }
        });
        binding.addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AddStudentToAcademy();
                } catch (ParseException e) {
                    Toast.makeText(getContext(), "Unable To Add Student", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        binding.deleteStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteStudents();
            }
        });

        //editable showing
        String isStudent = prefs.getString("isStudent", "false");
        Log.e("TAG", "onCreateView: " + isStudent);
        studentsList list = new studentsList(getActivity());
        String role = prefs.getString("role", "0");
        if ((isStudent.equals("true") || role.equals("1")) && editable.equals("false")) {
            list.getStudents().observe(requireActivity(), new Observer<ArrayList<Studentdata>>() {
                @Override
                public void onChanged(ArrayList<Studentdata> studentdata) {
                    Log.d("TAG", "onChanged: req " + userID);
                    for (int i = 0; i < studentdata.size(); i++) {
                        Log.d("TAG", "onChanged: got " + studentdata.get(i).getUserId());
                        if (studentdata.get(i).getUserId().equals(userID)) {
                            Log.e("TAG", "onChanged: " + studentdata.get(i).getUserId());
                            Log.e("TAG", "onChanged: " + studentdata.get(i).getName());
                            binding.setModel(studentdata.get(i));
                            currentStudent.setValue(studentdata.get(i));
                        }
                    }
                }
            });
        } else {
            list.getUsers().observe(requireActivity(), new Observer<ArrayList<Studentdata>>() {
                @Override
                public void onChanged(ArrayList<Studentdata> studentdata) {
                    Log.d("TAG", "onChanged: req " + userID);
                    for (int i = 0; i < studentdata.size(); i++) {
                        Log.d("TAG", "onChanged: got " + studentdata.get(i).getUserId());

                        if (studentdata.get(i).getUserId().equals(userID)) {
                            Log.e("TAG", "onChanged: " + studentdata.get(i).getUserId());
                            Log.e("TAG", "onChanged: " + studentdata.get(i).getName());
                            binding.setModel(studentdata.get(i));
                            currentStudent.setValue(studentdata.get(i));
                        }
                    }
                }
            });
        }
        binding.savebtn.setVisibility(View.GONE);

        currentStudent.observe(requireActivity(), new Observer<Studentdata>() {
            @Override
            public void onChanged(Studentdata studentdata) {
                binding.progress.setVisibility(View.GONE);
            }
        });
        return binding.getRoot();
    }

    private void deleteStudents() {
    }

    private void AddStudentToAcademy() throws ParseException {
        binding.progress.setVisibility(View.VISIBLE);
        Map<String,Object> ProfileData = new HashMap<>();
        ProfileData.put("Age",binding.studentAge.getText().toString().trim());
        ProfileData.put("name",binding.StudentName.getText().toString().trim());
        ProfileData.put("Email",binding.studentMail.getText().toString().trim());
        ProfileData.put("Sex",binding.studentGender.getText().toString().trim());
//        ProfileData.put("Dob",binding.studentDOB);
        ProfileData.put("phone number",binding.phoneNumber.getText().toString().trim());
        ProfileData.put("userID",currentStudent.getValue().getUserId());
        Map<String,Object> validity = new HashMap<>();
        validity.put("valid from", Timestamp.now());
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Log.e("TAG", "AddStudentToAcademy: "+String.valueOf(binding.StudentMember.getText()) );
        Date date = format.parse(String.valueOf(binding.StudentMember.getText()));
        validity.put("valid to", new Timestamp(date));
        ProfileData.put("fees",validity);
        studentsList service = new studentsList(getContext());
        service.getRoll(userID).observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ProfileData.put("RollNo", s);
               firestore.collection("students").document(userID).set(ProfileData, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Student Added SucessFully", Toast.LENGTH_SHORT).show();
                            Log.e("TAG", "onComplete: "+"validity added sucessfully" );
                        }else{
                            Log.e("TAG", "onComplete: eroor in adding student "+  task.getException());
                            Toast.makeText(getContext(), "Some Error Occured "+ task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                        binding.progress.setVisibility(View.GONE);
                    }
                });

            }
        });
    }
}