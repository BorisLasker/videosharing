package com.example.MediaShare;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public  String password;
    public String username;
    public String email;


    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

}