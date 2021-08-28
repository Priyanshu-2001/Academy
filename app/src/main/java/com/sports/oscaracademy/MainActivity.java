package com.sports.oscaracademy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    SharedPreferences.Editor prefEditor;
    Intent i;

    @Override
    protected void onStart() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        updateUI(currentUser);

    }

    private void updateUI(FirebaseUser currentUser) {
//        getWindow().setStatusBarColor(getColor(R.color.app_compat_light));
        Handler mHandeler = new Handler();
        mHandeler.postDelayed(new Runnable() {
            @Override
            public void run() {
                prefEditor = getSharedPreferences("tokenFile", MODE_PRIVATE).edit();
                prefEditor.putBoolean("isAppOpened", true);
                prefEditor.commit();
                if (currentUser != null) {
                    if (currentUser.isEmailVerified()) {
                        i = new Intent(MainActivity.this, Dashboard.class);
                        if (getIntent().getExtras() != null) {
                            try {
                                i.putExtra("notification", true);
                                Log.e("TAG", "run: getintent main " + getIntent().getExtras().getBoolean("notification"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else{
                        i = new Intent(MainActivity.this, EmailVerification.class);
                        i.putExtra("email" ,currentUser.getEmail());
                    }
                } else {
                        i = new Intent(MainActivity.this, LoginActivity.class);
                }

                startActivity(i);
                finish();
            }
        }, 2000);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}