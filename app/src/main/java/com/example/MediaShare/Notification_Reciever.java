package com.example.MediaShare;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Notification_Reciever extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("asdf", "onCreate:1234 ");
        onReceive();

    }

    private void onReceive() {
        Intent activityIntent = new Intent(this, Main.class);
        //activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(activityIntent);
        // Start Services
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.setAction("STOP_ACTION");
        ContextCompat.startForegroundService(this, serviceIntent);

    }
}

