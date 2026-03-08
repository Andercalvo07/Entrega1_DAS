package com.example.migym.utils;

import com.example.migym.data.Workout;
import com.example.migym.calendar.WorkoutSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WorkoutConverter {
    private static final String[] days = {
        "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"
    };

    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public static Workout toWorkout(WorkoutSession session) {
        return new Workout(
            session.getId(),
            session.getTitle(),
            session.getDescription(),
            session.getLocation(),
            session.getDayOfWeek(),
            session.getType(),
            session.getTime(),
            session.getDuration()
        );
    }

    public static WorkoutSession toWorkoutSession(Workout workout) {
        WorkoutSession session = new WorkoutSession();
        session.setId(workout.getId());
        session.setTitle(workout.getTitle());
        session.setDescription(workout.getDescription());
        session.setLocation(workout.getLocation());
        session.setDayOfWeek(workout.getDayOfWeek());
        session.setType(workout.getType());
        session.setTime(workout.getTime());
        session.setDuration(workout.getDuration());
        return session;
    }

    public static String getDayName(int dayOfWeek) {
        if (dayOfWeek >= 0 && dayOfWeek < days.length) {
            return days[dayOfWeek];
        }
        return "Desconocido";
    }
    
    public static List<Workout> toWorkouts(List<WorkoutSession> sessions) {
        List<Workout> workouts = new ArrayList<>();
        for (WorkoutSession session : sessions) {
            workouts.add(toWorkout(session));
        }
        return workouts;
    }
    
    public static List<WorkoutSession> toWorkoutSessions(List<Workout> workouts) {
        List<WorkoutSession> sessions = new ArrayList<>();
        for (Workout workout : workouts) {
            sessions.add(toWorkoutSession(workout));
        }
        return sessions;
    }
} 