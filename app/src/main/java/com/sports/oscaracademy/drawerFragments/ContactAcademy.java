package com.sports.oscaracademy.drawerFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.sports.oscaracademy.R;
import com.sports.oscaracademy.chatService.chat_fragment;
import com.sports.oscaracademy.databinding.FragmentContactAcademyBinding;

public class ContactAcademy extends Fragment {

    // TODO: Rename and change types of parameters
    public ContactAcademy() {
        // Required empty public constructor
    }

    public static ContactAcademy newInstance() {
        ContactAcademy fragment = new ContactAcademy();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    FragmentContactAcademyBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact_academy, container, false);
        getFragmentManager().beginTransaction().add(R.id.chatsystem, chat_fragment.newInstance()).commit();
//        getChildFragmentManager().beginTransaction().replace(R.id.chatsystem,chat_fragment.newInstance()).commit();
        return binding.getRoot();
    }
}