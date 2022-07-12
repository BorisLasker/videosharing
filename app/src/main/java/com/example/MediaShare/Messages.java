package com.example.MediaShare;

public class Messages {


    private  String currentDateTime;
    String imageUrl;


    public String getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(String currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    public Messages(String imageUrl , String currentDateTimeString){

        this.imageUrl = imageUrl;
        this.currentDateTime= currentDateTimeString;

    }

    public Messages() {

    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
