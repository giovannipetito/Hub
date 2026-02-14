package it.giovanni.hub.domain.birthday.reminder

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

object BirthdayReminderScheduler {
    private const val UNIQUE_NAME = "birthday_reminder_9am"

    fun scheduleDaily10AM(context: Context) {
        val now = ZonedDateTime.now()
        val next10 = now.withHour(9).withMinute(0).withSecond(0).withNano(0).let {
            if (it.isAfter(now)) it else it.plusDays(1)
        }
        val initialDelayMs = Duration.between(now, next10).toMillis().coerceAtLeast(0)

        val request = PeriodicWorkRequestBuilder<BirthdayReminderWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(initialDelayMs, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.Companion.getInstance(context).enqueueUniquePeriodicWork(
            UNIQUE_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }
}