package com.sports.oscaracademy.HomeActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import com.sports.oscaracademy.R;
import com.sports.oscaracademy.databinding.ActivityAttendanceBinding;
import com.sports.oscaracademy.service.attendanceService;

import org.jetbrains.annotations.NotNull;
import org.threeten.bp.LocalDate;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class attendance extends AppCompatActivity {

    ActivityAttendanceBinding binding;
    SharedPreferences pref;
    attendanceService service = new attendanceService();
    ArrayList<CalendarDay> leaveDay = new ArrayList<>();
    private FirebaseDatabase db;

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
        setSupportActionBar(binding.toolbar);
        pref = getSharedPreferences("tokenFile", MODE_PRIVATE);
        db = FirebaseDatabase.getInstance();

        binding.topTitleName.setText("Attendance");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        role = pref.getString("userType", "-1");
        Log.e("TAG", "onCreate: attendance role " + role);
        if (role.equals("1")) {
            binding.studentScrollView.setVisibility(View.VISIBLE);
            binding.AdminScrollView.setVisibility(View.GONE);
            highlightAttendance();
        }
        if (role.equals("-2")) {
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
                            int date_ = binding.calendarView.getSelectedDate().getDay();
                            int month = binding.calendarView.getSelectedDate().getMonth();
                            int year = binding.calendarView.getSelectedDate().getYear();
                            String newDate = date_ + "-" + month + "-" + year;
                            if (!leaveDay.contains(date))
                                applyLeave(newDate, date);
                            else {
                                cancelLeave(newDate, date);
                            }
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

    private void cancelLeave(String newDate, CalendarDay date) {
        binding.cancelLeave.setVisibility(View.VISIBLE);
        binding.leaveButton.setVisibility(View.GONE);
        String roll = String.valueOf(pref.getInt("roll", -1));
        binding.cancelLeave.setOnClickListener(v -> {
            db.getReference().child("CombinedAttendance").child(newDate).child(roll).removeValue();
            db.getReference().child("individualAttendance").child(roll).child(newDate).removeValue();
            DecorateView(date, -1);
            leaveDay.remove(date);
            Log.e("TAG", "cancelLeave: " + date);
            Log.e("TAG", "cancelLeave: " + leaveDay);
            binding.leaveButton.setVisibility(View.VISIBLE);
            binding.cancelLeave.setVisibility(View.GONE);
            Snackbar.make(v, "Leave Canceled for " + newDate, Snackbar.LENGTH_LONG).setDuration(5000).setBackgroundTint(getResources().getColor(R.color.black)).setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).show();
        });
    }

    private void highlightAttendance() {
        Integer roll = pref.getInt("roll", -1);
        if (roll != -1) {
            db.getReference().child("individualAttendance").child(String.valueOf(roll)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.e("TAG", "onComplete: " + task.getResult());
                        DataSnapshot snaps = task.getResult();
                        for (DataSnapshot snap : snaps.getChildren()) {
                            switch (snap.getValue(String.class)) {
                                case "A":
                                    DecorateView(getCalenderDate(snap.getKey()), 1);
                                    break;
                                case "P":
                                    DecorateView(getCalenderDate(snap.getKey()), 2);
                                    break;
                                case "L":
                                    DecorateView(getCalenderDate(snap.getKey()), 3);
                                    leaveDay.add(getCalenderDate(snap.getKey()));
                                    break;
                            }
                        }
                    }
                }
            });
        } else {

        }

    }

    public CalendarDay getCalenderDate(String date) {
        int count = 0;
        StringBuilder temp = new StringBuilder();
        int day = 0, mon = 0, year = 0;
        for (int i = 0; i < date.length(); i++) {
            if (date.charAt(i) == '-') {
//                Log.e("TAG", "getCalenderDate: " +temp );
                if (count == 0)
                    day = Integer.parseInt(String.valueOf(temp));
                else if (count == 1)
                    mon = Integer.parseInt(String.valueOf(temp));
                count++;
                temp.delete(0, temp.length());
                continue;
            }
            temp.append(date.charAt(i));
        }
        year = Integer.parseInt(String.valueOf(temp));
        Log.e("TAG", "getCalenderDate: " + CalendarDay.from(year, mon, day));
        return CalendarDay.from(year, mon, day);
    }

    private void DecorateView(CalendarDay date, int i) {
        DayViewDecorator decorator = new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                return day.equals(date);
            }

            @Override
            public void decorate(DayViewFacade view) {
                DotSpan span;
                switch (i) {
                    case 1:
                        span = new DotSpan(getColor(R.color.absent_span));
                        view.setBackgroundDrawable(AppCompatResources.getDrawable(attendance.this, R.drawable.absent_span));
                        break;
                    case 2:
                        span = new DotSpan(getColor(R.color.present_spam));
                        view.setBackgroundDrawable(AppCompatResources.getDrawable(attendance.this, R.drawable.present_span));
                        break;
                    case 3:
                        span = new DotSpan(getColor(R.color.leave_span));
                        view.setBackgroundDrawable(AppCompatResources.getDrawable(attendance.this, R.drawable.leave_span));
                        break;
                    default:
                        span = new DotSpan(getColor(R.color.colorPrimaryDarker));
                        view.setBackgroundDrawable(AppCompatResources.getDrawable(attendance.this, R.drawable.circle));
                }
                view.addSpan(span);
            }
        };
        binding.calendarView.addDecorators(decorator);
    }

    private void getAttendance() {
        Intent i = new Intent(this, admin_attendance.class);
        i.putExtra("date", binding.calendarView.getSelectedDate().getDay());
        i.putExtra("month", binding.calendarView.getSelectedDate().getMonth());
        i.putExtra("year", binding.calendarView.getSelectedDate().getYear());
        i.putExtra("type", "get");
        startActivity(i);
    }

    private void markAttendance() {
        Intent i = new Intent(this, admin_attendance.class);
        i.putExtra("date", binding.calendarView.getSelectedDate().getDay());
        i.putExtra("month", binding.calendarView.getSelectedDate().getMonth());
        i.putExtra("year", binding.calendarView.getSelectedDate().getYear());
        i.putExtra("type", "post");
        startActivity(i);
    }

    private void showAttendance() {
        binding.leaveButton.setVisibility(View.GONE);
    }


    private void applyLeave(String current_selection, @NotNull CalendarDay date) {
        binding.leaveButton.setVisibility(View.VISIBLE);
        binding.cancelLeave.setVisibility(View.GONE);
        binding.leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer roll = pref.getInt("roll", -1);
                Map<String, Object> map = new HashMap<>();
                if (roll != -1)
                    map.put(String.valueOf(roll), "L");
                Log.e("TAG", "onClick: rool " + map);
                Log.e("TAG", "onClick: currentSecetio " + current_selection);
                db.getReference().child("CombinedAttendance").child(current_selection).updateChildren(map)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                binding.cancelLeave.setVisibility(View.VISIBLE);
                                binding.leaveButton.setVisibility(View.GONE);
                                leaveDay.add(getCalenderDate(current_selection));
                                service.addToSpecificStudentRecord(current_selection, map);
                                DayViewDecorator decorator = new DayViewDecorator() {
                                    @Override
                                    public boolean shouldDecorate(CalendarDay day) {
                                        if (day.equals(date)) {
                                            Log.e("TAG", "shouldDecorate: " + date);
                                            return true;
                                        } else {
                                            return false;
                                        }
                                    }

                                    @Override
                                    public void decorate(DayViewFacade view) {
                                        view.addSpan(new DotSpan(getColor(R.color.scnd_grey)));
                                        view.setBackgroundDrawable(AppCompatResources.getDrawable(attendance.this, R.drawable.leave_span));
                                    }
                                };
                                Snackbar.make(v, "Leave applied for " + current_selection, Snackbar.LENGTH_LONG).setDuration(5000).setBackgroundTint(getResources().getColor(R.color.black)).setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).show();
                                binding.calendarView.addDecorators(decorator);
                            } else {
                                Snackbar.make(v, "failed", Snackbar.LENGTH_LONG).show();
                            }
                        });
            }
        });

    }
}