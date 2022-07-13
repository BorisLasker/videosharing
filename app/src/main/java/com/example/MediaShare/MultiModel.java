package com.example.MediaShare;

import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.stream.Stream;

public class MultiModel {
    public static final int IMAGE_TYPE = 0;
    public static final int VIDEO_TYPE = 1;

    public int type;
    public Messages data;
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

    public Messages getData() {
        return data;
    }

    public void setData(Messages data) {
        this.data = data;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MultiModel(int type, Messages data, String text) {
        this.type = type;
        this.data = data;
        this.text = text;
    }
}
