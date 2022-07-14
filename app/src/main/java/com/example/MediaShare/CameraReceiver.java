package com.example.MediaShare;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class CameraReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        String intent_action = intent.getAction();
        Log.i("lasker", "onReceive: ");

        if (intent_action.equals(Intent.ACTION_CAMERA_BUTTON) ) {
            abortBroadcast();
            Log.i("lasker", "onReceive: ");
            KeyEvent key = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);

            if ( key.getAction() == KeyEvent.ACTION_DOWN )
                Toast.makeText(context, "press", Toast.LENGTH_SHORT).show();
            else if ( key.getAction() == KeyEvent.ACTION_UP )
                Toast.makeText(context, "release", Toast.LENGTH_SHORT).show();
            else if ( key.getAction() == KeyEvent.ACTION_MULTIPLE )
                Toast.makeText(context, "multi", Toast.LENGTH_SHORT).show();
        }
    }


}