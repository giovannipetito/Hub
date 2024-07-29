package it.giovanni.hub.domain

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import it.giovanni.hub.MainActivity
import it.giovanni.hub.R
import it.giovanni.hub.data.ApiServiceClient
import it.giovanni.hub.data.request.NetworkRequest
import kotlinx.coroutines.delay

class NetworkWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        startForegroundService()

        val username = inputData.getString("username") ?: return Result.failure()
        val message = inputData.getString("message") ?: return Result.failure()

        return try {
            val networkRequest = NetworkRequest(username, message)
            val response = ApiServiceClient.sendMessage(networkRequest)
            if (response != null) {
                delay(10000)
                showNotification(response.reply)
                val outputData: Data = workDataOf("success" to response.reply)
                Result.success(outputData)
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            val outputData: Data = workDataOf("failure" to e.message)
            Result.failure(outputData)
        }
    }

    private fun startForegroundService() {
        val serviceIntent = Intent(applicationContext, ForegroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            applicationContext.startForegroundService(serviceIntent)
        } else {
            applicationContext.startService(serviceIntent)
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    private fun showNotification(reply: String?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(applicationContext, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    putExtra("reply", reply)
                }
                val pendingIntent: PendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

                val notification = NotificationCompat.Builder(applicationContext, "HubChannelId")
                    .setSmallIcon(R.drawable.ico_audioslave)
                    .setContentTitle("New reply")
                    .setContentText(reply)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build()

                NotificationManagerCompat.from(applicationContext).notify(1, notification)
            }
        }
    }
}