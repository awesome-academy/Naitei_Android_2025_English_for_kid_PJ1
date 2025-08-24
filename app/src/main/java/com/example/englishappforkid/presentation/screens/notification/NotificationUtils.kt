package com.example.englishappforkid.presentation.screens.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

fun createNotificationUtil(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel =
            NotificationChannel(
                "english_reminder_channel",
                "English Reminder",
                NotificationManager.IMPORTANCE_HIGH,
            ).apply {
                description = "Channel for daily English learning reminders"
            }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
