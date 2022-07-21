package com.example.MediaShare;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class Main extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab);

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);

        tabLayout.setupWithViewPager(viewPager);

        // Method to create adapter with tabs, Each tab is a fragment.
        // Adapter_Tabs is called
        Adapter_Tabs adapter = new Adapter_Tabs(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        //Extra data that was transfered from login page.
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String username = intent.getStringExtra("username");

        //transfer the User data to the fragments
        Bundle bundle = new Bundle();
        bundle.putString("email",email);
        bundle.putString("username",username);

        Settings_Fragment fragment = new Settings_Fragment();
        fragment.setArguments(bundle);

        Media_Fragment fragment_media = new Media_Fragment();
        fragment_media.setArguments(bundle);



        adapter.AddFragmentMedia(fragment_media,"Media");
        adapter.AddFragmentSettings(fragment,"Settings");
        viewPager.setAdapter(adapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){


            case R.id.exit:
                showDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }


    private void showDialog() {
        FragmentManager fm = getSupportFragmentManager();
        MyAlertDialogFragment alertDialog =MyAlertDialogFragment.newInstance("Closing the application","Are you sure","Yes","No");
        alertDialog.show(fm, "fragment_alert");

    }
}
