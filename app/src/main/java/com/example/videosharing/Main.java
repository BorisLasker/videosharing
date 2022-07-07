package com.example.videosharing;

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

        Adapter adapter = new Adapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new Media(),"Media");
        adapter.addFragment(new Settings(),"Settings");
        viewPager.setAdapter(adapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.exit,menu);
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
