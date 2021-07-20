package com.sports.oscaracademy.HomeActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_students);
        setSupportActionBar(binding.topBar.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        studentsList list = new studentsList(this);
        binding.topBar.topTitleName.setText("Students List");
        RecyclerView rcv = binding.studentrcv;
        list.getStudents().observe(this, new Observer<ArrayList<Studentdata>>() {
            @Override
            public void onChanged(ArrayList<Studentdata> studentdata) {
                adapter = new studentList_adapter(getApplicationContext(), studentdata);
                rcv.setAdapter(adapter);
            }
        });
    }
}