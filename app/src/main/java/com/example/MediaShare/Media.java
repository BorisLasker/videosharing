package com.example.MediaShare;

public class Media {


    private  String username;
    private  String email;
    private  String currentDateTime;
    String imageUrl;

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

    public Media(String imageUrl, String currentDateTime, String email, String username) {
        this.imageUrl = imageUrl;
        this.currentDateTime= currentDateTime;
        this.email = email;
        this.username = username;
    }


    public String getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(String currentDateTime) {
        this.currentDateTime = currentDateTime;
    }


    public Media() {

    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
