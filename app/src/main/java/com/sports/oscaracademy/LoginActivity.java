package com.sports.oscaracademy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sports.oscaracademy.data.loginData;
import com.sports.oscaracademy.databinding.ActivityLoginBinding;
import com.sports.oscaracademy.viewModel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel model;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    SignInButton gSignIn;
    private static final int RC_SIGN_IN = 1002;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        model = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewmodel(model);
        gSignIn = binding.btnParentLayout;

        mAuth = FirebaseAuth.getInstance();
        processSignIn();
        gSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLogin();
            }
        });
//        model.gSignIn.observe(this, new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean b) {
//                if(b){
//                    processLogin();
//                    model.gSignIn.setValue(false);
//                }
//            }
//        });
        model.getUser().observe(this, new Observer<loginData>() {
            @Override
            public void onChanged(loginData loginData) {
                login(loginData);
            }
        });

    }


    private void processLogin() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void processSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Login Sucessful", Toast.LENGTH_SHORT).show();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
//                            updateUI(null);
                        }
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
        } else {
            if (loginData.getPassword() == null) {
                binding.editPass.setError("Enter Password");
            }
            if (loginData.getUserName() == null) {
                binding.editUsername.setError("Enter Username");
            }
        }
    }
}