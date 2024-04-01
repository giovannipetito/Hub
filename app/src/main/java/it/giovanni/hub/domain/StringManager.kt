package it.giovanni.hub.domain

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

sealed class StringManager {
    data class DynamicString(val value: String) : StringManager()
    class ResourceString(
        @StringRes val id: Int,
        val args: Array<Any> = arrayOf()
    ) : StringManager()

    @Composable
    fun getString(): String {
        return when (this) {
            is DynamicString -> value
            is ResourceString -> LocalContext.current.getString(id, *args)
        }
    }

    fun getString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is ResourceString -> context.getString(id, *args)
        }
    }
}