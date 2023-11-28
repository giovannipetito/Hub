package it.giovanni.hub.domain.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import it.giovanni.hub.utils.Constants.ACTION_SERVICE_CANCEL
import it.giovanni.hub.utils.Constants.ACTION_SERVICE_START
import it.giovanni.hub.utils.Constants.ACTION_SERVICE_STOP
import it.giovanni.hub.utils.Constants.NOTIFICATION_CHANNEL_ID
import it.giovanni.hub.utils.Constants.NOTIFICATION_CHANNEL_NAME
import it.giovanni.hub.utils.Constants.NOTIFICATION_ID
import it.giovanni.hub.utils.Constants.COUNTER_STATE
import it.giovanni.hub.utils.CounterState
import it.giovanni.hub.utils.Globals.formatTime
import it.giovanni.hub.utils.Globals.pad
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@ExperimentalAnimationApi
@AndroidEntryPoint
class CounterService : Service() {

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    private val binder = CounterBinder()

    private var duration: Duration = Duration.ZERO

    private lateinit var timer: Timer

    var seconds = mutableStateOf("00")
        private set

    var minutes = mutableStateOf("00")
        private set

    var hours = mutableStateOf("00")
        private set

    var currentState = mutableStateOf(CounterState.Idle)
        private set

    override fun onBind(p0: Intent?) = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.getStringExtra(COUNTER_STATE)) {
            CounterState.Started.name -> {
                setStopButton()
                startForegroundService()
                startCounter { hours, minutes, seconds ->
                    updateNotification(hours = hours, minutes = minutes, seconds = seconds)
                }
            }
            CounterState.Stopped.name -> {
                stopCounter()
                setResumeButton()
            }
            CounterState.Canceled.name -> {
                stopCounter()
                cancelCounter()
                stopForegroundService()
            }
        }
        intent?.action.let {
            when (it) {
                ACTION_SERVICE_START -> {
                    setStopButton()
                    startForegroundService()
                    startCounter { hours, minutes, seconds ->
                        updateNotification(hours = hours, minutes = minutes, seconds = seconds)
                    }
                }
                ACTION_SERVICE_STOP -> {
                    stopCounter()
                    setResumeButton()
                }
                ACTION_SERVICE_CANCEL -> {
                    stopCounter()
                    cancelCounter()
                    stopForegroundService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startCounter(onTick: (h: String, m: String, s: String) -> Unit) {
        currentState.value = CounterState.Started
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            duration = duration.plus(1.seconds)
            updateTimeUnits()
            onTick(hours.value, minutes.value, seconds.value)
        }
    }

    private fun stopCounter() {
        if (this::timer.isInitialized) {
            timer.cancel()
        }
        currentState.value = CounterState.Stopped
    }

    private fun cancelCounter() {
        duration = Duration.ZERO
        currentState.value = CounterState.Idle
        updateTimeUnits()
    }

    private fun updateTimeUnits() {
        duration.toComponents { hours, minutes, seconds, _ ->
            this@CounterService.hours.value = hours.toInt().pad()
            this@CounterService.minutes.value = minutes.pad()
            this@CounterService.seconds.value = seconds.pad()
        }
    }

    private fun startForegroundService() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun stopForegroundService() {
        notificationManager.cancel(NOTIFICATION_ID)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun updateNotification(hours: String, minutes: String, seconds: String) {
        notificationManager.notify(
            NOTIFICATION_ID,
            notificationBuilder.setContentText(
                formatTime(hours = hours, minutes = minutes, seconds = seconds)
            ).build()
        )
    }

    private fun setStopButton() {
        notificationBuilder.mActions.removeAt(0)
        notificationBuilder.mActions.add(
            0,
            NotificationCompat.Action(
                0,
                "Stop",
                ServiceHelper.stopPendingIntent(this)
            )
        )
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun setResumeButton() {
        notificationBuilder.mActions.removeAt(0)
        notificationBuilder.mActions.add(
            0,
            NotificationCompat.Action(
                0,
                "Resume",
                ServiceHelper.resumePendingIntent(this)
            )
        )
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    inner class CounterBinder : Binder() {
        fun getService(): CounterService = this@CounterService
    }
}