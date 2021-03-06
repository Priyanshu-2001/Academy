package com.sports.oscaracademy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sports.oscaracademy.bottomSheet.bottomSheetOtpVerification;
import com.sports.oscaracademy.dialog.dialogs;
import com.sports.oscaracademy.drawerFragments.ContactAcademy;
import com.sports.oscaracademy.drawerFragments.Home_fragment;
import com.sports.oscaracademy.drawerFragments.ProfileFragment;
import com.sports.oscaracademy.drawerFragments.verifyContactDetails;

import org.jetbrains.annotations.NotNull;

public class Dashboard extends AppCompatActivity implements bottomSheetOtpVerification.sendOTP {
    private FirebaseAuth mAuth;
    private DrawerLayout drawer;
    private NavigationView navView;
    private FirebaseFirestore firestore;
    final Home_fragment HomeFragment = Home_fragment.newInstance();
    final verifyContactDetails verifyContactFragment = verifyContactDetails.newInstance();
    final ContactAcademy contactAcademy = ContactAcademy.newInstance();
    public Fragment temp = HomeFragment;
    FragmentManager manager = getSupportFragmentManager();
    ProfileFragment profileFragment;
    String otp;
    String currentuserID;
    private boolean homeFrag;
    SharedPreferences.Editor prefEditor;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        currentuserID = mAuth.getUid();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        com.sports.oscaracademy.databinding.ActivityDashboardBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        prefEditor = getSharedPreferences("tokenFile", MODE_PRIVATE).edit();
        pref = getSharedPreferences("tokenFile", MODE_PRIVATE);
        binding.setLifecycleOwner(Dashboard.this);
        Toolbar toolbar = binding.toolbar;
        drawer = binding.drawer;
        FirebaseMessaging.getInstance().subscribeToTopic("feeds").addOnSuccessListener(command -> {
            Log.e("TAG", "onCreate: " + "subscribed sucessfully");
        });
        navView = binding.navLayout;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getColor(R.color.white));

        navView.getMenu().getItem(0).setChecked(true);
        Log.e("TAG", "onCreate: uid " + mAuth.getUid());
        View header = navView.getHeaderView(0);
        ImageView imageView = header.findViewById(R.id.pImage);
        TextView name = header.findViewById(R.id.pName);
        TextView profileEmail = header.findViewById(R.id.Profile_email);
        SharedPreferences pref = getSharedPreferences("tokenFile", MODE_PRIVATE);
        if (pref.getString("name", "NA").equals("NA")) {
            firestore.collection("user").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        String tempName;
                        tempName = task.getResult().getString("name");
                        name.setText(tempName);
                    } else {
                        name.setText("NAME NOT AVAILABLE");
                    }
                }
            });
        } else {
            name.setText(pref.getString("name", "NA"));
        }
        profileEmail.setText(pref.getString("email", "NA"));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile();
            }
        });
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile();
            }
        });

        homeFrag = true;
        if (pref.getInt("roll", -1) == -1) {
            CheckforStudent();
        }
        manager.beginTransaction().add(R.id.frameContainer, HomeFragment, "home").commit();
        manager.beginTransaction().add(R.id.frameContainer, verifyContactFragment, "verify").hide(verifyContactFragment).commit();
        manager.beginTransaction().add(R.id.frameContainer, contactAcademy, "contactUs").hide(contactAcademy).commit();
//        manager.beginTransaction().add(R.id.frameContainer, profileFragment, "profile").hide(profileFragment).commit();
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.Home:
                        Log.e("TAG", "onNavigationItemSelected: " + temp);
                        manager.beginTransaction().hide(temp).show(HomeFragment).commit();
                        temp = HomeFragment;
                        navView.getMenu().getItem(0).setChecked(true);
                        getSupportActionBar().setTitle("");
                        break;

                    case R.id.UpdatephoneNumber:
                        Log.e("TAG", "onNavigationItemSelected: " + temp);
                        manager.beginTransaction().hide(temp).show(verifyContactFragment).commit();
                        navView.getMenu().getItem(2).setChecked(true);
                        temp = verifyContactFragment;
                        getSupportActionBar().setTitle("");
                        break;
                    case R.id.meet_coach:
                        Log.e("TAG", "onNavigationItemSelected: " + temp);
                        manager.beginTransaction().hide(temp).show(contactAcademy).commit();
                        temp = contactAcademy;
                        getSupportActionBar().setTitle("Contact Us");
                        navView.getMenu().getItem(1).setChecked(true);
                        break;

                    case R.id.privacy_policy:
                        navView.getMenu().getItem(3).setChecked(true);
                        Toast.makeText(Dashboard.this, "Privacy is Important :)", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.Log_out:
                        FirebaseDatabase.getInstance().getReference().child("presence").child(mAuth.getUid()).setValue("offline");
                        mAuth.signOut();
                        pref.edit().clear().apply();
                        startActivity(new Intent(Dashboard.this, LoginActivity.class));
                        finishAffinity();
                        break;
                }
                homeFrag = temp instanceof Home_fragment;
