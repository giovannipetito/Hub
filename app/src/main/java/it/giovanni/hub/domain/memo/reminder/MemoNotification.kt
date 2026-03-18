package it.giovanni.hub.domain.memo.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

object MemoNotification {
    const val CHANNEL_ID = "memo_reminders"

    fun ensureChannel(context: Context) {

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Memo reminders",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notifications for events saved in the app"
        }

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.createNotificationChannel(channel)
    }
}