package it.giovanni.hub.domain

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import it.giovanni.hub.App
import it.giovanni.hub.R

class ForegroundService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification()
        startForeground(1, notification)
        return START_NOT_STICKY
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, App.NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("Your request is being processed")
            .setSmallIcon(R.drawable.ico_audioslave)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}