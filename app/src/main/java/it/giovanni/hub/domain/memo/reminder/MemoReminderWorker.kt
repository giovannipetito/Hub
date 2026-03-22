package it.giovanni.hub.domain.memo.reminder

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
import it.giovanni.hub.data.database.MemoRoomDatabase
import java.time.LocalDate

class MemoReminderWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    // Same DB name you use in RoomDatabaseModule.kt
    private val db by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        Room.databaseBuilder(
            applicationContext,
            MemoRoomDatabase::class.java,
            "memo_database"
        ).build()
    }

    private val memoDao by lazy { db.memoDao() }

    override suspend fun doWork(): Result {
        MemoNotification.ensureChannel(applicationContext)

        val today = LocalDate.now()
        val memos = memoDao.readMemosForDay(today.monthValue, today.dayOfMonth)
        if (memos.isEmpty()) return Result.success()

        val title = "Memos today 📝"
        val names = memos.joinToString(", ") { it.memo.trim() }
        val bigText = memos.joinToString("\n") { memo ->
            val full = memo.memo.trim()
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

        val notification = NotificationCompat.Builder(applicationContext, MemoNotification.CHANNEL_ID)
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