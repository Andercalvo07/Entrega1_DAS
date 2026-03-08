package com.example.migym.ui.daily

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.migym.R
import com.example.migym.databinding.FragmentDailyBinding
import com.example.migym.model.Workout
import com.google.android.material.tabs.TabLayout
import java.util.*

class DailyFragment : Fragment() {
    private var _binding: FragmentDailyBinding? = null
    private val binding get() = _binding!!
    private lateinit var workoutAdapter: DailyWorkoutAdapter
    private val dayWorkouts = mutableMapOf<String, MutableList<Workout>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTabLayout()
        setupRecyclerView()
        loadWorkouts()
    }

    private fun setupTabLayout() {
        val days = resources.getStringArray(R.array.days_of_week)
        days.forEach { day ->
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(day))
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    updateWorkoutList(it.text.toString())
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // Select current day by default
        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_WEEK)
        val tabIndex = (currentDay + 5) % 7 // Convert Sunday=1 to Monday=0
        binding.tabLayout.getTabAt(tabIndex)?.select()
    }

    private fun setupRecyclerView() {
        workoutAdapter = DailyWorkoutAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = workoutAdapter
        }
    }

    private fun loadWorkouts() {
        // TODO: Load workouts from database
        // For now, we'll use dummy data
        val dummyWorkouts = listOf(
            Workout("Morning Run", "Cardio", "Monday", 8, 0, 30),
            Workout("Weight Training", "Strength", "Monday", 10, 0, 45),
            Workout("Yoga", "Flexibility", "Tuesday", 9, 0, 60),
            // Add more dummy workouts as needed
        )

        // Group workouts by day
        dummyWorkouts.forEach { workout ->
            dayWorkouts.getOrPut(workout.day) { mutableListOf() }.add(workout)
        }

        // Update the list for the current tab
        binding.tabLayout.selectedTabPosition.let { position ->
            val day = binding.tabLayout.getTabAt(position)?.text.toString()
            updateWorkoutList(day)
        }
    }

    private fun updateWorkoutList(day: String) {
        val workouts = dayWorkouts[day] ?: emptyList()
        workoutAdapter.submitList(workouts)
        
        // Show/hide empty state
        if (workouts.isEmpty()) {
            binding.emptyState.visibility = View.VISIBLE
            binding.emptyStateText.text = getString(R.string.no_workouts_for_day, day)
        } else {
            binding.emptyState.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 