package it.giovanni.hub.domain.notification

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters

class BirthdayWorkerFactory : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            BirthdayReminderWorker::class.java.name -> {
                BirthdayReminderWorker(appContext, workerParameters)
            }
            else -> null
        }
    }
}