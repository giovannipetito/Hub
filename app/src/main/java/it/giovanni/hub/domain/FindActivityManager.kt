package it.giovanni.hub.domain

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.fragment.app.FragmentActivity

class FindActivityManager {
    private fun Context.findActivity1(): Activity? = when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity1()
        else -> null
    }

    fun Context.findActivity2(): Activity {
        var context = this
        while (context is ContextWrapper) {
            if (context is Activity) return context
            context = context.baseContext
        }
        throw IllegalStateException("No activity")
    }

    /*
    private fun Context.findAppCompatActivity1(): AppCompatActivity? = when (this) {
        is AppCompatActivity -> this
        is ContextWrapper -> baseContext.findAppCompatActivity1()
        else -> null
    }

    fun Context.findAppCompatActivity2(): AppCompatActivity {
        var context = this
        while (context is ContextWrapper) {
            if (context is AppCompatActivity) return context
            context = context.baseContext
        }
        throw IllegalStateException("No activity")
    }
    */

    private fun Context.findFragmentActivity1(): FragmentActivity? = when (this) {
        is FragmentActivity -> this
        is ContextWrapper -> baseContext.findFragmentActivity1()
        else -> null
    }

    fun Context.findFragmentActivity2(): FragmentActivity {
        var context = this
        while (context is ContextWrapper) {
            if (context is FragmentActivity) return context
            context = context.baseContext
        }
        throw IllegalStateException("No activity")
    }

    private fun Context.findComponentActivity1(): ComponentActivity? = when (this) {
        is ComponentActivity -> this
        is ContextWrapper -> baseContext.findComponentActivity1()
        else -> null
    }

    fun Context.findComponentActivity2(): ComponentActivity {
        var context = this
        while (context is ContextWrapper) {
            if (context is ComponentActivity) return context
            context = context.baseContext
        }
        throw IllegalStateException("No activity")
    }
}