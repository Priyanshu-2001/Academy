package com.sports.oscaracademy.HomeActivities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

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
        String type = getIntent().getStringExtra("type");
        progressBar = binding.progress;
        Sprite doubleBounce = new WanderingCubes();
        progressBar.setIndeterminateDrawable(doubleBounce);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView rcv = binding.RcycView;
        ArrayList<Attendance_list> list = new ArrayList<>();
        binding.setLifecycleOwner(this);
        studentsList students = new studentsList(this);
        attendanceService getAttend = new attendanceService();
        final boolean[] isadded = {false};
        if (type.equals("post")) {
            progressBar.setVisibility(View.VISIBLE);
            students.getStudents().observe(this, new Observer<ArrayList<Studentdata>>() {
                @Override
                public void onChanged(ArrayList<Studentdata> studentdata) {
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
                                                i--;
                                                studentAttendances.remove(j);
                                                isadded[0] = true;
                                                break;
                                            }
                                        }
                                        if (!isadded[0]) {
                                            list.add(new Attendance_list(studentdata.get(i).getRollno(), studentdata.get(i).getName()));
                                            isadded[0] = false;
                                        }
                                    } else {
                                        list.add(new Attendance_list(studentdata.get(i).getRollno(), studentdata.get(i).getName()));
                                        studentdata.remove(i);
                                        i--;
                                    }
                                }
                                if (!studentdata.isEmpty()) {
                                    for (int i = 0; i < studentdata.size(); i++) {
                                        list.add(new Attendance_list(studentdata.get(i).getRollno(), studentdata.get(i).getName()));
                                        studentdata.remove(i);
                                        i--;
                                    }
                                }
                                if (!studentAttendances.isEmpty()) {
                                    for (int i = 0; i < studentAttendances.size(); i++) {
                                        list.add(new Attendance_list(studentAttendances.get(i).getRollNo(), studentAttendances.get(i).getPresent(), studentAttendances.get(i).getOnLeave()));
                                        studentAttendances.remove(i);
                                        i--;
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
        }
        else {
            binding.headerTop.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            binding.button.setVisibility(View.GONE);
            getAttend.getPreviousRecord(date + "-" + month + "-" + year).observe(this, new Observer<ArrayList<Attendance_list>>() {
                @Override
                public void onChanged(ArrayList<Attendance_list> studentAttendances) {
                    students.getStudents().observe(binding.getLifecycleOwner(), new Observer<ArrayList<Studentdata>>() {
                        @Override
                        public void onChanged(ArrayList<Studentdata> studentdata) {

                            if (studentAttendances != null) {
                                binding.headerTop.setVisibility(View.VISIBLE);
                                for (int i = 0; i < studentAttendances.size(); i++) {
                                    for (int j = 0; j < studentdata.size(); j++) {
                                        if (studentAttendances.get(i).getRollNo().equals(studentdata.get(j).getRollno())) {
                                            list.add(new Attendance_list(studentdata.get(j).getRollno(), studentdata.get(j).getName(), studentAttendances.get(i).getOnLeave(), studentAttendances.get(i).getPresent()));
                                            studentAttendances.remove(i);
                                            i--;
                                            break;
                                        }
                                    }
                                }
                                if (!studentAttendances.isEmpty()) {
                                    for (int i = 0; i < studentAttendances.size(); i++) {
                                        list.add(new Attendance_list(studentdata.get(i).getRollno(), studentAttendances.get(i).getOnLeave(), studentAttendances.get(i).getPresent()));
                                    }
                                }
                            } else {
                                binding.divider.setVisibility(View.GONE);
                                binding.noData.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.no_data));
                                binding.noData.setVisibility(View.VISIBLE);
                            }
                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                }
            });

        }

        adapter = new attendance_marker_adapter(list, date, month, year,type);
        rcv.setAdapter(adapter);

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                adapter.UpdateDB(progressBar);
            }
        });
    }
}