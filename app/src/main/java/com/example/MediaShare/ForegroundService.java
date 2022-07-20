package com.example.MediaShare;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Constants;

public class ForegroundService extends Service {
    private FirebaseDatabase firebaseDatabase;
    private boolean flag = true;
    private DatabaseReference databaseReference;
    private long Media_counter;
    private DatabaseReference mSearchedLocationReference;
    public static final String CHANNEL_ID_1 = "ForegroundServiceChannel";
    public static final String CHANNEL_ID_2 = "NotificationChannel";
    @Override
    public void onCreate() {

        super.onCreate();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        createNotificationChannel1();
        createNotificationChannel2();

        databaseReference.child("message").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Media_counter = snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent notificationIntent = new Intent(this, Notification_Reciever.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification Foreground_notification = new NotificationCompat.Builder(this, CHANNEL_ID_1)
                .setContentTitle("Foreground Service")
                .setContentText("Service is listening to new Media!")
                .setSmallIcon(R.drawable.icon)
                .build();

        Notification New_media_notification = new NotificationCompat.Builder(this, CHANNEL_ID_2)
                .setContentTitle("Media Share")
                .setContentText("New media is available!")
                .setSmallIcon(R.drawable.icon)
                .setContentIntent(pendingIntent)
                .build();

        if (intent.getAction() != null && intent.getAction().equals("STOP_ACTION")) {
            Log.i("qwer", "killed ");
            startForeground(1, Foreground_notification);
        }
        //do heavy work on a background thread
        //stopSelf();

        databaseReference.child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.i("123456", String.valueOf(snapshot.getChildrenCount()) + Media_counter);

                if(Media_counter != snapshot.getChildrenCount()){
                    Log.i("123456", "hererere");
                    Media_counter = snapshot.getChildrenCount();
                    startForeground(2,New_media_notification);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(flag){
            flag = false;
            Log.i("123456", "flaghere");
            startForeground(1, Foreground_notification);
        }

        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel1() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel1 = new NotificationChannel(
                    CHANNEL_ID_1,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_UNSPECIFIED
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel1);
        }
    }
    private void createNotificationChannel2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel2 = new NotificationChannel(
                    CHANNEL_ID_2,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel2);
        }
    }
}