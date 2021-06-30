package com.sports.oscaracademy.viewModel;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sports.oscaracademy.EmailVerification;
import com.sports.oscaracademy.LoginActivity;
import com.sports.oscaracademy.data.signUpdata;

public class SignupViewModel extends AndroidViewModel {
    public SignupViewModel(@NonNull @org.jetbrains.annotations.NotNull Application application) {
        super(application);
    }
    public MutableLiveData<signUpdata> SignUpdata = new MutableLiveData<>();
    public MutableLiveData<String> userName = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> con_password = new MutableLiveData<>();
    public MutableLiveData<signUpdata> getUserData(){
        if(SignUpdata == null){
            SignUpdata = new MutableLiveData<>();
        }
        return SignUpdata;
    }
    public void signupBtnclick(View v) {
        if (!(userName.getValue() == null || con_password.getValue() == null || password.getValue() == null || email.getValue() == null)) {
            if (!(userName.getValue().isEmpty() && con_password.getValue().isEmpty() && password.getValue().isEmpty() && email.getValue().isEmpty())) {
                if (con_password.getValue().equals(password.getValue())) {
                    //do authentication with firebse
                    signUpdata data = new signUpdata(userName.getValue(), email.getValue(), con_password.getValue());
                    SignUpdata.setValue(data);
//                    v.getContext().startActivity(new Intent(v.getContext(), EmailVerification.class));
//                    ((Activity) v.getContext()).finish();
                } else {
                    Toast.makeText(v.getContext(), "Password Does Not Match", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(v.getContext(), "Please fill details propertly", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(v.getContext(), "Please fill details propertly", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickSignIn(View v) {
        v.getContext().startActivity(new Intent(v.getContext(), LoginActivity.class));
        ((Activity) v.getContext()).finishAffinity();
    }
}
