package com.example.pillremind.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.pillremind.view.HomeFragment;
import com.example.pillremind.view.PillFragment;
import com.example.pillremind.view.PrescriptionFragment;
import com.example.pillremind.view.ProfileFragment;

public class ViewAdapter extends FragmentStateAdapter {


    public ViewAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new HomeFragment();
        } else if (position == 1) {
            return new PillFragment();
        }else if (position == 2) {
            return new PrescriptionFragment();
        }else if (position == 3) {
            return new ProfileFragment();
        }
        return new HomeFragment();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}

