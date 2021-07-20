package com.sports.oscaracademy.drawerFragments;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String UID = "param1";
    private String userID;
    public ProfileFragment() {
        // Required empty public constructor
    }
    public static ProfileFragment newInstance(String userID) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(UID,userID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userID = getArguments().getString(UID);
        }
    }
//    private FragmentProfileBinding binding ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentProfileBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile,container,false);


        studentsList list = new studentsList(getActivity());
        list.getStudents().observe(requireActivity(), new Observer<ArrayList<Studentdata>>() {
            @Override
            public void onChanged(ArrayList<Studentdata> studentdata) {
                for(int i=0 ; i<studentdata.size();i++){
                    if(studentdata.get(i).getUserId().equals(userID)){
                        Log.e("TAG", "onChanged: "+studentdata.get(i).getUserId());
                        Log.e("TAG", "onChanged: "+studentdata.get(i).getName());
                        binding.setModel(studentdata.get(i));
                    }
                }
            }
        });

        return binding.getRoot();
    }


}