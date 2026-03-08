package com.example.migym.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class WorkoutPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 7 // One for each day of the week

    override fun createFragment(position: Int): Fragment {
        return WorkoutDayFragment.newInstance(position)
    }
} 