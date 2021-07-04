package com.sports.oscaracademy.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class HomeModel extends AndroidViewModel {
    public HomeModel(@NonNull @NotNull Application application) {
        super(application);
        mAuth = FirebaseAuth.getInstance();
        userid.setValue(mAuth.getCurrentUser().getUid());
        useremail.setValue(mAuth.getCurrentUser().getEmail());
        username.setValue(mAuth.getCurrentUser().getDisplayName());
//        photo.setValue(mAuth.getCurrentUser().getPhotoUrl().toString());
        phone.setValue(mAuth.getCurrentUser().getPhoneNumber().toString());
    }
    public FirebaseAuth mAuth;
    public MutableLiveData<String> userid = new MutableLiveData<>();
    public MutableLiveData<String> useremail = new MutableLiveData<>();
    public MutableLiveData<String> username = new MutableLiveData<>();
    public MutableLiveData<String> photo = new MutableLiveData<>();
    public MutableLiveData<String> phone = new MutableLiveData<>();

}
