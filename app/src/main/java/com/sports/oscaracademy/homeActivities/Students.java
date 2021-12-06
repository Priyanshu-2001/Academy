package com.sports.oscaracademy.homeActivities;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.sports.oscaracademy.R;
import com.sports.oscaracademy.adapters.studentList_adapter;
import com.sports.oscaracademy.data.Studentdata;
import com.sports.oscaracademy.databinding.ActivityStudentsBinding;
import com.sports.oscaracademy.service.studentsList;

import java.util.ArrayList;

//TODO try running simple way and then attack viewModel here

public class Students extends AppCompatActivity {
    studentsList list = new studentsList(this);
    private studentList_adapter adapter;

    ActivityStudentsBinding binding;

    Button tempFilterSelection, filter_rollNo, filter_name, filter_phoneNumber, filter_email;
    String filterType = "name";
    String catcher = null;


    @Override
    protected void onRestart() {
        super.onRestart();
        binding.progress.setVisibility(View.VISIBLE);
        updateFullData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Sprite doubleBounce = new WanderingCubes();

        try {
            catcher = getIntent().getStringExtra("catcher");
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_students);
        binding.progress.setIndeterminateDrawable(doubleBounce);
        binding.progress.setVisibility(View.VISIBLE);

        filter_rollNo = binding.topBar.filterRollNo;
        filter_name = binding.topBar.filerName;
        filter_email = binding.topBar.filterEmail;
        filter_phoneNumber = binding.topBar.filterPhoneNumber;

        tempFilterSelection = filter_rollNo;

        setSupportActionBar((Toolbar) binding.topBar.getRoot());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.topBar.topTitleName.setText("Students List");
        binding.topBar.backBtn.setOnClickListener(v -> {
            finish();
        });

        binding.topBar.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutTransition layoutTransition = binding.layoutContainer.getLayoutTransition();
                layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
                if (binding.topBar.editSearch.getVisibility() == View.GONE) {
                    binding.topBar.editSearch.setVisibility(View.VISIBLE);
                    binding.topBar.searchFilter.setVisibility(View.VISIBLE);
                } else {
                    binding.topBar.editSearch.setVisibility(View.GONE);
                    binding.topBar.searchFilter.setVisibility(View.GONE);
                    updateFullData();
                }

            }
        });

        if (catcher != null) {
            updateFullData();
//            applyFilter("phone number", "9877371590");
        }

        filter_phoneNumber.setOnClickListener(v -> {
            setColorToDefault();
            filterType = "phone number";
            tempFilterSelection = filter_phoneNumber;
            setColorToSelected(tempFilterSelection);
        });

        filter_name.setOnClickListener(v -> {
            setColorToDefault();
            filterType = "name";
            tempFilterSelection = filter_name;
            setColorToSelected(tempFilterSelection);
        });

        filter_email.setOnClickListener(v -> {
            setColorToDefault();
            tempFilterSelection = filter_email;
            filterType = "email";
            setColorToSelected(tempFilterSelection);
        });

        filter_rollNo.setOnClickListener(v -> {
            setColorToDefault();
            filterType = "RollNo";
            tempFilterSelection = filter_rollNo;
            setColorToSelected(tempFilterSelection);
        });
    }

    public void setColorToDefault() {
        tempFilterSelection.setBackground(AppCompatResources.getDrawable(this, R.drawable.btn_theme_2));
    }

    public void setColorToSelected(Button btn) {
        btn.setBackground(AppCompatResources.getDrawable(this, R.drawable.btn_theme_1));
    }

    public void updateFullData() {
        if (catcher != null) {
            if (catcher.equals("0")) {
                list.getStudents().observe(this, studentData -> {
                    adapter = new studentList_adapter(getApplicationContext(), studentData, "false");
                    binding.studentrcv.setAdapter(adapter);
                    binding.progress.setVisibility(View.GONE);
                });
            } else {
                list.getUsers().observe(this, new Observer<ArrayList<Studentdata>>() {
                    @Override
                    public void onChanged(ArrayList<Studentdata> studentdata) {
                        adapter = new studentList_adapter(getApplicationContext(), studentdata, "true");
                        binding.studentrcv.setAdapter(adapter);
                        binding.progress.setVisibility(View.GONE);
                    }
                });
            }
        }
    }

    public void applyFilter(String filterType, Object filter) {
        if (catcher != null) {
            if (catcher.equals("0")) {
                list.filteredStudentList(filterType, filter).observe(this, studentData -> {
                    Log.e("TAG", "applyFilter: " + studentData);
                    adapter = new studentList_adapter(getApplicationContext(), studentData, "false");
                    binding.studentrcv.setAdapter(adapter);
                    binding.progress.setVisibility(View.GONE);
                });
            } else {
                list.filteredUserList(filterType, filter).observe(this, new Observer<ArrayList<Studentdata>>() {
                    @Override
                    public void onChanged(ArrayList<Studentdata> studentdata) {
                        adapter = new studentList_adapter(getApplicationContext(), studentdata, "true");
                        binding.studentrcv.setAdapter(adapter);
                        binding.progress.setVisibility(View.GONE);
                    }
                });
            }
        }
    }
}