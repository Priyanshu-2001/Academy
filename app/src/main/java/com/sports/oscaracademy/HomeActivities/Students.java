package com.sports.oscaracademy.HomeActivities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class Students extends AppCompatActivity {
    private studentList_adapter adapter;

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    ActivityStudentsBinding binding;

    String catcher = null;
    studentsList list = new studentsList(this);

    @Override
    protected void onRestart() {
        super.onRestart();
        binding.progress.setVisibility(View.VISIBLE);
        updateData();
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
        setSupportActionBar(binding.topBar.getRoot());
        binding.progress.setIndeterminateDrawable(doubleBounce);
        binding.progress.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        binding.topBar.topTitleName.setText("Students List");

        if (catcher != null) {
            updateData();
        }

    }

    public void updateData() {
        if (catcher != null) {
            if (catcher.equals("0")) {
                list.getStudents().observe(this, new Observer<ArrayList<Studentdata>>() {
                    @Override
                    public void onChanged(ArrayList<Studentdata> studentdata) {
                        adapter = new studentList_adapter(getApplicationContext(), studentdata, "false");
                        binding.studentrcv.setAdapter(adapter);
                        binding.progress.setVisibility(View.GONE);
                    }
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
}