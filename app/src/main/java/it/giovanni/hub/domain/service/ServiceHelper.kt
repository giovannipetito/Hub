package it.giovanni.hub.domain.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import it.giovanni.hub.MainActivity
import it.giovanni.hub.utils.Constants.CANCEL_REQUEST_CODE
import it.giovanni.hub.utils.Constants.CLICK_REQUEST_CODE
import it.giovanni.hub.utils.Constants.RESUME_REQUEST_CODE
import it.giovanni.hub.utils.Constants.COUNTER_STATE
import it.giovanni.hub.utils.Constants.STOP_REQUEST_CODE
import it.giovanni.hub.utils.CounterState

object ServiceHelper {

    fun clickPendingIntent(context: Context): PendingIntent {
        val clickIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(COUNTER_STATE, CounterState.Started.name)
        }
        return PendingIntent.getActivity(context, CLICK_REQUEST_CODE, clickIntent, PendingIntent.FLAG_IMMUTABLE)
    }

    fun stopPendingIntent(context: Context): PendingIntent {
        val stopIntent = Intent(context, CounterService::class.java).apply {
            putExtra(COUNTER_STATE, CounterState.Stopped.name)
        }
        return PendingIntent.getService(context, STOP_REQUEST_CODE, stopIntent, PendingIntent.FLAG_IMMUTABLE)
    }

    fun resumePendingIntent(context: Context): PendingIntent {
        val resumeIntent = Intent(context, CounterService::class.java).apply {
            putExtra(COUNTER_STATE, CounterState.Started.name)
        }
        return PendingIntent.getService(context, RESUME_REQUEST_CODE, resumeIntent, PendingIntent.FLAG_IMMUTABLE)
    }

    fun cancelPendingIntent(context: Context): PendingIntent {
        val cancelIntent = Intent(context, CounterService::class.java).apply {
            putExtra(COUNTER_STATE, CounterState.Canceled.name)
        }
        return PendingIntent.getService(context, CANCEL_REQUEST_CODE, cancelIntent, PendingIntent.FLAG_IMMUTABLE)
    }

    fun triggerForegroundService(context: Context, action: String) {
        Intent(context, CounterService::class.java).apply {
            this.action = action
            context.startService(this)
        }
    }
}