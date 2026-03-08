package com.example.migym.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.migym.data.AppDatabase;
import com.example.migym.data.Workout;
import com.example.migym.data.WorkoutDao;

public class WorkoutRepository {
    private final WorkoutDao workoutDao;
    private final ExecutorService executorService;

    public WorkoutRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        workoutDao = db.workoutDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Workout>> getAllWorkouts() {
        return workoutDao.getAllWorkouts();
    }

    public LiveData<List<Workout>> getWorkoutsByDay(int dayOfWeek) {
        return workoutDao.getWorkoutsByDay(dayOfWeek);
    }

    public LiveData<Workout> getWorkoutById(long id) {
        return workoutDao.getWorkoutById(id);
    }

    public List<Workout> getWorkoutsAtTimeSync(int dayOfWeek, String time) {
        return workoutDao.getWorkoutsAtTimeSync(dayOfWeek, time);
    }

    public void insert(Workout workout) {
        executorService.execute(() -> {
            long id = workoutDao.insert(workout);
            workout.setId(id);
        });
    }

    public void update(Workout workout) {
        executorService.execute(() -> workoutDao.update(workout));
    }

    public void delete(Workout workout) {
        executorService.execute(() -> workoutDao.delete(workout));
    }

    public void deleteAll() {
        executorService.execute(() -> workoutDao.deleteAll());
    }
} 