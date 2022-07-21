package com.example.MediaShare;


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
import com.google.firebase.database.ValueEventListener;

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
        //Creates channels for the notification to be sent through.
        createNotificationChannel1();
        createNotificationChannel2();


        databaseReference.child("message").addListenerForSingleValueEvent(new ValueEventListener() {
            // counts the media on the database.
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Media_counter = snapshot.getChildrenCount();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });


    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // When User clicks on new media notification , moves to Notification_reciever.
        Intent notificationIntent = new Intent(this, Notification_Service_Handler.class);
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


        // called from notification reciever
        if (intent.getAction() != null && intent.getAction().equals("STOP_ACTION")) {

            //When user clicks on new media notifcation, Notification_Reciever restarts the service with "STOP_ACTION".
            startForeground(1, Foreground_notification);
        }

        //Listening to changes in the database.
        databaseReference.child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(Media_counter != snapshot.getChildrenCount()){
                    Media_counter = snapshot.getChildrenCount();
                    //When data changed, creates a new media notification.
                    startForeground(2, New_media_notification);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // First time notification, from login screen.
        if(flag){
            flag = false;
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
                    NotificationManager.IMPORTANCE_LOW
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