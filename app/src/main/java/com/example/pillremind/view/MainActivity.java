package com.example.pillremind.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.pillremind.R;
import com.example.pillremind.adapter.ViewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nagivator);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        ViewPager2 viewPager2 = findViewById(R.id.view_pager);
        ViewAdapter viewAdapter = new ViewAdapter(this);
        viewPager2.setAdapter(viewAdapter);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.homepage) {
                viewPager2.setCurrentItem(0);
            } else if (itemId == R.id.meds) {
                viewPager2.setCurrentItem(1);
            } else if (itemId == R.id.schedule) {
                viewPager2.setCurrentItem(2);
            } else if (itemId == R.id.profile) {
                viewPager2.setCurrentItem(3);
            }
            return true;
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
        });
    }
}