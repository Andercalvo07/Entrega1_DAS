package com.example.migym.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.example.migym.R;
import com.example.migym.data.Workout;
import com.example.migym.databinding.DialogAddWorkoutBinding;
import com.example.migym.viewmodels.WorkoutViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddWorkoutDialog extends DialogFragment {
    private DialogAddWorkoutBinding binding;
    private OnWorkoutListener listener;
    private Workout existingWorkout;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public interface OnWorkoutListener {
        void onWorkoutCreated(Workout workout);
        void onWorkoutUpdated(Workout workout);
    }

    public static AddWorkoutDialog newInstance(@Nullable Workout workout) {
        AddWorkoutDialog dialog = new AddWorkoutDialog();
        if (workout != null) {
            dialog.existingWorkout = workout;
        }
        return dialog;
    }

    public void setListener(OnWorkoutListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogAddWorkoutBinding.inflate(LayoutInflater.from(getContext()));

        setupSpinners();
        setupTimeEditText();

        if (existingWorkout != null) {
            loadWorkoutData();
        }

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext())
                .setTitle(existingWorkout != null ? R.string.edit_workout : R.string.add_workout)
                .setView(binding.getRoot())
                .setPositiveButton(R.string.save, null)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dismiss());

        AlertDialog dialog = builder.create();
        
        dialog.setOnShowListener(dialogInterface -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                if (validateAndSaveWorkout()) {
                    dialog.dismiss();
                }
            });
        });

        return dialog;
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.days_of_week,
                android.R.layout.simple_spinner_item
        );
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.daySpinner.setAdapter(dayAdapter);

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.workout_types,
                android.R.layout.simple_spinner_item
        );
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.typeSpinner.setAdapter(typeAdapter);

        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.workout_locations,
                android.R.layout.simple_spinner_item
        );
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.locationSpinner.setAdapter(locationAdapter);
    }

    private void setupTimeEditText() {
        binding.timeInput.setOnClickListener(v -> showTimePicker());
    }

    private void loadWorkoutData() {
        binding.titleInput.setText(existingWorkout.getTitle());
        binding.descriptionInput.setText(existingWorkout.getDescription());
        String[] locations = getResources().getStringArray(R.array.workout_locations);
        for (int i = 0; i < locations.length; i++) {
            if (locations[i].equals(existingWorkout.getLocation())) {
                binding.locationSpinner.setSelection(i);
                break;
            }
        }
        binding.daySpinner.setSelection(existingWorkout.getDayOfWeek() - 1);
        binding.typeSpinner.setSelection(existingWorkout.getType());
        binding.timeInput.setText(existingWorkout.getTime());
        binding.durationInput.setText(String.valueOf(existingWorkout.getDuration()));
    }

    private void showTimePicker() {
        String currentTime = binding.timeInput.getText().toString();
        int hour = 8;
        int minute = 0;

        if (!TextUtils.isEmpty(currentTime)) {
            String[] parts = currentTime.split(":");
            if (parts.length == 2) {
                try {
                    hour = Integer.parseInt(parts[0]);
                    minute = Integer.parseInt(parts[1]);
                } catch (NumberFormatException e) {
                    // Use default values if parsing fails
                }
            }
        }

        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(hour)
                .setMinute(minute)
                .setTitleText(R.string.select_time)
                .build();

        picker.addOnPositiveButtonClickListener(view -> {
            String time = String.format(Locale.getDefault(), "%02d:%02d", picker.getHour(), picker.getMinute());
            binding.timeInput.setText(time);
        });

        picker.show(getChildFragmentManager(), "timePicker");
    }

    private boolean validateAndSaveWorkout() {
        if (!validateFields()) {
            return false;
        }

        String title = binding.titleInput.getText().toString().trim();
        String description = binding.descriptionInput.getText().toString().trim();
        String location = binding.locationSpinner.getSelectedItem().toString();
        int dayOfWeek = binding.daySpinner.getSelectedItemPosition() + 1;
        int type = binding.typeSpinner.getSelectedItemPosition();
        String time = binding.timeInput.getText().toString();
        int duration = Integer.parseInt(binding.durationInput.getText().toString());

        Workout workout = new Workout();
        if (existingWorkout != null) {
            workout.setId(existingWorkout.getId());
        }
        workout.setTitle(title);
        workout.setDescription(description);
        workout.setType(type);
        workout.setLocation(location);
        workout.setDayOfWeek(String.valueOf(dayOfWeek));
        workout.setTime(time);
        workout.setDuration(duration);

        WorkoutViewModel viewModel = new ViewModelProvider(requireActivity()).get(WorkoutViewModel.class);
        if (existingWorkout != null) {
            viewModel.updateWorkout(workout);
            if (listener != null) {
                listener.onWorkoutUpdated(workout);
            }
        } else {
            viewModel.addWorkout(workout, new WorkoutViewModel.OnWorkoutAddListener() {
                @Override
                public void onSuccess() {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), R.string.workout_added, Toast.LENGTH_SHORT).show();
                    }
                    if (listener != null) {
                        listener.onWorkoutCreated(workout);
                    }
                }

                @Override
                public void onConflict(List<Workout> conflicts) {
                    showConflictDialog(conflicts, workout);
                }
            });
        }

        return true;
    }

    private boolean validateFields() {
        String title = binding.titleInput.getText().toString().trim();
        String timeStr = binding.timeInput.getText().toString();
        String durationStr = binding.durationInput.getText().toString();

        if (TextUtils.isEmpty(title)) {
            binding.titleInput.setError(getString(R.string.error_title_required));
            return false;
        }

        if (TextUtils.isEmpty(timeStr)) {
            binding.timeInput.setError(getString(R.string.error_time_required));
            return false;
        }

        // Validate time format
        try {
            timeFormat.parse(timeStr);
        } catch (ParseException e) {
            binding.timeInput.setError(getString(R.string.error_time_invalid));
            return false;
        }

        if (TextUtils.isEmpty(durationStr)) {
            binding.durationInput.setError(getString(R.string.error_duration_required));
            return false;
        }

        try {
            int duration = Integer.parseInt(durationStr);
            if (duration <= 0) {
                binding.durationInput.setError(getString(R.string.error_duration_positive));
                return false;
            }
        } catch (NumberFormatException e) {
            binding.durationInput.setError(getString(R.string.error_duration_invalid));
            return false;
        }

        return true;
    }

    private void showConflictDialog(List<Workout> conflicts, Workout newWorkout) {
        if (getContext() == null) return;

        StringBuilder message = new StringBuilder();
        message.append(getString(R.string.conflict_message)).append("\n\n");
        
        for (Workout conflict : conflicts) {
            message.append("- ").append(conflict.getTitle())
                  .append(" (").append(conflict.getTime()).append(")\n");
        }

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.conflict_title)
                .setMessage(message)
                .setPositiveButton(R.string.add_anyway, (dialog, which) -> {
                    WorkoutViewModel viewModel = new ViewModelProvider(requireActivity()).get(WorkoutViewModel.class);
                    viewModel.forceAddWorkout(newWorkout);
                    if (listener != null) {
                        listener.onWorkoutCreated(newWorkout);
                    }
                    dismiss();
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