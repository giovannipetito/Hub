package it.giovanni.hub.domain.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

object BirthdayNotifications {
    const val CHANNEL_ID = "birthday_reminders"

    fun ensureChannel(context: Context) {

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Birthday reminders",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notifications for birthdays saved in the app"
        }

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.createNotificationChannel(channel)
    }
}