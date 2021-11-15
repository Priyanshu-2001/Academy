package com.sports.oscaracademy.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sports.oscaracademy.chat_feature.chat_activity;
import com.sports.oscaracademy.data.Studentdata;
import com.sports.oscaracademy.databinding.SingleChatProfileBinding;

import java.util.ArrayList;


public class chat_profile_adapter extends RecyclerView.Adapter<chat_profile_adapter.chatViewHolder> {


    public ArrayList<Studentdata> coachData;
    public ArrayList<Studentdata> adminData;
    ArrayList<Studentdata> studentList = null;
    Context context;

    public chat_profile_adapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<Studentdata> coach, ArrayList<Studentdata> admin) {
        coachData = coach;
        adminData = admin;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public chatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SingleChatProfileBinding binding = SingleChatProfileBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new chatViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull chatViewHolder holder, int position) {

        if (studentList != null) {
            holder.binding.username.setText(studentList.get(position).getName());
            holder.binding.position.setText("STUDENT");
            holder.binding.rollNo.setText(studentList.get(position).getRollno());
            int currentPos = position;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, chat_activity.class);
                    intent.putExtra("name", studentList.get(currentPos).getName());
                    intent.putExtra("uid", studentList.get(currentPos).getUserId());
//                intent.putExtra("token", studentList.get(position).getToken());
                    context.startActivity(intent);
                }
            });
            getPresence(studentList.get(currentPos).getUserId(), holder.binding);

        } else {
            if (position < adminData.size()) {
                holder.binding.username.setText(adminData.get(position).getName());
                holder.binding.position.setText("ADMIN");
                int currentPos = position;
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, chat_activity.class);
                        intent.putExtra("name", adminData.get(currentPos).getName());
                        intent.putExtra("uid", adminData.get(currentPos).getUserId());
                        context.startActivity(intent);
                    }
                });
                getPresence(adminData.get(currentPos).getUserId(), holder.binding);

            } else {
                holder.binding.username.setText(coachData.get(position - adminData.size()).getName());
                holder.binding.position.setText("COACH");
                int currentPos = position;
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, chat_activity.class);
                        intent.putExtra("name", coachData.get(currentPos - adminData.size()).getName());
                        intent.putExtra("uid", coachData.get(currentPos - adminData.size()).getUserId());
                        context.startActivity(intent);
                    }
                });
                getPresence(coachData.get(currentPos - adminData.size()).getUserId(), holder.binding);

            }
        }

    }

    @Override
    public int getItemCount() {
        if (studentList == null)
            return coachData.size() + adminData.size();
        return studentList.size();
    }

    public void setData(ArrayList<Studentdata> studentdata) {
        studentList = studentdata;
    }

    static class chatViewHolder extends RecyclerView.ViewHolder {
        SingleChatProfileBinding binding;

        public chatViewHolder(@NonNull SingleChatProfileBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public void getPresence(String receiverUid, SingleChatProfileBinding binding) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("presence").child(receiverUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String status = snapshot.getValue(String.class);
                    if (status != null) {
                        if (!status.isEmpty()) {
                            if (status.equals("offline")) {
                                binding.statusIcon.setVisibility(View.GONE);
                            } else {
                                binding.statusIcon.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
