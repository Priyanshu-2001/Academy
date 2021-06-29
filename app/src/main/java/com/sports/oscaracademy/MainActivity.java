package com.sports.oscaracademy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {

        Handler mHandeler = new Handler();
        mHandeler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
                if(currentUser==null){
                    i = new Intent(MainActivity.this,LoginActivity.class);
                }else{
                    i = new Intent(MainActivity.this , Dashboard.class);
                }

                startActivity(i);
                finish();
            }
        },2000);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}