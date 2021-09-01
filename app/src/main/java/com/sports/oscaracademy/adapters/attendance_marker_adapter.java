package com.sports.oscaracademy.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sports.oscaracademy.R;
import com.sports.oscaracademy.data.Attendance_list;
import com.sports.oscaracademy.databinding.SingleAttendMarkerRcvBinding;
import com.sports.oscaracademy.service.attendanceService;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class attendance_marker_adapter extends RecyclerView.Adapter<attendance_marker_adapter.Vholder> {
    public ArrayList<Attendance_list> data;
    public ArrayList<Attendance_list> newData = new ArrayList<>();
    public boolean isPresent , onLeave ;
    public int date, month , year;
    public String type;
    public attendance_marker_adapter(ArrayList<Attendance_list> list, int date, int month, int year,String type) {
        this.data = list;
        this.date = date;
        this.month = month;
        this.year = year;
        this.type = type;
    }


    @NonNull
    @NotNull
    @Override
    public Vholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        SingleAttendMarkerRcvBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.single_attend_marker_rcv,parent,false);
        return new Vholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull attendance_marker_adapter.Vholder holder, int position) {
        holder.binding.setDataModel(data.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void UpdateDB(ProgressBar progressBar) {
        attendanceService service = new attendanceService();
        Map<String, Object> attend = new HashMap<>();
        for (int i = 0; i < newData.size(); i++) {
            if (!newData.get(i).getOnLeave())
                if (newData.get(i).getPresent())
                    attend.put(String.valueOf(newData.get(i).getRollNo()), "A");
                else
                    attend.put(String.valueOf(newData.get(i).getRollNo()), "P");
            else
                attend.put(String.valueOf(newData.get(i).getRollNo()), "L");
        }
        service.Updatedatabase(attend, date, month, year, progressBar);
    }

    public class Vholder extends RecyclerView.ViewHolder implements RadioGroup.OnCheckedChangeListener {
        SingleAttendMarkerRcvBinding binding;

        public Vholder(@NonNull @NotNull SingleAttendMarkerRcvBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            if (type.equals("get")) {
                this.binding.leave.setClickable(false);
                this.binding.absent.setClickable(false);
                this.binding.present.setClickable(false);
            }
            binding.radioGrp.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            newData.add(new Attendance_list(Integer.parseInt(binding.StudentID.getText().toString()), binding.StudentName.getText().toString(), binding.leave.isChecked(), binding.present.isChecked()));
        }
    }
}
