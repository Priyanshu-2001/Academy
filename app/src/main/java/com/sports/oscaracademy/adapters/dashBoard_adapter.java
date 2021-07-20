package com.sports.oscaracademy.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sports.oscaracademy.HomeActivities.Students;
import com.sports.oscaracademy.HomeActivities.attendance;
import com.sports.oscaracademy.data.DashBoardData;
import com.sports.oscaracademy.databinding.SingleDashboardRcvBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class dashBoard_adapter extends RecyclerView.Adapter<dashBoard_adapter.holder> {
    private ArrayList<DashBoardData> data;
    Context context;
    public dashBoard_adapter(ArrayList<DashBoardData> data, Context context) {
        this.data = data;
        this.context = context;
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
        private SingleDashboardRcvBinding binding;
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
            switch (text.trim()){
                case "Fees & Payments":
                    break;
                case "Attendance":
                    context.startActivity(new Intent(context, attendance.class));
                    break;
                case "Upcoming Tournament": //for both admin and students
                    break;
                case "Schedule" :
                    break;
                case "Students":
                    context.startActivity(new Intent(context, Students.class));
                    break;

                case "Add Student":
                    break;

                case "Update Schedule":
                    break;

            }
        }
    }

}
