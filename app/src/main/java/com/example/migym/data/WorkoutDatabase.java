package com.example.migym.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.migym.data.Workout;

@Database(entities = {Workout.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class WorkoutDatabase extends RoomDatabase {
    public abstract WorkoutDao workoutDao();

    private static volatile WorkoutDatabase INSTANCE;

    public static WorkoutDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WorkoutDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WorkoutDatabase.class, "workout_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}