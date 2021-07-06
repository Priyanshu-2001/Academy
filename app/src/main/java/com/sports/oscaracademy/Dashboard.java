package com.sports.oscaracademy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sports.oscaracademy.adapters.dashBoard_adapter;
import com.sports.oscaracademy.data.DashBoardData;
import com.sports.oscaracademy.databinding.ActivityDashboardBinding;
import com.sports.oscaracademy.viewModel.HomeModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ActivityDashboardBinding binding;
    private HomeModel model;
    private RecyclerView rcv;
    private dashBoard_adapter adapter;
    private FirebaseFirestore db;
    private ArrayList<DashBoardData> datalist = new ArrayList<>();
    private ProgressBar progressBar;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navView;
    private ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
//        model = new ViewModelProvider(this).get(HomeModel.class);
        binding.setLifecycleOwner(Dashboard.this);
        rcv = binding.MainRCV;

        toolbar = binding.toolbar;
        drawer = binding.drawer;
        navView = binding.navLayout;

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);

        View header = navView.getHeaderView(0);
        ImageView imageView = header.findViewById(R.id.pImage);
        TextView name = header.findViewById(R.id.pName);
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

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Home:
                        Toast.makeText(Dashboard.this, "Home", Toast.LENGTH_SHORT).show();
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.meet_coach:
                        Toast.makeText(Dashboard.this, "Meeting Coach", Toast.LENGTH_SHORT).show();
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.privacy_policy:
                        Toast.makeText(Dashboard.this, "Privacy is Important :)", Toast.LENGTH_SHORT).show();
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.Log_out:
                        mAuth.signOut();
                        finishAffinity();
                        startActivity(new Intent(Dashboard.this, LoginActivity.class));
                        drawer.closeDrawer(GravityCompat.START);
                        break;


                }
                return true;
            }
        });

        progressBar = binding.progress;
        Sprite doubleBounce = new WanderingCubes();
        progressBar.setIndeterminateDrawable(doubleBounce);
        getItem();
        rcv.setLayoutManager(new GridLayoutManager(Dashboard.this, 2));
    }

    public void getItem() {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("Student_dashboard").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<String> arr = (ArrayList<String>) queryDocumentSnapshots.getDocuments().get(0).get("section");
                        ArrayList<String> imgURL = (ArrayList<String>) queryDocumentSnapshots.getDocuments().get(0).get("sectionImg");
                        Log.d("TAG", "onSuccess: " + arr);
                        Log.d("TAG", "onSuccess: " + imgURL);
                        int i = 0;
                        for (String s : arr) {
                            Log.e("TAG", "onSuccess: field " + i + s);
                            datalist.add(new DashBoardData(imgURL.get(i), s));
                            i++;
                        }
                        adapter = new dashBoard_adapter(datalist); // need data over here from firebase server
                        Log.d("TAG", "onCreate: " + datalist.get(1).getFieldname());
                        rcv.setLayoutManager(new GridLayoutManager(Dashboard.this, 2));
                        rcv.setAdapter(adapter);
                        i = 0;
                        progressBar.setVisibility(View.GONE);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                    }
                });

    }
    private void openProfile() {
        drawer.closeDrawer(GravityCompat.START);
        Toast.makeText(this, "Profile Opened", Toast.LENGTH_SHORT).show();
    }
}