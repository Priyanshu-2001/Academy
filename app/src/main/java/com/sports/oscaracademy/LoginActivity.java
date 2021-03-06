package com.sports.oscaracademy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sports.oscaracademy.data.loginData;
import com.sports.oscaracademy.databinding.ActivityLoginBinding;
import com.sports.oscaracademy.dialog.dialogs;
import com.sports.oscaracademy.viewModel.LoginViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel model;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    SignInButton gSignIn;
    private static final int RC_SIGN_IN = 1002;
    dialogs dialog = new dialogs();
    private ProgressDialog progressDialog;
    FirebaseFirestore store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        model = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewmodel(model);
        gSignIn = binding.btnParentLayout;
        progressDialog = new ProgressDialog(LoginActivity.this,R.style.AlertDialog);
        mAuth = FirebaseAuth.getInstance();
        store = FirebaseFirestore.getInstance();
        processSignIn();
        gSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLogin();
            }
        });
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
                dialog.alertDialogLogin(progressDialog,"Signing In....");
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
                        dialog.dismissDialog(progressDialog);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            Log.e("TAG", "onComplete: " + task.getResult().getAdditionalUserInfo().isNewUser() );
                            if(task.getResult().getAdditionalUserInfo().isNewUser()){
                                saveInDB();
                            }else{
                                SaveToPreferences();
                                startActivity(new Intent(LoginActivity.this, Dashboard.class));
                                finishAffinity();
                            }
                            Snackbar.make(binding.getRoot(),"Login Success",BaseTransientBottomBar.LENGTH_LONG).show();
                        } else {
                            dialog.displayDialog(task.getException().getLocalizedMessage() , LoginActivity.this);
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }


    private void login(loginData loginData) {

        if (loginData.getPassword() != null && loginData.getPassword() != null) {

            if (!loginData.getUserName().isEmpty() && !loginData.getPassword().isEmpty()) {
                dialog.alertDialogLogin(progressDialog,"Signing In....");
                logUserIn(loginData);
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

    private void logUserIn(loginData loginData) {

        mAuth.signInWithEmailAndPassword(loginData.getUserName(),loginData.getPassword())
                .addOnCompleteListener(LoginActivity.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        dialog.dismissDialog(progressDialog);
                        if(task.isSuccessful()){
                            if(mAuth.getCurrentUser().isEmailVerified()) {
                                Log.d("TAG", "onComplete: " + task.getResult().getAdditionalUserInfo().isNewUser());
                                if(task.getResult().getAdditionalUserInfo().isNewUser()){
                                    saveInDB();
                                }else{
                                    SaveToPreferences();
                                    startActivity(new Intent(LoginActivity.this, Dashboard.class));
                                    finishAffinity();
                                }
                            }else{
                                mAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dialog.displayDialog("Please Verify Your Email",LoginActivity.this);
                                    }
                                });
                            }
                        }else{
                            dialog.displayDialog(task.getException().getLocalizedMessage(),LoginActivity.this);
//                            Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveInDB(){
        Map<String,String> item = new HashMap<>();
        item.put("name" ,mAuth.getCurrentUser().getDisplayName() );
        item.put("isStudent" , "false");
        item.put("email" , mAuth.getCurrentUser().getEmail());
        item.put("userID", mAuth.getCurrentUser().getUid());
        item.put("phone number", "Not Available");
        item.put("Age", "Not Available");
        item.put("Sex", "Not Available");
        item.put("DOB", "Not Available");
        store.collection("user").document(mAuth.getUid()).set(item).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Map<String, String> type = new HashMap<>();
                            type.put("role","Student_dashboard");
                            store.collection("userType_private").document(mAuth.getCurrentUser().getUid())
                                    .set(type)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            SaveToPreferences();
                                            startActivity(new Intent(LoginActivity.this, Dashboard.class));
                                            finishAffinity();
                                        }
                                    });
                        }else{
                            Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void SaveToPreferences() {
        SharedPreferences.Editor editor = getSharedPreferences("tokenFile", MODE_PRIVATE).edit();
        editor.putString("uid",mAuth.getCurrentUser().getUid());
        editor.putString("email",mAuth.getCurrentUser().getEmail());
        editor.putString("name", mAuth.getCurrentUser().getDisplayName());

        try {
            editor.putString("photoUrl", String.valueOf(mAuth.getCurrentUser().getPhotoUrl()));
            editor.putString("phoneNumber", mAuth.getCurrentUser().getPhoneNumber());
        } catch (Exception e) {
            editor.putString("photoUrl", "-1");
            editor.putString("phoneNumber", "-1");
        }
        editor.apply();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        Map<String, Object> token = new HashMap<>();
                        token.put("token", task.getResult());
                        FirebaseFirestore.getInstance()
                                .collection("token")
                                .document(FirebaseAuth.getInstance().getUid())
                                .set(token, SetOptions.merge());
                    }
                });

    }
}