package com.sports.oscaracademy.data;
public class Studentdata {
    String name;
    Integer rollno;
    String phone;
    String userId;
    String email;
    String sex;
    String Age;
    String memberShip;
    String session;


    public Studentdata(String name, Integer rollno, String phone, String userId, String email, String sex, String age, String session, String membershipStatus) {
        this.name = name;
        this.rollno = rollno;
        this.phone = phone;
        this.userId = userId;
        this.email = email;
        this.sex = sex;
        Age = age;
        this.session = session;
        memberShip = membershipStatus;
    }


    public Studentdata(String name, String phone, String userId, String email, String sex, String age) {
        this.name = name;
        this.phone = phone;
        this.userId = userId;
        this.email = email;
        this.sex = sex;
        Age = age;
    }

    public String getMemberShip() {
        return memberShip;
    }


    public void setMemberShip(String memberShip) {
        this.memberShip = memberShip;
    }

    public String getSession() {
        return session;
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
}
