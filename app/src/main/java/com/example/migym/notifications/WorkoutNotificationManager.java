package com.example.migym.notifications;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.example.migym.R;
import com.example.migym.data.Workout;
import java.util.Calendar;

public class WorkoutNotificationManager {
    private static final String CHANNEL_ID = "workout_notifications";
    private static final String CHANNEL_NAME = "Workout Notifications";
    private static final String CHANNEL_DESCRIPTION = "Notifications for upcoming workouts";
    private final Context context;
    private final NotificationManager notificationManager;
    private final AlarmManager alarmManager;

    public WorkoutNotificationManager(Application application) {
        this.context = application;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(CHANNEL_DESCRIPTION);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void scheduleWorkoutNotification(Workout workout) {
        if (workout == null || workout.getTime() == null) {
            return;
        }

        Calendar calendar = Calendar.getInstance();
        String[] timeParts = workout.getTime().split(":");
        if (timeParts.length < 2) {
            return;
        }

        try {
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);

            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.add(Calendar.MINUTE, -30); // Schedule notification 30 minutes before workout

            Intent intent = new Intent(context, WorkoutNotificationReceiver.class);
            intent.putExtra("workoutId", workout.getId());
            intent.putExtra("workoutTitle", workout.getTitle());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    workout.getId().intValue(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY * 7, // Repeat weekly
                    pendingIntent
            );
        } catch (NumberFormatException e) {
            // Handle invalid time format
        }
    }

    public void cancelWorkoutNotification(Long workoutId) {
        Intent intent = new Intent(context, WorkoutNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                workoutId.intValue(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        alarmManager.cancel(pendingIntent);
        notificationManager.cancel(workoutId.intValue());
    }

    public void showWorkoutNotification(long workoutId, String workoutTitle) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(context.getString(R.string.upcoming_workout))
                .setContentText(workoutTitle)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        notificationManager.notify((int) workoutId, builder.build());
    }

    public void cancelAllNotifications() {
        notificationManager.cancelAll();
    }
}