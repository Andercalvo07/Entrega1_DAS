package com.example.migym.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.snackbar.Snackbar;

import com.example.migym.R;
import com.example.migym.adapters.WorkoutAdapter;
import com.example.migym.data.Workout;
import com.example.migym.databinding.FragmentWorkoutListBinding;
import com.example.migym.viewmodels.WorkoutViewModel;

public class WorkoutListFragment extends Fragment implements WorkoutAdapter.OnWorkoutClickListener {
    private FragmentWorkoutListBinding binding;
    private WorkoutViewModel workoutViewModel;
    private WorkoutAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWorkoutListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        setupViewModel();
        setupFab();
    }

    private void setupRecyclerView() {
        adapter = new WorkoutAdapter(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        workoutViewModel = new ViewModelProvider(requireActivity()).get(WorkoutViewModel.class);
        workoutViewModel.getAllWorkouts().observe(getViewLifecycleOwner(), workouts -> {
            adapter.submitList(workouts);
            binding.emptyView.setVisibility(workouts.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    private void setupFab() {
        binding.fab.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.action_workoutList_to_addWorkout);
        });
    }

    @Override
    public void onWorkoutClick(Workout workout) {
        workoutViewModel.setSelectedWorkout(workout);
        Bundle args = new Bundle();
        args.putLong("workoutId", workout.getId());
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_workoutList_to_detail, args);
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
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle(R.string.delete_workout)
                .setMessage(R.string.confirm_delete)
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    workoutViewModel.deleteWorkout(workout);
                    Snackbar.make(binding.getRoot(), R.string.workout_deleted, Snackbar.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}