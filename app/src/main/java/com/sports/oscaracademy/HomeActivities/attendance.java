package com.sports.oscaracademy.HomeActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import com.sports.oscaracademy.R;
import com.sports.oscaracademy.databinding.ActivityAttendanceBinding;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.threeten.bp.LocalDate;

public class attendance extends AppCompatActivity {

    ActivityAttendanceBinding binding;
    SharedPreferences pref;
    private FirebaseFirestore db;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String role;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Calendar calendar = Calendar.getInstance();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_attendance);
        setSupportActionBar((Toolbar) binding.toolbar);
        pref = getSharedPreferences("tokenFile", MODE_PRIVATE);
        db = FirebaseFirestore.getInstance();

        binding.topTitleName.setText("Attendance");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        role = pref.getString("role", "-1");
        if (role.equals("0")) {
            binding.studentScrollView.setVisibility(View.VISIBLE);
            binding.AdminScrollView.setVisibility(View.GONE);
        }
        if (role.equals("1")) {
            binding.studentScrollView.setVisibility(View.GONE);
            binding.AdminScrollView.setVisibility(View.VISIBLE);
            binding.markAttendance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    markAttendance();
                }
            });
            binding.getAttend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAttendance();
                }
            });
        }


        binding.calendarView.setSelectedDate(LocalDate.now());
        binding.calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull @NotNull MaterialCalendarView widget, @NonNull @NotNull CalendarDay date, boolean selected) {
                DecimalFormat formatter = new DecimalFormat("00");
                String mon = formatter.format(date.getMonth());
                String selecteddate = date.getDay() + " " + mon + " " + date.getYear();
                DateFormat sdf = new SimpleDateFormat("dd MM yyyy", Locale.getDefault());
                Log.e("TAG", "onDateSelected: " + selecteddate);
                sdf.setLenient(false);
                try {
                    Date current_selection = sdf.parse(selecteddate);
                    if (current_selection != null) {
                        if (current_selection.after(calendar.getTime())) {
                            Log.e("TAG", "onDateSelected: " + "ahead");
                            applyLeave(date);
                            binding.markAttendance.setVisibility(View.GONE);
                            binding.getAttend.setVisibility(View.GONE);
                        } else {
                            Log.e("TAG", "onDateSelected: " + "Behind");
                            showAttendance();
                            binding.markAttendance.setVisibility(View.VISIBLE);
                            binding.getAttend.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getAttendance() {
        Intent i = new Intent(this,admin_attendance.class);
        i.putExtra("date",binding.calendarView.getSelectedDate().getDay());
        i.putExtra("month",binding.calendarView.getSelectedDate().getMonth());
        i.putExtra("year",binding.calendarView.getSelectedDate().getYear());
        i.putExtra("type","get");
        startActivity(i);
    }

    private void markAttendance() {
        Intent i = new Intent(this,admin_attendance.class);
        i.putExtra("date",binding.calendarView.getSelectedDate().getDay());
        i.putExtra("month",binding.calendarView.getSelectedDate().getMonth());
        i.putExtra("year",binding.calendarView.getSelectedDate().getYear());
        i.putExtra("type","post");
        startActivity(i);
    }

    private void showAttendance() {
        binding.leaveButton.setVisibility(View.GONE);
    }


    private void applyLeave(@NotNull CalendarDay current_selection) {
        binding.leaveButton.setVisibility(View.VISIBLE);
        binding.leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> map = new HashMap<>();
                map.put("onLeave", "true");
                map.put("date", String.valueOf(current_selection));
                db.collection("attendance")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .set(map, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                DayViewDecorator decorator = new DayViewDecorator() {
                                    @Override
                                    public boolean shouldDecorate(CalendarDay day) {
                                        if (day.equals(current_selection)) {
                                            Toast.makeText(attendance.this, "should", Toast.LENGTH_SHORT).show();
                                            return true;
                                        } else {
                                            return false;
                                        }
                                    }

                                    @Override
                                    public void decorate(DayViewFacade view) {
                                        view.addSpan(new DotSpan(getColor(R.color.scnd_grey)));
                                        Toast.makeText(attendance.this, "decorate", Toast.LENGTH_SHORT).show();
                                        view.setBackgroundDrawable(AppCompatResources.getDrawable(attendance.this, R.drawable.leave_span));
                                    }
                                };
                                binding.calendarView.addDecorators(decorator);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {

                            }
                        });
            }
        });

    }
}