package com.sports.oscaracademy.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sports.oscaracademy.R;
import com.sports.oscaracademy.data.Studentdata;
import com.sports.oscaracademy.databinding.SingleStudentRcvBinding;
import com.sports.oscaracademy.drawerFragments.profileActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class studentList_adapter extends RecyclerView.Adapter<studentList_adapter.studentsHolder> {
    private final ArrayList<Studentdata> data;
    private final Context mContext;
    String isEditable;

    public studentList_adapter(Context c, ArrayList<Studentdata> d, String editable) {
        data = d;
        mContext = c;
        isEditable = editable;
    }

    @NonNull
    @NotNull
    @Override
    public studentsHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        SingleStudentRcvBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.single_student_rcv,parent,false);
        return new studentsHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull studentList_adapter.studentsHolder holder, int position) {
        int pos = position;
        holder.binding.setStudent(data.get(pos));
        holder.binding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, profileActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("UID", data.get(pos).getUserId());
                i.putExtra("editable", isEditable);
                mContext.startActivity(i);
            }
        });
        Log.e("TAG", "onBindViewHolder: " + data.get(pos).getRollno());
        Log.e("TAG", "onBindViewHolder: " + data.get(pos).getPhone());
        Log.e("TAG", "onBindViewHolder: " + data.get(pos).getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class studentsHolder extends RecyclerView.ViewHolder{
    SingleStudentRcvBinding binding ;
        public studentsHolder(@NonNull @NotNull SingleStudentRcvBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
