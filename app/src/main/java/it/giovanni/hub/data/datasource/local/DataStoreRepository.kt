package it.giovanni.hub.data.datasource.local

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import androidx.core.net.toUri

class DataStoreRepository(context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "hub_preferences")
        val EMAIL_KEY = stringPreferencesKey(name = "email_key")
        val LOGIN_KEY = booleanPreferencesKey(name = "login_key")
        val URI_KEY = stringPreferencesKey(name = "uri_key")
        val URI_STRING_KEY = stringPreferencesKey(name = "uri_string_key")
        val DARK_THEME_KEY = booleanPreferencesKey(name = "dark_theme_key")
        val DYNAMIC_COLOR_KEY = booleanPreferencesKey(name = "dynamic_color_key")
    }

    private val dataStore = context.dataStore

    suspend fun saveEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = email
        }
    }

    fun getEmail(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences())
                else throw exception
            }
            .map { preferences ->
                val savedEmail: String = preferences[EMAIL_KEY] ?: ""
                savedEmail
            }
    }

    suspend fun resetEmail() {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = ""
        }
    }

    suspend fun saveLoginState(state: Boolean) {
        dataStore.edit { preferences ->
            preferences[LOGIN_KEY] = state
        }
    }

    fun getLoginState(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences())
                else throw exception
            }
            .map { preferences ->
                val savedState: Boolean = preferences[LOGIN_KEY] == true
                savedState
            }
    }

    suspend fun saveUri(uri: Uri?) {
        dataStore.edit { preferences ->
            preferences[URI_KEY] = uri.toString()
        }
    }

    suspend fun getUri(): Uri? {
        val preferences = dataStore.data.first()
        val uriString = preferences[URI_KEY]
        return uriString?.toUri()
    }

    suspend fun saveUriString(uriString: String) {
        dataStore.edit { preferences ->
            preferences[URI_STRING_KEY] = uriString
        }
    }

    fun getUriString(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences())
                else throw exception
            }
            .map { preferences ->
                val uriString: String = preferences[URI_STRING_KEY] ?: ""
                uriString
        }
    }

    fun isDarkTheme(isDarkTheme: Boolean): Flow<Boolean> =
        dataStore.data
            .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
            .map { prefs -> prefs[DARK_THEME_KEY] ?: isDarkTheme }

    suspend fun setDarkTheme(theme: Boolean) {
        dataStore.edit { preferences ->
            preferences[DARK_THEME_KEY] = theme
        }
    }

    fun isDynamicColor(default: Boolean): Flow<Boolean> =
        dataStore.data
            .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
            .map { prefs -> prefs[DYNAMIC_COLOR_KEY] ?: default }

    suspend fun setDynamicColor(color: Boolean) {
        dataStore.edit { preferences ->
            preferences[DYNAMIC_COLOR_KEY] = color
        }
    }

    suspend fun resetTheme() {
        dataStore.edit { preferences ->
            preferences.remove(DARK_THEME_KEY)
            preferences.remove(DYNAMIC_COLOR_KEY)
        }
    }
}