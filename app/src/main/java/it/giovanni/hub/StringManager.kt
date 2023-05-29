package it.giovanni.hub

import android.util.Log

class StringManager {

    companion object {

        fun logString(string: String) {
            Log.i("[STRING]", "string: " + string)
        }
    }
}