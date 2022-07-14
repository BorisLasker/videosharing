package com.example.MediaShare;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class Fragment_to_main extends AppCompatActivity implements  Media_Fragment.FragAListener, Info_Fragment.FragBListener{
    private Info_Fragment.FragBListener listener;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_to_main);

/*
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container,new Info_Fragment());
        fragmentTransaction.commit();
        /*
 */
    }

    @Override
    public void OnClickEventFragA(int position) {
        Info_Fragment Info_Fragment;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container, Info_Fragment.class, null,"FRAGB")
                    .addToBackStack("BBB")
                    .commit();
            getSupportFragmentManager().executePendingTransactions();
        }
        Info_Fragment = (Info_Fragment) getSupportFragmentManager().findFragmentByTag("FRAGB");

        }
        //Info_Fragment;


    }

