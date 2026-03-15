package it.giovanni.hub.domain.birthday.reminder

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import it.giovanni.hub.MainActivity
import it.giovanni.hub.R
import it.giovanni.hub.data.database.BirthdayRoomDatabase
import java.time.LocalDate

class BirthdayReminderWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    // Same DB name you use in RoomDatabaseModule.kt
    private val db by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        Room.databaseBuilder(
            applicationContext,
            BirthdayRoomDatabase::class.java,
            "memo_database"
        ).build()
    }

    private val birthdayDao by lazy { db.birthdayDao() }

    override suspend fun doWork(): Result {
        BirthdayNotification.ensureChannel(applicationContext)

        val today = LocalDate.now()
        val birthdays = birthdayDao.readMemosForDay(today.monthValue, today.dayOfMonth)
        if (birthdays.isEmpty()) return Result.success()

        val title = "Birthdays today 🎉"
        val names = birthdays.joinToString(", ") { it.memo.trim() }
        val bigText = birthdays.joinToString("\n") { b ->
            val full = b.memo.trim()
            "• $full"
        }

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            1001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, BirthdayNotification.CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_audioslave_monochrome) // R.drawable.logo_audioslave
            .setContentTitle(title)
            .setContentText(names)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat.from(applicationContext).notify(9001, notification)
        }

        return Result.success()
    }
}