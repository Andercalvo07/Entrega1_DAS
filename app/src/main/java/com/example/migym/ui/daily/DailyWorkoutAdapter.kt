package com.example.migym.ui.daily

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.migym.databinding.ItemDailyWorkoutBinding
import com.example.migym.model.Workout

class DailyWorkoutAdapter : ListAdapter<Workout, DailyWorkoutAdapter.WorkoutViewHolder>(WorkoutDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val binding = ItemDailyWorkoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WorkoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class WorkoutViewHolder(
        private val binding: ItemDailyWorkoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(workout: Workout) {
            binding.apply {
                workoutTitle.text = workout.title
                workoutType.text = workout.type
                workoutTime.text = String.format("%02d:00", workout.hour)
                workoutDuration.text = binding.root.context.getString(
                    com.example.migym.R.string.duration_minutes,
                    workout.duration
                )
            }
        }
    }

    private class WorkoutDiffCallback : DiffUtil.ItemCallback<Workout>() {
        override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem.title == newItem.title && 
                   oldItem.day == newItem.day && 
                   oldItem.hour == newItem.hour
        }

        override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem == newItem
        }
    }
} 