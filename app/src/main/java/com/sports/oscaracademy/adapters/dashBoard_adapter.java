package com.sports.oscaracademy.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sports.oscaracademy.data.DashBoardData;
import com.sports.oscaracademy.databinding.SingleDashboardRcvBinding;
import com.sports.oscaracademy.homeActivities.FeesPayment;
import com.sports.oscaracademy.homeActivities.PayAndPlay;
import com.sports.oscaracademy.homeActivities.Students;
import com.sports.oscaracademy.homeActivities.attendance;
import com.sports.oscaracademy.homeActivities.news_feeds;
import com.sports.oscaracademy.homeActivities.schedule.Schedule;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class dashBoard_adapter extends RecyclerView.Adapter<dashBoard_adapter.holder> {
    private final ArrayList<DashBoardData> data;
    Context context;
    private final SharedPreferences pref;
    private final String isStudent;
    private final String role;

    public dashBoard_adapter(ArrayList<DashBoardData> data, Context context) {
        this.data = data;
        this.context = context;
        pref = context.getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        isStudent = pref.getString("isStudent", "false");
        role = pref.getString("userType", "-1");
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SingleDashboardRcvBinding binding = SingleDashboardRcvBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull dashBoard_adapter.holder holder, int position) {
       DashBoardData d =  data.get(position);
       holder.binding.setUserModelRCV(d);
        Log.d("TAG", "onBindViewHolder: " + d.getFieldname());
       holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class holder extends RecyclerView.ViewHolder{
        private final SingleDashboardRcvBinding binding;
        public holder(@NonNull @NotNull SingleDashboardRcvBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextScreen(String.valueOf(binding.textView.getText()));
                }
            });
        }
        public void nextScreen(String text){
            Intent i;
            switch (text.trim()) {
                case "Fees & Payments":
                    if (isStudent.equals("true")) {
                        context.startActivity(new Intent(context, FeesPayment.class));
                    } else {
                        Toast.makeText(context, "Only Available For Enrolled Students", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "Attendance":
                    if (isStudent.equals("true") || role.equals("-2")) {
                        context.startActivity(new Intent(context, attendance.class));
                    } else {
                        Toast.makeText(context, "Only Available For Enrolled Students", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "News Feeds": //for both admin and students
                    context.startActivity(new Intent(context, news_feeds.class));
                    break;
                case "Schedule":
                    if (isStudent.equals("true")) {
                        context.startActivity(new Intent(context, Schedule.class));
                    } else {
                        Toast.makeText(context, "Only Available For Enrolled Students", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "Students":
                    i = new Intent(context, Students.class);
                    i.putExtra("catcher", "0");
                    context.startActivity(i);
                    break;

                case "Pay & Play":
                    context.startActivity(new Intent(context, PayAndPlay.class));
                    break;

                case "Add Student":
                    i = new Intent(context, Students.class);
                    i.putExtra("catcher", "1");
                    context.startActivity(i);
                    break;

                case "Update Schedule":
                    break;

            }
        }
    }

}
