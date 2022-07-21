package com.example.MediaShare;

public class MultiMedia {
    public static final int IMAGE_TYPE = 0;
    public static final int VIDEO_TYPE = 1;

    public int type;
    public Media data;
    public String text;

    public static int getImageType() {
        return IMAGE_TYPE;
    }

    public static int getVideoType() {
        return VIDEO_TYPE;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Media getData() {
        return data;
    }

    public void setData(Media data) {
        this.data = data;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MultiMedia(int type, Media data, String text) {
        this.type = type;
        this.data = data;
        this.text = text;
    }
}
