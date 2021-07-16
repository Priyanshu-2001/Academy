package com.sports.oscaracademy.data;

import androidx.lifecycle.MutableLiveData;

public class Attendance_list {
    String RollNo , Name ;
    Boolean onLeave, isPresent;

    public Attendance_list(String rollNo, String name, Boolean onLeave, Boolean isPresent) {
        RollNo = rollNo;
        Name = name;
        this.onLeave = onLeave;
        this.isPresent = isPresent;
    }

    public String getRollNo() {
        return RollNo;
    }

    public void setRollNo(String rollNo) {
        RollNo = rollNo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Boolean getOnLeave() {
        return onLeave;
    }

    public void setOnLeave(Boolean onLeave) {
        this.onLeave = onLeave;
    }

    public Boolean getPresent() {
        return isPresent;
    }

    public void setPresent(Boolean present) {
        isPresent = present;
    }
}
