package com.sports.oscaracademy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.sports.oscaracademy.data.loginData;
import com.sports.oscaracademy.databinding.ActivityLoginBinding;
import com.sports.oscaracademy.viewModel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding ;
    private LoginViewModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_login);
        model = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewmodel(model);

        model.getUser().observe(this, new Observer<loginData>() {
            @Override
            public void onChanged(loginData loginData) {
                login(loginData);
            }
        });
    }

    private void login(loginData loginData) {

        if (loginData.getPassword() != null && loginData.getPassword() != null) {

            if (!loginData.getUserName().isEmpty() && !loginData.getPassword().isEmpty()) {
                Log.e("TAG", "onChanged: username " + loginData.getUserName());
                Log.e("TAG", "onChanged: pass " + loginData.getPassword());
//                service = new loginService(login_data, LoginActivity.this);

            } else if (!loginData.getUserName().isEmpty() && loginData.getPassword().isEmpty()) {
                binding.editPass.setError("Enter Password");
            } else if (loginData.getUserName().isEmpty() && !loginData.getPassword().isEmpty()) {
                binding.editUsername.setError("Enter Username");
            } else {
                binding.editUsername.setError("Enter Username");
                binding.editPass.setError("Enter Password");
            }
        }
        else{
            if(loginData.getPassword() == null){
                binding.editPass.setError("Enter Password");
            }
            if(loginData.getUserName() == null) {
                binding.editUsername.setError("Enter Username");
            }
        }
    }
}