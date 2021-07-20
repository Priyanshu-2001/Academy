package com.sports.oscaracademy.data;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Studentdata {
    String name;
    String rollno;
    String phone;
    String userId;
    String email;
    Timestamp Dob;
    String sex;
    String Age;
    Timestamp start;
    Timestamp end;

    public Studentdata(String name, String phone, String userId, String email, Timestamp dob, String sex, String age) {
        this.name = name;
        this.phone = phone;
        this.userId = userId;
        this.email = email;
        Dob = dob;
        this.sex = sex;
        Age = age;
    }

    public Studentdata(String name, String phone, String userId, String email, String sex, String age) {
        this.name = name;
        this.phone = phone;
        this.userId = userId;
        this.email = email;
        this.sex = sex;
        Age = age;
    }

    public Studentdata(String name, String rollno, String phone, String userId, String email, Timestamp dob, String sex, String age, Timestamp start, Timestamp end) {
        this.name = name;
        this.rollno = rollno;
        this.phone = phone;
        this.userId = userId;
        this.email = email;
        this.Dob = dob;
        this.sex = sex;
        Age = age;
        this.start = start;
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getDob() {
        return Dob;
    }

    public void setDob(Timestamp dob) {
        Dob = dob;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getDOB() {
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return sfd.format(getDob().toDate()).toString();
    }

    public String getValidTo(){
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return sfd.format(end.toDate()).toString();
    }
}
