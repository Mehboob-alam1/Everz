package com.example.everz;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new CyberFragment();
            case 1: return new Web3Fragment();
            case 2: return new XRFragment();
            case 3: return new IOTFragment();
            case 4: return new CharityFragment();
            default: return new CyberFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5; // Number of tabs
    }
}

