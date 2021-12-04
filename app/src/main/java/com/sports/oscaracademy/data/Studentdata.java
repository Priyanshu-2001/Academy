package com.sports.oscaracademy.data;

import com.google.firebase.Timestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Studentdata {
    String name;
    Integer rollno;
    String phone;
    String userId;
    String email;
    Timestamp Dob = Timestamp.now();
    String sex;
    String Age;
    String memberShip;
//    Timestamp start;
//    Timestamp end;

    public Studentdata(String name, Integer rollno, String phone, String userId, String email, Timestamp dob, String sex, String age, String session, String membershipStatus) {
        this.name = name;
        this.rollno = rollno;
        this.phone = phone;
        this.userId = userId;
        this.email = email;
        this.Dob = dob;
        this.sex = sex;
        Age = age;
        this.session = session;
        memberShip = membershipStatus;
    }

    public String getMemberShip() {
        return memberShip;
    }

    String session;

    public void setMemberShip(String memberShip) {
        this.memberShip = memberShip;
    }

    public String getSession() {
        return session;
    }

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
//        this.Dob = new Timestamp(new Date());
//        end = new Timestamp(new Date());
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollno() {
        return String.valueOf(rollno);
    }

    public void setRollno(Integer rollno) {
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

    public void setDob(String dob) {
        DateFormat sfd = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        Timestamp stamp  = Timestamp.now();
        try{
            date = sfd.parse(dob);
             stamp = new Timestamp(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        Dob = stamp;
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
        String date = "NOT AVAILABLE";
        try {
            date = sfd.format(getDob().toDate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public void setDob(Timestamp dob) {
        Dob = dob;
    }
//
//    public Timestamp getStart() {
//        return start;
//    }
//
//    public void setStart(Timestamp start) {
//        this.start = start;
//    }
//
//    public Timestamp getEnd() {
//        return end;
//    }
//
//    public void setEnd(Timestamp end) {
//        this.end = end;
//    }

//    public String getValidTo() {
//        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
//        String date = "NOT AVAILABLE";
//        try {
//            date = sfd.format(end.toDate());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return date;
//    }
}
