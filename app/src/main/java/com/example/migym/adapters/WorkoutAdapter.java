package com.example.migym.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.migym.R;
import com.example.migym.data.Workout;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class WorkoutAdapter extends ListAdapter<Workout, WorkoutAdapter.WorkoutViewHolder> {
    private final OnWorkoutClickListener listener;

    public interface OnWorkoutClickListener {
        void onWorkoutClick(Workout workout);
        void onWorkoutLongClick(Workout workout);
        void onWorkoutDelete(Workout workout);
    }

    public WorkoutAdapter(OnWorkoutClickListener listener) {
        super(new WorkoutDiffCallback());
        this.listener = listener;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workout, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleText;
        private final TextView timeText;
        private final TextView typeText;
        private final TextView durationText;
        private final TextView locationText;
        private final ImageButton deleteButton;

        WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.workout_title);
            timeText = itemView.findViewById(R.id.workout_time);
            typeText = itemView.findViewById(R.id.workout_type);
            durationText = itemView.findViewById(R.id.workout_duration);
            locationText = itemView.findViewById(R.id.workout_location);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onWorkoutClick(getItem(position));
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onWorkoutLongClick(getItem(position));
                    return true;
                }
                return false;
            });

            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onWorkoutDelete(getItem(position));
                }
            });
        }

        void bind(Workout workout) {
            if (workout != null) {
                titleText.setText(String.valueOf(workout.getTitle()));
                timeText.setText(String.valueOf(workout.getTime()));
                typeText.setText(String.valueOf(workout.getType()));
                durationText.setText(String.format(Locale.getDefault(), "%d min", workout.getDuration()));
                locationText.setText(String.valueOf(workout.getLocation()));
            }
        }
    }

    private static class WorkoutDiffCallback extends DiffUtil.ItemCallback<Workout> {
        @Override
        public boolean areItemsTheSame(@NonNull Workout oldItem, @NonNull Workout newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Workout oldItem, @NonNull Workout newItem) {
            return oldItem.equals(newItem);
        }
    }
} 