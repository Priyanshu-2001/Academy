package com.sports.oscaracademy.chat_feature;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sports.oscaracademy.R;
import com.sports.oscaracademy.adapters.chat_profile_adapter;
import com.sports.oscaracademy.data.Studentdata;
import com.sports.oscaracademy.databinding.FragmentChatBinding;
import com.sports.oscaracademy.service.studentsList;

import java.util.ArrayList;
import java.util.Map;

public class chat_fragment extends Fragment {
    FragmentChatBinding binding;
    SharedPreferences preferences;
    studentsList service;
    MutableLiveData<String> userCategory = new MutableLiveData<>();

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

    public void getUserType() {
        Log.e("TAG", "getUserType: " + "called");
        String currentuserID = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore store = FirebaseFirestore.getInstance();
        SharedPreferences.Editor pref = preferences.edit();
        store.collection("chatResponders")
                .document("coaches")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<String> coachList = (ArrayList<String>) task.getResult().get("coachesList");
                        if (coachList.contains(currentuserID)) {
                            pref.putString("userType", "-2");
                            pref.apply();
                            userCategory.setValue("coach");
                            Toast.makeText(getContext(), "welcome back Coach", Toast.LENGTH_SHORT).show();
                        } else {
                            store.collection("chatResponders")
                                    .document("Admin")
                                    .get()
                                    .addOnCompleteListener(it ->
                                            {
                                                if (it.isSuccessful()) {
                                                    ArrayList<String> adminList = (ArrayList<String>) it.getResult().get("adminID");
                                                    Log.e("TAG", "getUserType adminList " + adminList);
                                                    Log.e("TAG", "getUserType adminList " + currentuserID);
                                                    if (adminList.contains(currentuserID)) {
                                                        pref.putString("userType", "-2");
                                                        pref.apply();
                                                        userCategory.setValue("admin");
                                                        Toast.makeText(getContext(), "welcome back Admin", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        pref.putString("userType", "1");
                                                        pref.apply();
                                                        userCategory.setValue("student");
                                                        Toast.makeText(getContext(), "welcome back Student", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                    );
                        }
                        pref.apply();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false);
        getUserType();
        Sprite doubleBounce = new WanderingCubes();
        binding.progress.setIndeterminateDrawable(doubleBounce);
        binding.progress.setVisibility(View.VISIBLE);

        userCategory.observe(this, s -> {
            if (preferences.getString("isStudent", "false").equals("true") && s.equals("student")) {
                binding.textView5.setText("CHAT WITH FACULTY");
                service.getchatusers().observe(chat_fragment.this, new Observer<Map<String, ArrayList<String>>>() {
                    @Override
                    public void onChanged(Map<String, ArrayList<String>> stringArrayListMap) {
                        showAllAdminAndCoaches(stringArrayListMap);//list of coach and admin ID is here
                    }
                });
            }
            if (s.equals("admin") || s.equals("coach")) {
                binding.textView5.setText("CHAT WITH STUDENT");
                service.getStudents().observe(requireActivity(), studentdata -> {
                    showStudents(studentdata);
                    Log.e("TAG", "onChanged: student size " + studentdata.size());
                });
            }
            if (preferences.getString("isStudent", "false").equals("false") && s.equals("student")) {
                binding.textView5.setText("CHAT WITH FACULTY");
                service.getchatusers().observe(chat_fragment.this, new Observer<Map<String, ArrayList<String>>>() {
                    @Override
                    public void onChanged(Map<String, ArrayList<String>> stringArrayListMap) {
                        Log.e("TAG", "onChanged:map " + stringArrayListMap.get("coach"));
                        showAllAdmin(stringArrayListMap);//list of coach and admin ID is here
                    }
                });
            }
        });

        String temp = preferences.getString("isStudent", "false");
        Toast.makeText(getContext(), temp, Toast.LENGTH_SHORT).show();
        binding.progress.setVisibility(View.GONE);
        return binding.getRoot();
    }

    private void showAllAdmin(Map<String, ArrayList<String>> chatusers) {
        ArrayList<String> adminID = chatusers.get("admin");
        ArrayList<String> coachesID = chatusers.get("coach");
        service.getUsers().observe(this, studentdata -> {
            ArrayList<Studentdata> admin = new ArrayList<>();
            ArrayList<Studentdata> coach = new ArrayList<>();
            for (Studentdata data : studentdata) {
                if (adminID != null) {
                    if (adminID.contains(data.getUserId())) {
                        admin.add(data);
                        Log.e("TAG", "onChanged: adminID" + data.getUserId());
                        adminID.remove(data.getUserId());
                    }
                }
                if (coachesID != null) {
//                    if (coachesID.contains(data.getUserId())) {
//                        coach.add(data);
//                        Log.e("TAG", "onChanged: coachID" + data.getUserId());
//                        coachesID.remove(data.getUserId());
//                    }
                }
            }
            chat_profile_adapter adapter = new chat_profile_adapter(getContext());
            binding.rcvProfiles.setAdapter(adapter);
            adapter.setData(coach, admin);
            binding.progress.setVisibility(View.GONE);
        });
    }

    private void showStudents(ArrayList<Studentdata> studentdata) {
        chat_profile_adapter adapter = new chat_profile_adapter(getContext());
        binding.rcvProfiles.setAdapter(adapter);
        adapter.setData(studentdata);
        binding.progress.setVisibility(View.GONE);
    }

    private void showAllAdminAndCoaches(Map<String, ArrayList<String>> chatusers) {
        ArrayList<String> adminID = chatusers.get("admin");
        ArrayList<String> coachesID = chatusers.get("coach");
        service.getUsers().observe(this, studentdata -> {
            ArrayList<Studentdata> admin = new ArrayList<>();
            ArrayList<Studentdata> coach = new ArrayList<>();
            for (Studentdata data : studentdata) {
                if (adminID != null) {
                    if (adminID.contains(data.getUserId())) {
                        admin.add(data);
                        Log.e("TAG", "onChanged: adminID" + data.getUserId());
                        adminID.remove(data.getUserId());
                    }
                }
                if (coachesID != null) {
                    if (coachesID.contains(data.getUserId())) {
                        coach.add(data);
                        Log.e("TAG", "onChanged: coachID" + data.getUserId());
                        coachesID.remove(data.getUserId());
                    }
                }
            }
            chat_profile_adapter adapter = new chat_profile_adapter(getContext());
            binding.rcvProfiles.setAdapter(adapter);
            adapter.setData(coach, admin);
            binding.progress.setVisibility(View.GONE);
        });
    }
}