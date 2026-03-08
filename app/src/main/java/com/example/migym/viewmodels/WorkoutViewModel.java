package com.example.migym.viewmodels;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.CalendarContract;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import java.util.Date;

import com.example.migym.data.Workout;
import com.example.migym.repositories.WorkoutRepository;
import com.example.migym.notifications.WorkoutNotificationManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Calendar;

public class WorkoutViewModel extends AndroidViewModel {
    private final WorkoutRepository repository;
    private final LiveData<List<Workout>> allWorkouts;
    private final MutableLiveData<Workout> selectedWorkout = new MutableLiveData<>();
    private final ExecutorService executorService;
    private final WorkoutNotificationManager notificationManager;

    public interface OnWorkoutAddListener {
        void onSuccess();
        void onConflict(List<Workout> conflictingWorkouts);
    }

    public WorkoutViewModel(Application application) {
        super(application);
        repository = new WorkoutRepository(application);
        allWorkouts = repository.getAllWorkouts();
        executorService = Executors.newSingleThreadExecutor();
        notificationManager = new WorkoutNotificationManager(application);
    }

    public LiveData<List<Workout>> getAllWorkouts() {
        return allWorkouts;
    }

    public LiveData<Workout> getSelectedWorkout() {
        return selectedWorkout;
    }

    public void setSelectedWorkout(Workout workout) {
        selectedWorkout.setValue(workout);
    }

    public void addWorkout(Workout workout, OnWorkoutAddListener listener) {
        executorService.execute(() -> {
            try {
                workout.setId(null);
                if (hasTimeConflict(workout)) {
                    List<Workout> conflicts = repository.getWorkoutsAtTimeSync(workout.getDayOfWeek(), workout.getTime());
                    if (conflicts != null && !conflicts.isEmpty()) {
                        listener.onConflict(conflicts);
                        return;
                    }
                }
                repository.insert(workout);
                notificationManager.scheduleWorkoutNotification(workout);
                listener.onSuccess();
            } catch (Exception e) {
                // Handle any database errors
                e.printStackTrace();
            }
        });
    }

    public void updateWorkout(Workout workout) {
        executorService.execute(() -> {
            repository.update(workout);
            notificationManager.scheduleWorkoutNotification(workout);
        });
    }

    public void deleteWorkout(Workout workout) {
        executorService.execute(() -> {
            repository.delete(workout);
            notificationManager.cancelWorkoutNotification(workout.getId());
        });
    }

    public LiveData<Workout> getWorkoutById(long id) {
        return repository.getWorkoutById(id);
    }

    public LiveData<List<Workout>> getWorkoutsByDay(int dayOfWeek) {
        return repository.getWorkoutsByDay(dayOfWeek);
    }

    public void insert(Workout workout) {
        repository.insert(workout);
        notificationManager.scheduleWorkoutNotification(workout);
    }

    public void update(Workout workout) {
        repository.update(workout);
        notificationManager.cancelWorkoutNotification(workout.getId());
        notificationManager.scheduleWorkoutNotification(workout);
    }

    public void delete(Workout workout) {
        repository.delete(workout);
        notificationManager.cancelWorkoutNotification(workout.getId());
    }

    public void deleteAll() {
        repository.deleteAll();
        notificationManager.cancelAllNotifications();
    }

    private boolean hasTimeConflict(Workout workout) {
        List<Workout> conflicts = repository.getWorkoutsAtTimeSync(workout.getDayOfWeek(), workout.getTime());
        return conflicts != null && !conflicts.isEmpty();
    }

    public void deleteAllWorkouts() {
        executorService.execute(() -> {
            repository.deleteAll();
        });
    }

    public void addWorkoutToCalendar(Workout workout) {
        ContentResolver contentResolver = getApplication().getContentResolver();
        ContentValues values = new ContentValues();
        
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.TITLE, workout.getTitle());
        values.put(CalendarContract.Events.DESCRIPTION, workout.getDescription());
        values.put(CalendarContract.Events.EVENT_LOCATION, workout.getLocation());
        values.put(CalendarContract.Events.DURATION, String.valueOf(workout.getDuration() * 60 * 1000));
        values.put(CalendarContract.Events.ALL_DAY, 0);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "UTC");

        Uri uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values);
        if (uri != null) {
            long eventId = Long.parseLong(uri.getLastPathSegment());
            // You might want to store this eventId with the workout for future reference
        }
    }

    public void forceAddWorkout(Workout workout) {
        executorService.execute(() -> {
            try {
                workout.setId(null);
                repository.insert(workout);
                notificationManager.scheduleWorkoutNotification(workout);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
} 