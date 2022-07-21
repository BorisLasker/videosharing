package com.example.MediaShare;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.widget.Toast;

public class NetworkReceiver extends BroadcastReceiver {

    //When network connectivity changes, onRecieve start to work.
    @Override
    public void onReceive(Context context, Intent intent)
    {
        try
        {
            if (isOnline(context)) {
                Settings_Fragment.is_network_connect(true);

            } else {
                Settings_Fragment.is_network_connect(false);


            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    // Checks if the android is connected to the network
    public boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

}