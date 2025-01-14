package it.giovanni.hub.ui.widgets

import android.content.res.Resources

val Float.toPx get() = this * Resources.getSystem().displayMetrics.density