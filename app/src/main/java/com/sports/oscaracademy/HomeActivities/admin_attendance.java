package com.sports.oscaracademy.HomeActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.sports.oscaracademy.R;
import com.sports.oscaracademy.adapters.attendance_marker_adapter;
import com.sports.oscaracademy.data.Attendance_list;
import com.sports.oscaracademy.data.Studentdata;
import com.sports.oscaracademy.databinding.ActivityAdminAttendanceBinding;
import com.sports.oscaracademy.service.attendanceService;
import com.sports.oscaracademy.service.studentsList;

import java.util.ArrayList;

public class admin_attendance extends AppCompatActivity {
    public ActivityAdminAttendanceBinding binding;
    public ProgressBar progressBar;
    public attendance_marker_adapter adapter;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_attendance);
        int date = getIntent().getIntExtra("date", 1);
        int month = getIntent().getIntExtra("month", 1);
        int year = getIntent().getIntExtra("year", 1);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView rcv = binding.RcycView;
        ArrayList<Attendance_list> list = new ArrayList<>();
        binding.setLifecycleOwner(this);
        studentsList students = new studentsList(this);
        attendanceService getAttend = new attendanceService();
        final boolean[] isadded = {false};

        students.getStudents().observe(this, new Observer<ArrayList<Studentdata>>() {
            @Override
            public void onChanged(ArrayList<Studentdata> studentdata) {
                Log.d("TAG", "onChanged: " + studentdata.get(0));
                progressBar.setVisibility(View.VISIBLE);
                getAttend.getPreviousRecord(date + "-" + month + "-" + year).observe(binding.getLifecycleOwner(), new Observer<ArrayList<Attendance_list>>() {
                    @Override
                    public void onChanged(ArrayList<Attendance_list> studentAttendances) {
                        if (studentAttendances != null) {
                            for (int i = 0; i < studentdata.size(); i++) {
                                if (studentAttendances.size() > 0) {
                                    for (int j = 0; j < studentAttendances.size(); j++) {
                                        if (studentAttendances.get(j).getRollNo().equals(studentdata.get(i).getRollno())) {
                                            list.add(new Attendance_list(studentdata.get(i).getRollno(), studentdata.get(i).getName(), studentAttendances.get(j).getOnLeave(), studentAttendances.get(j).getPresent()));
                                            studentdata.remove(i);
                                            studentAttendances.remove(j);
                                            isadded[0] = true;
                                            break;
                                        }
                                    }
                                    if (!isadded[0]) {
                                        list.add(new Attendance_list(studentdata.get(i).getRollno(), studentdata.get(i).getName()));
                                        isadded[0] = false;
                                    }
                                }
                                if (studentAttendances.size() > 0) {
                                    for (int k = 0; k < studentAttendances.size(); k++) {
                                        list.add(new Attendance_list(studentAttendances.get(k).getRollNo(), studentAttendances.get(k).getName(), studentAttendances.get(k).getOnLeave(), studentAttendances.get(k).getPresent()));
                                    }
                                }
                            }
                            Log.d("TAG", "onCreate: " + list.get(0).getOnLeave());
                            Log.d("TAG", "onCreate: " + list.get(0).getPresent());
                            progressBar.setVisibility(View.GONE);
                        } else {
                            if (studentdata.size() > 0) {
                                for (int i = 0; i < studentdata.size(); i++) {
                                    list.add(new Attendance_list(studentdata.get(i).getRollno(), studentdata.get(i).getName()));
                                }
                                adapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });


            }
        });

        adapter = new attendance_marker_adapter(list, date, month, year);

        rcv.setAdapter(adapter);
        progressBar = binding.progress;
        Sprite doubleBounce = new WanderingCubes();
        progressBar.setIndeterminateDrawable(doubleBounce);

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                adapter.UpdateDB(progressBar);
            }
        });
    }
}