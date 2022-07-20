package com.example.MediaShare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.security.AccessController;

public class Initiali extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "New media available!");
        ContextCompat.startForegroundService(this, serviceIntent);



        Intent intent = new Intent(this, LoginForm.class);
        startActivity(intent);
    }
}