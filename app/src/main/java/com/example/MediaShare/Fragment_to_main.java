package com.example.MediaShare;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;


// this activity is used to move from one fragment to another
public class Fragment_to_main extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_to_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Intent intent = getIntent();
        String time = intent.getStringExtra("time");
        String email = intent.getStringExtra("email");
        String username = intent.getStringExtra("username");

        // Set Fragment class Arguments
        Bundle bundle = new Bundle();
        bundle.putString("time",time);
        bundle.putString("email",email);
        bundle.putString("username",username);


        // moving to info_fragment
        Info_Fragment myFragment = new Info_Fragment();
        myFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.fragment_container, myFragment).commit();

    }
}
