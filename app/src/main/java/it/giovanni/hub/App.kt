package it.giovanni.hub

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import it.giovanni.hub.domain.notification.BirthdayWorkerFactory

@HiltAndroidApp
class App: Application(), Configuration.Provider {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "hub_notification_channel_id"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Hub Notification Channel Name",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Hub Notification Channel Description"
        }
        val notificationManager: NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        // Or: getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(BirthdayWorkerFactory())
            .build()
}