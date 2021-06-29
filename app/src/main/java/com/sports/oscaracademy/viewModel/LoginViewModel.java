package com.sports.oscaracademy.viewModel;

import android.app.Application;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.sports.oscaracademy.R;
import com.sports.oscaracademy.data.loginData;
import com.sports.oscaracademy.signUpActivity;

public class LoginViewModel extends AndroidViewModel {
    private loginData data ;
    public MutableLiveData<loginData> loginData = new MutableLiveData<>();
    public MutableLiveData<String> userName = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    private FirebaseAuth mAuth ;
    private Application app;
    public LoginViewModel(Application application) {
        super(application);
        app = application;
        mAuth = FirebaseAuth.getInstance();
    }

    public MutableLiveData<loginData> getUser() {

        if (loginData == null) {
            loginData= new MutableLiveData<>();
        }
        return loginData;
    }

    public void onClick(View v){
        data = new loginData(userName.getValue(),password.getValue());
        loginData.setValue(data);
    }
    public void onClickSignUp(View v){
        v.getContext().startActivity(new Intent(v.getContext(),signUpActivity.class));
    }
    public void googleSignIn(View v){

    }
    public void processSignIn(View v){
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(v.getContext().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }
}
