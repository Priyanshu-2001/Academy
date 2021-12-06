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
import androidx.lifecycle.ViewModelProvider;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.sports.oscaracademy.R;
import com.sports.oscaracademy.adapters.studentList_adapter;
import com.sports.oscaracademy.data.Studentdata;
import com.sports.oscaracademy.databinding.ActivityStudentsBinding;
import com.sports.oscaracademy.viewModel.AdminStudentsViewModel;

import java.util.ArrayList;


public class Students extends AppCompatActivity {
    private studentList_adapter adapter;

    ActivityStudentsBinding binding;
    AdminStudentsViewModel viewModel;
    Button tempFilterSelection, filter_rollNo, filter_name, filter_phoneNumber, filter_email;
    String filterType = "RollNo";
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
        viewModel = new ViewModelProvider(this).get(AdminStudentsViewModel.class);

        try {
            catcher = getIntent().getStringExtra("catcher");
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_students);
        binding.progress.setIndeterminateDrawable(doubleBounce);
        binding.progress.setVisibility(View.VISIBLE);
        binding.topBar.EndSearchBtn.animate().translationX(1000f);

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

        binding.topBar.searchBtn.setOnClickListener(v -> {
            LayoutTransition layoutTransition = binding.layoutContainer.getLayoutTransition();
            layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
            openSearchbar();
        });

        binding.topBar.EndSearchBtn.setOnClickListener(v -> {
            exitSearchBar();
        });

        if (catcher != null) {
            updateFullData();
        }

        binding.topBar.ActionSearchBtn.setOnClickListener(v -> {
            Object query = binding.topBar.editSearch.getText().toString();
            applyFilter(query);
        });

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
        setColorToSelected(tempFilterSelection);
    }

    private void openSearchbar() {
        binding.topBar.searchBtn.animate().translationY(170f).withEndAction(() -> {
            binding.topBar.ActionSearchBtn.setVisibility(View.VISIBLE);
            binding.topBar.searchBtn.setVisibility(View.GONE);
        });
        binding.topBar.EndSearchBtn.setVisibility(View.VISIBLE);
        binding.topBar.EndSearchBtn.animate().translationX(0f);
        binding.topBar.editSearch.setVisibility(View.VISIBLE);
        binding.topBar.searchFilter.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (binding.topBar.editSearch.getVisibility() == View.VISIBLE) {
            exitSearchBar();
        } else {
            super.onBackPressed();
            finish();
        }
    }

    private void exitSearchBar() {
        binding.topBar.searchBtn.setVisibility(View.VISIBLE);
        binding.topBar.EndSearchBtn.setVisibility(View.GONE);
        binding.topBar.EndSearchBtn.animate().translationX(0f);
        binding.topBar.searchBtn.animate().translationY(0f);
        binding.topBar.ActionSearchBtn.animate().translationY(-20f);
        binding.topBar.ActionSearchBtn.setVisibility(View.INVISIBLE);
        binding.topBar.EndSearchBtn.animate().translationX(1000f);
        binding.topBar.editSearch.setVisibility(View.GONE);
        binding.topBar.searchFilter.setVisibility(View.GONE);
        updateFullData();
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
                viewModel.getStudentData().observe(this, studentData -> {
                    adapter = new studentList_adapter(getApplicationContext(), studentData, "false");
                    binding.studentrcv.setAdapter(adapter);
                    binding.progress.setVisibility(View.GONE);
                });
            } else {
                viewModel.getUserData().observe(this, new Observer<ArrayList<Studentdata>>() {
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

    public void applyFilter(Object filter) {
        Log.e("TAG", "applyFilter: " + filterType + " " + filter);
        if (catcher != null) {
            if (catcher.equals("0")) {
                viewModel.getStudentDataFiltered(filterType, filter).observe(this, studentData -> {
                    Log.e("TAG", "applyFilter: " + studentData);
                    adapter = new studentList_adapter(getApplicationContext(), studentData, "false");
                    binding.studentrcv.setAdapter(adapter);
                    binding.progress.setVisibility(View.GONE);
                });
            } else {
                viewModel.getUserDataFiltered(filterType, filter).observe(this, new Observer<ArrayList<Studentdata>>() {
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