package com.sports.oscaracademy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sports.oscaracademy.dialog.dialogs;
import com.sports.oscaracademy.drawerFragments.ContactAcademy;
import com.sports.oscaracademy.drawerFragments.Home_fragment;
import com.sports.oscaracademy.drawerFragments.ProfileFragment;

import org.jetbrains.annotations.NotNull;

public class Dashboard extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DrawerLayout drawer;
    private NavigationView navView;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        com.sports.oscaracademy.databinding.ActivityDashboardBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);


        binding.setLifecycleOwner(Dashboard.this);
        Toolbar toolbar = binding.toolbar;
        drawer = binding.drawer;
        navView = binding.navLayout;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navView.getMenu().getItem(0).setChecked(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, Home_fragment.newInstance()).commit();

        View header = navView.getHeaderView(0);
        ImageView imageView = header.findViewById(R.id.pImage);
        TextView name = header.findViewById(R.id.pName);
        TextView profileEmail = header.findViewById(R.id.Profile_email);
        SharedPreferences pref = getSharedPreferences("tokenFile",MODE_PRIVATE);
        if(pref.getString("name", "NA").equals("NA")){

            firestore.collection("user").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        String tempName ;
                        tempName = task.getResult().getString("name");
                        name.setText(tempName);
                    }else {
                        name.setText("NAME NOT AVAILABLE");
                    }
                }
            });
        }else{
            name.setText(pref.getString("name", "NA"));
        }
        profileEmail.setText(pref.getString("email","NA"));
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

        CheckforStudent();

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                Fragment temp = null;
                switch (item.getItemId()) {
                    case R.id.Home:
                        Toast.makeText(Dashboard.this, "Home", Toast.LENGTH_SHORT).show();
                        temp = Home_fragment.newInstance();
                        navView.getMenu().getItem(0).setChecked(true);
                        drawer.closeDrawer(GravityCompat.START);
                        getSupportActionBar().setTitle("");
                        break;
                    case R.id.meet_coach:
                        temp = ContactAcademy.newInstance();
                        getSupportActionBar().setTitle("Contact Us");
                        navView.getMenu().getItem(1).setChecked(true);
                        Toast.makeText(Dashboard.this, "Meeting Coach", Toast.LENGTH_SHORT).show();
                        drawer.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.Log_out:
                        mAuth.signOut();
                        finishAffinity();
                        startActivity(new Intent(Dashboard.this, LoginActivity.class));
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                }
                if (temp != null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, temp).commit();
                else {
                    if (item.getItemId() == R.id.privacy_policy) {
                        navView.getMenu().getItem(2).setChecked(true);
                        Toast.makeText(Dashboard.this, "Privacy is Important :)", Toast.LENGTH_SHORT).show();
                        drawer.closeDrawer(GravityCompat.START);
                    }
                }
                return true;
            }
        });
    }

    private void CheckforStudent() {
        firestore.collection("user").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String isStudent = documentSnapshot.getString("isStudent");
                SharedPreferences.Editor pref = getSharedPreferences("tokenFile",MODE_PRIVATE).edit();
                pref.putString("isStudent",isStudent);
                if (isStudent.equals("true")){
                    getRollNo();
                }else{
                    pref.putString("roll","-1");
                }
                pref.apply();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                dialogs dialog = new dialogs();
                dialog.displayDialog(e.getLocalizedMessage(),getApplicationContext());
            }
        });
    }

    private void getRollNo() {
        firestore.collection("students").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String rollNo = documentSnapshot.getString("RollNo");
                SharedPreferences.Editor pref = getSharedPreferences("tokenFile",MODE_PRIVATE).edit();
                pref.putString("roll",rollNo);
                pref.apply();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                dialogs dialog = new dialogs();
                dialog.displayDialog(e.getLocalizedMessage(),getApplicationContext());
            }
        });
    }

    private void openProfile() {
        try {
            navView.getCheckedItem().setChecked(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        drawer.closeDrawer(GravityCompat.START);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, ProfileFragment.newInstance(mAuth.getUid(),"user")).addToBackStack(null).commit();
    }
}