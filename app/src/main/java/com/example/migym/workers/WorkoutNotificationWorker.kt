package com.example.dasproyectonew.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.dasproyectonew.R

class WorkoutNotificationWorker(
    private val context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        val workoutTitle = inputData.getString(KEY_WORKOUT_TITLE) ?: return Result.failure()
        val notificationType = inputData.getString(KEY_NOTIFICATION_TYPE) ?: return Result.failure()

        createNotificationChannel()

        val notification = when (notificationType) {
            TYPE_REMINDER -> createReminderNotification(workoutTitle)
            TYPE_COMPLETION -> createCompletionNotification(workoutTitle)
            else -> return Result.failure()
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)

        return Result.success()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Workout Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for workout reminders and completion"
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createReminderNotification(workoutTitle: String) = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle("Upcoming Workout")
        .setContentText("Your $workoutTitle starts in 1 hour!")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .build()

    private fun createCompletionNotification(workoutTitle: String) = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle("Workout Complete")
        .setContentText("How was your $workoutTitle? Rate your performance!")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .build()

    companion object {
        const val CHANNEL_ID = "workout_notifications"
        const val KEY_WORKOUT_TITLE = "workout_title"
        const val KEY_NOTIFICATION_TYPE = "notification_type"
        const val TYPE_REMINDER = "reminder"
        const val TYPE_COMPLETION = "completion"
    }
} 