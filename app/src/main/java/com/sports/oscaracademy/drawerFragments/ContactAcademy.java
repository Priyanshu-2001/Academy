package com.sports.oscaracademy.drawerFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.sports.oscaracademy.Application.MyApplication;
import com.sports.oscaracademy.R;
import com.sports.oscaracademy.chat_feature.chat_fragment;
import com.sports.oscaracademy.databinding.FragmentContactAcademyBinding;

public class ContactAcademy extends Fragment {

    private final MutableLiveData<Long> phoneNumber = new MutableLiveData<>();
    private final MutableLiveData<String> email = new MutableLiveData<>();
    private FirebaseRemoteConfig remoteConfig;

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
    }

    FragmentContactAcademyBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact_academy, container, false);
        getParentFragmentManager().beginTransaction().add(R.id.chatsystem, chat_fragment.newInstance()).commit();
        remoteConfig = ((MyApplication) (requireActivity().getApplication())).getRemoteConfig1();
        phoneNumber.setValue(remoteConfig.getValue("academyContactNumber").asLong());
        email.setValue(remoteConfig.getValue("academyContactEmail").asString());

        phoneNumber.observe(this, aLong -> {
            binding.phoneNumber.setText("Contact Us At \n" + aLong.toString());
        });
        email.observe(this, email -> binding.academyEmail.setText("Mail us at \n" + email));
        return binding.getRoot();
    }
}