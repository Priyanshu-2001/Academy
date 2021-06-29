package com.sports.oscaracademy.data;

public class signUpdata {
    private String username , email , pass ;

    public signUpdata(String username, String email, String pass) {
        this.username = username;
        this.email = email;
        this.pass = pass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