//                homeFrag = temp instanceof Home_fragment;
                Log.e("TAG", "onNavigationItemSelected: " + temp);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        FirebaseDatabase.getInstance().getReference().child("presence").child(currentuserID).setValue("Online");
        super.onResume();
        if (getIntent().getExtras() != null) {
            Log.e("TAG", "onResume: " + "getintent not null");
            try {
                Log.e("TAG", "onResume: " + "getIntent" + getIntent().getExtras().getBoolean("notification"));
                if (getIntent().getExtras().getBoolean("notification")) {
                    manager.beginTransaction().hide(temp).show(contactAcademy).commit();
                    temp = contactAcademy;
                    homeFrag = false;
                    getIntent().removeExtra("notification");
                }
//                Log.e("Notification ", "sendNotification: dashOut"+ getIntent().getExtras().getBoolean("isFeed") );
//
//                if(getIntent().getExtras().getBoolean("isFeed")){
//                    getIntent().removeExtra("isFeed");
//                    Log.e("TAG", "onResume: service called" );
//                    Intent i = new Intent(this, news_feeds.class);
//                    startActivity(i);
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPause() {
        prefEditor.putBoolean("isAppOpened", false);
        FirebaseDatabase.getInstance().getReference().child("presence").child(currentuserID).setValue("offline");
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        prefEditor.putBoolean("isAppOpened", false);
        FirebaseDatabase.getInstance().getReference().child("presence").child(currentuserID).setValue("offline");
        super.onDestroy();
    }

    private void CheckforStudent() {
        firestore.collection("user").document(currentuserID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String isStudent = documentSnapshot.getString("isStudent");
                SharedPreferences.Editor pref = getSharedPreferences("tokenFile", MODE_PRIVATE).edit();
                pref.putString("isStudent", isStudent);
                if (isStudent.equals("true")) {
                    getRollNo();
                } else {
                    pref.putInt("roll", -1);
                }
                pref.apply();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                dialogs dialog = new dialogs();
                dialog.displayDialog(e.getLocalizedMessage(), getApplicationContext());
            }
        });
    }

    private void getRollNo() {
        firestore.collection("students").document(currentuserID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                Integer rollNo = documentSnapshot.get("RollNo", Integer.class);
                String Session = documentSnapshot.getString("session");
                SharedPreferences.Editor pref = getSharedPreferences("tokenFile", MODE_PRIVATE).edit();
                pref.putInt("roll", rollNo);
                pref.putString("session", Session);
                pref.apply();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                dialogs dialog = new dialogs();
                dialog.displayDialog(e.getLocalizedMessage(), getApplicationContext());
            }
        });
    }

    private void openProfile() {
        try {
            navView.getCheckedItem().setChecked(false);
            profileFragment = ProfileFragment.newInstance(mAuth.getUid(), "user");
            manager.beginTransaction().add(R.id.frameContainer, profileFragment, "profile").hide(temp).commit();
            manager.beginTransaction().show(profileFragment).commit();
            temp = profileFragment;
            homeFrag = false;
            Log.e("TAG", "openProfile: " + temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        Log.e("TAG", "onBackPressed: " + homeFrag);
        // Close navigation drawer if open
        if (drawer.isOpen()) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!homeFrag) {
                SetHomeAsPrimary();
            } else {
                super.onBackPressed();
                finishAffinity();
            }
        }
    }


    private void SetHomeAsPrimary() {
        manager.beginTransaction().show(HomeFragment).hide(temp).commit();
        temp = HomeFragment;
        homeFrag = true;
        navView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void getOTP(String OTP) {
        otp = OTP;
        ((verifyContactDetails) temp).otpReciever(OTP);
    }
}