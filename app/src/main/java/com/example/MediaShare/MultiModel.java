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

    public MultiModel(int type, Messages data, String text) {
        this.type = type;
        this.data = data;
        this.text = text;
    }
}
