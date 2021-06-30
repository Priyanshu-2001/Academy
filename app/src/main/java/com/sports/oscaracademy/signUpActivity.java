package com.sports.oscaracademy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sports.oscaracademy.data.signUpdata;
import com.sports.oscaracademy.databinding.ActivitySignUpBinding;
import com.sports.oscaracademy.dialog.dialogs;
import com.sports.oscaracademy.viewModel.SignupViewModel;

import org.jetbrains.annotations.NotNull;

public class signUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    SignupViewModel model;
    FirebaseAuth mAuth;
    dialogs dialog = new dialogs();
    private ProgressDialog progressDialog;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            if(currentUser.isEmailVerified()){
                startActivity(new Intent(signUpActivity.this,Dashboard.class));
                finishAffinity();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this ,R.layout.activity_sign_up);
        model = new ViewModelProvider(this).get(SignupViewModel.class);
        binding.setLifecycleOwner(this);
        mAuth = FirebaseAuth.getInstance();
        binding.setViewmodel(model);
        progressDialog = new ProgressDialog(signUpActivity.this,R.style.AlertDialog);
        model.getUserData().observe(this, new Observer<signUpdata>() {
            @Override
            public void onChanged(signUpdata data) {
                dialog.alertDialogLogin(progressDialog,"Signing Up....");
                mAuth.createUserWithEmailAndPassword(data.getEmail(), data.getPass())
                        .addOnCompleteListener(signUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            dialog.dismissDialog(progressDialog);
                                            if (task.isSuccessful()) {
                                                Intent i =new Intent(signUpActivity.this,EmailVerification.class);
                                                i.putExtra("email" ,mAuth.getCurrentUser().getEmail());
                                                startActivity(i);
                                                finishAffinity();
                                            }else{
                                                dialog.displayDialog(task.getException().getLocalizedMessage(),signUpActivity.this);
                                            }
                                        }
                                    });

                        }else{
                            dialog.dismissDialog(progressDialog);
                            dialog.displayDialog(task.getException().getLocalizedMessage(),signUpActivity.this);
                        }
                    }
                });
            }
        });
    }
}