package com.sports.oscaracademy.data;

public class Attendance_list {
    String RollNo, Name;
    Boolean onLeave, isPresent, isAbsent;

    public Attendance_list(String rollNo, String name, Boolean onLeave, Boolean isPresent) {
        RollNo = rollNo;
        Name = name;
        this.onLeave = onLeave;
        if(onLeave){
            this.isPresent = false;
            this.isAbsent = false;
        }else {
            this.isPresent = isPresent;
            this.isAbsent = !isPresent;
        }
    }

    public Attendance_list(String rollNo, Boolean isPresent , Boolean onLeave) {
        RollNo = rollNo;
        this.onLeave = onLeave;
        this.isPresent = isPresent;
        this.isAbsent = !isPresent;
    }

    public Attendance_list(String rollNo, String name, Boolean onLeave) {
        RollNo = rollNo;
        Name = name;
        this.onLeave = onLeave;
        this.isPresent = false;
        this.isAbsent = false;
    }

    public Attendance_list(String rollNo, String name) {
        RollNo = rollNo;
        Name = name;
        this.isPresent = false;
        this.onLeave = false;
        this.isAbsent = false;
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

    public Boolean getAbsent() {
        return isAbsent;
    }

    public void setAbsent(Boolean absent) {
        if (absent) {
            isAbsent = true;
            isPresent = false;
        } else {
            isAbsent = false;
            isPresent = true;
        }
    }

    public void setPresent(Boolean present) {
        isPresent = present;
    }
}
