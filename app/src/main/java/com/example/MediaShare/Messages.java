package com.example.MediaShare;

public class Messages {


    private  String currentDateTimeString;
    String imageUrl;


    public String getCurrentDateTimeString() {
        return currentDateTimeString;
    }

    public void setCurrentDateTimeString(String currentDateTimeString) {
        this.currentDateTimeString = currentDateTimeString;
    }

    public Messages(String imageUrl , String currentDateTimeString){

        this.imageUrl = imageUrl;
        this.currentDateTimeString = currentDateTimeString;

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
