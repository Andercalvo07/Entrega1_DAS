package com.example.migym.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.google.android.material.snackbar.Snackbar;

import com.example.migym.R;
import com.example.migym.adapters.WorkoutAdapter;
import com.example.migym.data.Workout;
import com.example.migym.databinding.FragmentWorkoutBinding;
import com.example.migym.dialogs.AddWorkoutDialog;
import com.example.migym.viewmodels.WorkoutViewModel;
import java.util.List;

public class WorkoutFragment extends Fragment implements WorkoutAdapter.OnWorkoutClickListener, AddWorkoutDialog.OnWorkoutListener {
    private FragmentWorkoutBinding binding;
    private WorkoutViewModel workoutViewModel;
    private WorkoutAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWorkoutBinding.inflate(inflater, container, false);
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
            AddWorkoutDialog dialog = AddWorkoutDialog.newInstance(null);
            dialog.setListener(this);
            dialog.show(getChildFragmentManager(), "add_workout");
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
        new AlertDialog.Builder(requireContext())
                .setMessage(R.string.confirm_delete)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    workoutViewModel.deleteWorkout(workout);
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void handleWorkoutAdd(Workout workout) {
        workoutViewModel.addWorkout(workout, new WorkoutViewModel.OnWorkoutAddListener() {
            @Override
            public void onSuccess() {
                Snackbar.make(binding.getRoot(), R.string.workout_added, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onConflict(List<Workout> conflictingWorkouts) {
                Snackbar.make(binding.getRoot(), R.string.time_conflict, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onWorkoutCreated(Workout workout) {
        handleWorkoutAdd(workout);
    }

    @Override
    public void onWorkoutUpdated(Workout workout) {
        workoutViewModel.updateWorkout(workout);
        Snackbar.make(binding.getRoot(), R.string.workout_updated, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 