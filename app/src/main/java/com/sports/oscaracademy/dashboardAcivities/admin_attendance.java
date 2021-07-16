package com.sports.oscaracademy.dashboardAcivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Service;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.sports.oscaracademy.R;
import com.sports.oscaracademy.adapters.attendance_marker_adapter;
import com.sports.oscaracademy.data.Attendance_list;
import com.sports.oscaracademy.databinding.ActivityAdminAttendanceBinding;

import java.time.LocalDate;
import java.util.ArrayList;

public class admin_attendance extends AppCompatActivity {
    public ActivityAdminAttendanceBinding binding;
    public ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_admin_attendance);
        int date = getIntent().getIntExtra("date", 1);
        int month = getIntent().getIntExtra("month",1);
        int year = getIntent().getIntExtra("year",1);

        RecyclerView rcv = binding.RcycView;
        ArrayList<Attendance_list> list = new ArrayList<>();
        MutableLiveData<Boolean> present = new MutableLiveData<>();
        present.setValue(true);
        MutableLiveData<Boolean> leave = new MutableLiveData<>();
        present.setValue(false);
        list.add(new Attendance_list("1","Priyanshu gupta" ,present.getValue() , present.getValue())); //from server backend
        list.add(new Attendance_list("2","Raju gupta" ,present.getValue() , present.getValue())); //from server backend
        list.add(new Attendance_list("4","Kaju gupta" ,present.getValue() , present.getValue())); //from server backend
        list.add(new Attendance_list("3","badam gupta" ,present.getValue() , present.getValue())); //from server backend
        attendance_marker_adapter adapter = new attendance_marker_adapter(list,date , month , year);
        rcv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

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