package com.example.migym.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class WorkoutPagerAdapter extends FragmentStateAdapter {
    private static final int NUM_DAYS = 7;

    public WorkoutPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        WorkoutDayFragment fragment = new WorkoutDayFragment();
        Bundle args = new Bundle();
        args.putInt("dayOfWeek", position + 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return NUM_DAYS;
    }
} 