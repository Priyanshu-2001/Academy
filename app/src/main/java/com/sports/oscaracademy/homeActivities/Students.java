package com.sports.oscaracademy.homeActivities;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    Button tempFilterSelection, filter_rollNo, filter_name, filter_phoneNumber, filter_email, filter_InActive, filterBatchWise;
    String filterType = "name";
    String catcher = null;
    private boolean isInActiveBtnActive = false;
    private boolean isBatchWiseBtnActive = false;


    @Override
    protected void onRestart() {
        super.onRestart();
        binding.progress.setVisibility(View.VISIBLE);
        viewModel.refreshData();
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


        setSupportActionBar((Toolbar) binding.topBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.topTitleName.setText("Students List");
        binding.backBtn.setOnClickListener(v -> {
            finish();
        });
        binding.EndSearchBtn.animate().translationX(1000f);

        filter_rollNo = binding.filterRollNo;
        filter_name = binding.filerName;
        filter_email = binding.filterEmail;
        filter_InActive = binding.filterActive;
        filterBatchWise = binding.filterBatchWise;
        filter_phoneNumber = binding.filterPhoneNumber;
        binding.searchBtn.setOnClickListener(v -> {
            LayoutTransition layoutTransition = binding.layoutContainer.getLayoutTransition();
            layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
            openSearchbar();
        });
        tempFilterSelection = filter_name;
        binding.EndSearchBtn.setOnClickListener(v -> {
            exitSearchBar();
        });

        if (catcher != null) {
            updateFullData();
        }

        binding.ActionSearchBtn.setOnClickListener(v -> {
            Object query = binding.editSearch.getText().toString();
            applyFilter(query);
        });

        binding.editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
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

        filter_InActive.setOnClickListener(v -> {
            isInActiveBtnActive = !isInActiveBtnActive;
            setColorToDefault();
            setColorToSelected(tempFilterSelection);
            if (isInActiveBtnActive) {
//                applyFilter(binding.editSearch.getText().toString());
                binding.ActionSearchBtn.performClick();
                filter_InActive.setTextColor(getResources().getColor(R.color.white, null));
                filter_InActive.setBackground(AppCompatResources.getDrawable(this, R.drawable.premium_btn));
            } else {
                filter_InActive.setTextColor(getResources().getColor(R.color.black, null));
//                updateFullData();
                binding.ActionSearchBtn.performClick();
                filter_InActive.setBackground(AppCompatResources.getDrawable(this, R.drawable.btn_theme_2));
            }
        });
        filterBatchWise.setOnClickListener(v -> {
            isBatchWiseBtnActive = !isBatchWiseBtnActive;
            setColorToDefault();
            setColorToSelected(tempFilterSelection);
            if (isBatchWiseBtnActive) {
//                applyFilter(binding.editSearch.getText().toString());
                binding.ActionSearchBtn.performClick();
                filterBatchWise.setTextColor(getResources().getColor(R.color.white, null));
                filterBatchWise.setBackground(AppCompatResources.getDrawable(this, R.drawable.premium_btn));
            } else {
                filterBatchWise.setTextColor(getResources().getColor(R.color.black, null));
//                updateFullData();
                binding.ActionSearchBtn.performClick();
                filterBatchWise.setBackground(AppCompatResources.getDrawable(this, R.drawable.btn_theme_2));
            }
        });
        setColorToSelected(tempFilterSelection);
    }

    private void openSearchbar() {
        binding.searchBtn.animate().translationY(170f).withEndAction(() -> {
            binding.ActionSearchBtn.setVisibility(View.VISIBLE);
            binding.searchBtn.setVisibility(View.GONE);
        });
        binding.EndSearchBtn.setVisibility(View.VISIBLE);
        binding.EndSearchBtn.animate().translationX(0f);
        binding.editSearch.setVisibility(View.VISIBLE);
        binding.searchFilter.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (binding.editSearch.getVisibility() == View.VISIBLE) {
            exitSearchBar();
        } else {
            super.onBackPressed();
            finish();
        }
    }

    private void exitSearchBar() {
        binding.searchBtn.setVisibility(View.VISIBLE);
        binding.EndSearchBtn.setVisibility(View.GONE);
        binding.EndSearchBtn.animate().translationX(0f);
        binding.searchBtn.animate().translationY(0f);
        binding.ActionSearchBtn.animate().translationY(-20f);
        binding.ActionSearchBtn.setVisibility(View.INVISIBLE);
        binding.EndSearchBtn.animate().translationX(1000f);
        binding.editSearch.setVisibility(View.GONE);
        binding.searchFilter.setVisibility(View.GONE);
        updateFullData();
    }


    public void setColorToDefault() {
        tempFilterSelection.setTextColor(getResources().getColor(R.color.black, null));
        tempFilterSelection.setBackground(AppCompatResources.getDrawable(this, R.drawable.btn_theme_2));
    }

    public void setColorToSelected(Button btn) {
        tempFilterSelection.setTextColor(getResources().getColor(R.color.white, null));
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
                viewModel.getStudentDataFiltered(filterType, filter, isInActiveBtnActive, isBatchWiseBtnActive).observe(this, studentData -> {
                    Log.e("TAG", "applyFilter: " + studentData);
                    adapter = new studentList_adapter(getApplicationContext(), studentData, "false");
                    binding.studentrcv.setAdapter(adapter);
                    binding.progress.setVisibility(View.GONE);
                });
            } else {
                viewModel.getUserDataFiltered(filterType, filter, isInActiveBtnActive, isBatchWiseBtnActive).observe(this, new Observer<ArrayList<Studentdata>>() {
                    @Override
                    public void onChanged(ArrayList<Studentdata> studentData) {
                        adapter = new studentList_adapter(getApplicationContext(), studentData, "true");
                        binding.studentrcv.setAdapter(adapter);
                        binding.progress.setVisibility(View.GONE);
                    }
                });
            }
        }
    }
}