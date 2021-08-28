package com.sports.oscaracademy.chatService;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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

    public void getuserType() {
        Log.e("TAG", "getuserType: " + "called");
        String currentuserID = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance().collection("userType_private").document(currentuserID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        SharedPreferences.Editor pref = getContext().getSharedPreferences("tokenFile", Context.MODE_PRIVATE).edit();
                        if (documentSnapshot.get("role").equals("Student_dashboard")) {
                            pref.putString("userType", "1");
                            userCategory.setValue("student");
                        }
                        if (documentSnapshot.get("role").equals("admin_dashboard")) {
                            pref.putString("userType", "-2");
                            userCategory.setValue("admin");
                        }
                        pref.apply();
                        Log.e("TAG", "getuserType: " + "called" + documentSnapshot.get("role").equals("admin_dashboard"));

                    }
                });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false);
        Map<String, ArrayList<String>> chatusers = new HashMap<>();
        final ArrayList[] chatStudentsList = new ArrayList[]{new ArrayList<>()};
        getuserType();
        Sprite doubleBounce = new WanderingCubes();
        binding.progress.setIndeterminateDrawable(doubleBounce);
        binding.progress.setVisibility(View.VISIBLE);

        userCategory.observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (preferences.getString("isStudent", "false").equals("true") && s.equals("student")) {
                    binding.textView5.setText("CHAT WITH FACULTY");
                    service.getchatusers().observeForever(new Observer<Map<String, ArrayList<String>>>() {
                        @Override
                        public void onChanged(Map<String, ArrayList<String>> stringArrayListMap) {
                            Log.e("TAG", "onChanged:map " + stringArrayListMap.get("coach"));
                            showAllAdminAndCoaches(stringArrayListMap);//list of coach and admin ID is here
                        }
                    });
                } else if (s.equals("admin")) {
                    binding.textView5.setText("CHAT WITH STUDENT");
                    service.getStudents().observe(requireActivity(), new Observer<ArrayList<Studentdata>>() {
                        @Override
                        public void onChanged(ArrayList<Studentdata> studentdata) {
                            showStudents(studentdata);
                            Log.e("TAG", "onChanged: student size " + studentdata.size());
                        }
                    });
                } else
                    binding.progress.setVisibility(View.GONE);
            }
        });
        return binding.getRoot();
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
        service.getUsers().observe(getActivity(), new Observer<ArrayList<Studentdata>>() {
            @Override
            public void onChanged(ArrayList<Studentdata> studentdata) {
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
            }

        });
    }
}