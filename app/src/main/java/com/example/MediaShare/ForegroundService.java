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
    private DatabaseReference databaseReference;
    private long Media_counter;
    private DatabaseReference mSearchedLocationReference;
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    @Override
    public void onCreate() {

        super.onCreate();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        createNotificationChannel();

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

        if (intent.getAction() != null && intent.getAction().equals("STOP_ACTION")) {
            stopForeground(true);
        }



        String input = intent.getStringExtra("inputExtra");

        Intent notificationIntent = new Intent(this, Notification_Reciever.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        //Intent notification_intent = new Intent(this, Notification_Reciever.class);
        //PendingIntent contentIntent = PendingIntent.getBroadcast(this, 0, notification_intent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.icon)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        //do heavy work on a background thread
        //stopSelf();

        databaseReference.child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.i("123456", String.valueOf(snapshot.getChildrenCount()));
                try {
                    if(Media_counter != snapshot.getChildrenCount()){
                        Thread.sleep(2000);
                        startForeground(1, notification);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return START_NOT_STICKY;
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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}