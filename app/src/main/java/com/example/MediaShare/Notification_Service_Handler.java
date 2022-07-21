package com.example.MediaShare;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Notification_Service_Handler extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onReceive();

    }

    private void onReceive() {
        Intent activityIntent = new Intent(this, LoginForm.class);
        this.startActivity(activityIntent);

        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.setAction("STOP_ACTION");
        ContextCompat.startForegroundService(this, serviceIntent);


    }
}

