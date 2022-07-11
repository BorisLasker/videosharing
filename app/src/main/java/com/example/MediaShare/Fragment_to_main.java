package com.example.MediaShare;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class Fragment_to_main extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_to_main);

        Intent intent = getIntent();
        int pos = 0;
        int position = intent.getIntExtra("position",pos);


        Bundle bundle = new Bundle();


        // Set Fragmentclass Arguments
        Fragment fragobj = new Info_Fragment();
        fragobj.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_info,fragobj)
                .commit();
/*
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container,new Info_Fragment());
        fragmentTransaction.commit();
*/

    }
}
