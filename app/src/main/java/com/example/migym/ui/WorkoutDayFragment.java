package com.example.migym.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.migym.adapters.WorkoutAdapter;
import com.example.migym.data.Workout;
import com.example.migym.databinding.FragmentWorkoutDayBinding;
import com.example.migym.dialogs.AddWorkoutDialog;
import com.example.migym.viewmodels.WorkoutViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class WorkoutDayFragment extends Fragment implements WorkoutAdapter.OnWorkoutClickListener, AddWorkoutDialog.OnWorkoutListener {
    private FragmentWorkoutDayBinding binding;
    private WorkoutViewModel workoutViewModel;
    private WorkoutAdapter adapter;
    private int dayOfWeek;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dayOfWeek = getArguments().getInt("dayOfWeek", 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWorkoutDayBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        workoutViewModel = new ViewModelProvider(requireActivity()).get(WorkoutViewModel.class);

        setupRecyclerView();
        setupFab();
        observeWorkouts();
    }

    private void setupRecyclerView() {
        adapter = new WorkoutAdapter(this);
        binding.workoutList.setAdapter(adapter);
        binding.workoutList.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void setupFab() {
        binding.addWorkoutFab.setOnClickListener(v -> showAddWorkoutDialog());
    }

    private void showAddWorkoutDialog() {
        AddWorkoutDialog dialog = new AddWorkoutDialog();
        dialog.setListener(this);
        dialog.show(getChildFragmentManager(), "AddWorkoutDialog");
    }

    private void observeWorkouts() {
        workoutViewModel.getWorkoutsByDay(dayOfWeek).observe(getViewLifecycleOwner(), workouts -> {
            adapter.submitList(workouts);
            updateEmptyState(workouts);
        });
    }

    private void updateEmptyState(List<Workout> workouts) {
        if (workouts == null || workouts.isEmpty()) {
            binding.emptyView.setVisibility(View.VISIBLE);
            binding.workoutList.setVisibility(View.GONE);
        } else {
            binding.emptyView.setVisibility(View.GONE);
            binding.workoutList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onWorkoutClick(Workout workout) {
        AddWorkoutDialog dialog = AddWorkoutDialog.newInstance(workout);
        dialog.setListener(this);
        dialog.show(getChildFragmentManager(), "edit_workout");
    }

    @Override
    public void onWorkoutLongClick(Workout workout) {
        showDeleteConfirmationDialog(workout);
    }

    @Override
    public void onWorkoutDelete(Workout workout) {
        showDeleteConfirmationDialog(workout);
    }

    private void showDeleteConfirmationDialog(Workout workout) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete Workout")
                .setMessage("Are you sure you want to delete this workout?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    workoutViewModel.deleteWorkout(workout);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onWorkoutCreated(Workout workout) {
        // No need to do anything here as the LiveData will update the UI
    }

    @Override
    public void onWorkoutUpdated(Workout workout) {
        // No need to do anything here as the LiveData will update the UI
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 