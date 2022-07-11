package com.sports.oscaracademy.homeActivities;

import android.os.Bundle;
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

public class adminAttendance extends AppCompatActivity {
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
        if (type.equals("post")) {
            progressBar.setVisibility(View.VISIBLE);
            students.getStudents().observe(this, TotalStudents ->
                    getAttend.getPreviousRecord(date + "-" + month + "-" + year)
                            .observe(adminAttendance.this, StudentPreviousAttendanceList -> {
                                if (StudentPreviousAttendanceList != null) {
                                    for (int i = 0; i < TotalStudents.size(); i++) {
                                        containRoll c = containsRoll(StudentPreviousAttendanceList, TotalStudents.get(i).getRollno());
                                        if (c.isContaining) {
                                            StudentPreviousAttendanceList.remove(c.obj);
                                            list.add(new Attendance_list(Integer.valueOf(TotalStudents.get(i).getRollno()), TotalStudents.get(i).getName(), c.obj.getOnLeave(), c.obj.getPresent()));
                                        } else {
                                            list.add(new Attendance_list(Integer.valueOf(TotalStudents.get(i).getRollno()), TotalStudents.get(i).getName()));
                                        }
                                    }
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    if (TotalStudents.size() > 0) {
                                        for (int i = 0; i < TotalStudents.size(); i++) {
                                            list.add(new Attendance_list(Integer.valueOf(TotalStudents.get(i).getRollno()), TotalStudents.get(i).getName()));
                                        }
                                        adapter.notifyDataSetChanged();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }));
        } else {
            binding.headerTop.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            binding.button.setVisibility(View.GONE);
            getAttend.getPreviousRecord(date + "-" + month + "-" + year).observe(this, new Observer<ArrayList<Attendance_list>>() {
                @Override
                public void onChanged(ArrayList<Attendance_list> studentAttendances) {
                    students.getStudents().observe(adminAttendance.this, new Observer<ArrayList<Studentdata>>() {
                        @Override
                        public void onChanged(ArrayList<Studentdata> studentdata) {

                            if (studentAttendances != null) {
                                binding.headerTop.setVisibility(View.VISIBLE);
                                for (int i = 0; i < studentAttendances.size(); i++) {
                                    for (int j = 0; j < studentdata.size(); j++) {
                                        if (studentAttendances.get(i).getRollNo().equals(studentdata.get(j).getRollno())) {
                                            list.add(new Attendance_list(Integer.valueOf(studentdata.get(j).getRollno()), studentdata.get(j).getName(), studentAttendances.get(i).getOnLeave(), studentAttendances.get(i).getPresent()));
                                            studentAttendances.remove(i);
                                            i--;
                                            break;
                                        }
                                    }
                                }
                                if (!studentAttendances.isEmpty()) {
                                    for (int i = 0; i < studentAttendances.size(); i++) {
                                        list.add(new Attendance_list(Integer.valueOf(studentdata.get(i).getRollno()), studentAttendances.get(i).getOnLeave(), studentAttendances.get(i).getPresent()));
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

        adapter = new attendance_marker_adapter(list, date, month, year, type);
        rcv.setAdapter(adapter);

        binding.button.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            adapter.UpdateDB(progressBar);
        });
    }

    public containRoll containsRoll(final ArrayList<Attendance_list> list, final String roll) {
        for (Attendance_list o : list) {
            if (o.getRollNo().equals(roll)) {
                return new containRoll(o, true);
            }
        }
        return new containRoll(new Attendance_list(), false);
    }
}

class containRoll {
    Attendance_list obj;
    boolean isContaining;

    public containRoll(Attendance_list obj, boolean isContaining) {
        this.obj = obj;
        this.isContaining = isContaining;
    }
}
