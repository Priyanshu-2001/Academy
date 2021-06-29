package com.sports.oscaracademy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.sports.oscaracademy.databinding.ActivitySignUpBinding;
import com.sports.oscaracademy.viewModel.SignupViewModel;

public class signUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    SignupViewModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this ,R.layout.activity_sign_up);
        model = new ViewModelProvider(this).get(SignupViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewmodel(model);
    }
}