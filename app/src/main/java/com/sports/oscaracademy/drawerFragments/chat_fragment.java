package com.sports.oscaracademy.drawerFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.sports.oscaracademy.R;
import com.sports.oscaracademy.adapters.chat_profile_adapter;
import com.sports.oscaracademy.data.Studentdata;
import com.sports.oscaracademy.databinding.FragmentChatBinding;
import com.sports.oscaracademy.service.studentsList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class chat_fragment extends Fragment {
    FragmentChatBinding binding;
    SharedPreferences preferences;
    studentsList service;

    public chat_fragment() {
        // Required empty public constructor
    }

    public static chat_fragment newInstance() {
        return new chat_fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = requireActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        service = new studentsList(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false);
        Map<String, ArrayList<String>> chatusers = new HashMap<>();
        final ArrayList[] chatStudentsList = new ArrayList[]{new ArrayList<>()};

        String role = preferences.getString("userType", "0");
        Log.e("role", "onCreateView: " + role);
        if (preferences.getString("isStudent", "false").equals("true") && role.equals("1")) {
            binding.textView5.setText("CHAT WITH FACULTY");
            service.getchatusers().observeForever(new Observer<Map<String, ArrayList<String>>>() {
                @Override
                public void onChanged(Map<String, ArrayList<String>> stringArrayListMap) {
                    Log.e("TAG", "onChanged:map " + stringArrayListMap.get("coach"));
                    showAllAdminAndCoaches(stringArrayListMap);//list of coach and admin ID is here
                }
            });

        }
        if (role.equals("-2")) {
            binding.textView5.setText("CHAT WITH STUDENT");
            service.getStudents().observe(requireActivity(), new Observer<ArrayList<Studentdata>>() {
                @Override
                public void onChanged(ArrayList<Studentdata> studentdata) {
                    showStudents(studentdata);
                    Log.e("TAG", "onChanged: student size " + studentdata.size());
                }
            });
        }
        return binding.getRoot();
    }

    private void showStudents(ArrayList<Studentdata> studentdata) {
        chat_profile_adapter adapter = new chat_profile_adapter();
        binding.rcvProfiles.setAdapter(adapter);
        adapter.setData(studentdata);
    }

    private void showAllAdminAndCoaches(Map<String, ArrayList<String>> chatusers) {
        ArrayList<String> adminID = chatusers.get("admin");
        ArrayList<String> coachesID = chatusers.get("coach");
        service.getUsers().observe(getActivity(), new Observer<ArrayList<Studentdata>>() {
            @Override
            public void onChanged(ArrayList<Studentdata> studentdata) {
                ArrayList<Studentdata> admin = new ArrayList<>();
                ArrayList<Studentdata> coach = new ArrayList<>();
                for (Studentdata data : studentdata) {
                    if (adminID != null) {
                        if (adminID.contains(data.getUserId())) {
                            Log.e("TAG", "onChanged: " + data.getName());
                            admin.add(data);
                            adminID.remove(data.getUserId());
                        }
                    }
                    if (coachesID != null) {
                        if (coachesID.contains(data.getUserId())) {
                            coach.add(data);
                            Log.e("TAG", "onChanged: " + data.getName());
                            coachesID.remove(data.getUserId());
                        }
                    }
                }
                chat_profile_adapter adapter = new chat_profile_adapter();
                binding.rcvProfiles.setAdapter(adapter);
                adapter.setData(coach, admin);
            }
        });
    }
}