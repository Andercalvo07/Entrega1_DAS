package com.example.migym.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;
import java.util.List;
import java.util.Date;

import com.example.migym.data.Workout;

@Dao
@TypeConverters({Converters.class})
public interface WorkoutDao {
    @Query("SELECT * FROM workouts ORDER BY dayOfWeek ASC, time ASC")
    LiveData<List<Workout>> getAllWorkouts();

    @Query("SELECT * FROM workouts WHERE id = :id")
    LiveData<Workout> getWorkoutById(long id);

    @Query("SELECT * FROM workouts WHERE dayOfWeek = :dayOfWeek ORDER BY time ASC")
    LiveData<List<Workout>> getWorkoutsByDay(int dayOfWeek);

    @Query("SELECT * FROM workouts WHERE dayOfWeek = :dayOfWeek AND time = :time")
    List<Workout> getWorkoutsAtTimeSync(int dayOfWeek, String time);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Workout workout);

    @Update
    void update(Workout workout);

    @Delete
    void delete(Workout workout);

    @Query("DELETE FROM workouts")
    void deleteAll();

    @Query("DELETE FROM workouts WHERE id = :id")
    void deleteById(Long id);

    @Query("SELECT * FROM workouts ORDER BY dayOfWeek ASC, time ASC")
    List<Workout> getAllWorkoutsSync();
}