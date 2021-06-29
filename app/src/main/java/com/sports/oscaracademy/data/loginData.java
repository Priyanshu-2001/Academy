package com.sports.oscaracademy.data;

public class loginData {
    public boolean isNew;
    public String userName , password , uid ;
    public boolean isCreated;

    public loginData(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public loginData(String userName, String password, String uid) {
        this.userName = userName;
        this.password = password;
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
