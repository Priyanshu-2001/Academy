package com.sports.oscaracademy.drawerFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sports.oscaracademy.R;
import com.sports.oscaracademy.data.Studentdata;
import com.sports.oscaracademy.databinding.FragmentProfileBinding;
import com.sports.oscaracademy.service.studentsList;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private static final String UID = "param1";
    private static final String EDITABLE = "editable";
    private String userID;
    private String editable = "false";
    private SharedPreferences prefs;

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

    //    private FragmentProfileBinding binding ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentProfileBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);

        if (editable.equals("false")) { // list of present student is shown
            binding.addStudent.setVisibility(View.GONE);
        }
        if (editable.equals("true")) { // if its in add student section i.e. users list is shown
            binding.deleteStudent.setVisibility(View.GONE);
        }
        if (editable.equals("user")) { //if clicked in profile
            binding.addStudent.setVisibility(View.GONE);
            binding.deleteStudent.setVisibility(View.GONE);
            binding.savebtn.setVisibility(View.GONE);
        }

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
        //editable showing
        String isStudent = prefs.getString("isStudent", "false");
        Log.e("TAG", "onCreateView: "+ isStudent );
        studentsList list = new studentsList(getActivity());
        String role = prefs.getString("role","0");
        if ((isStudent.equals("true") || role.equals("1")) && editable.equals("false")) {
            list.getStudents().observe(requireActivity(), new Observer<ArrayList<Studentdata>>() {
                @Override
                public void onChanged(ArrayList<Studentdata> studentdata) {
                    Log.d("TAG", "onChanged: req " + userID);
                    for (int i = 0; i < studentdata.size(); i++) {
                        Log.d("TAG", "onChanged: got " +studentdata.get(i).getUserId() );
                        if (studentdata.get(i).getUserId().equals(userID)) {
                            Log.e("TAG", "onChanged: " + studentdata.get(i).getUserId());
                            Log.e("TAG", "onChanged: " + studentdata.get(i).getName());
                            binding.setModel(studentdata.get(i));
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
                        Log.d("TAG", "onChanged: got " +studentdata.get(i).getUserId() );

                        if (studentdata.get(i).getUserId().equals(userID)) {
                            Log.e("TAG", "onChanged: " + studentdata.get(i).getUserId());
                            Log.e("TAG", "onChanged: " + studentdata.get(i).getName());
                            binding.setModel(studentdata.get(i));
                        }
                    }
                }
            });
        }
        return binding.getRoot();
    }
}