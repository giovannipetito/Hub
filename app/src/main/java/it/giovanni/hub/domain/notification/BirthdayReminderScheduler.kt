package it.giovanni.hub.domain.notification

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

object BirthdayReminderScheduler {
    private const val UNIQUE_NAME = "birthday_reminder_10am"

    fun scheduleDaily10AM(context: Context) {
        val now = ZonedDateTime.now()
        val next10 = now.withHour(10).withMinute(0).withSecond(0).withNano(0).let {
            if (it.isAfter(now)) it else it.plusDays(1)
        }
        val initialDelayMs = Duration.between(now, next10).toMillis().coerceAtLeast(0)

        val request = PeriodicWorkRequestBuilder<BirthdayReminderWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(initialDelayMs, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            UNIQUE_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    fun testScheduler(context: Context, seconds: Long = 10) {
        val req = OneTimeWorkRequestBuilder<BirthdayReminderWorker>()
            .setInitialDelay(seconds, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(context).enqueue(req)
    }
}