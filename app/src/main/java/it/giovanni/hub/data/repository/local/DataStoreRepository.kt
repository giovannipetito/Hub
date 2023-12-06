package it.giovanni.hub.data.repository.local

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
import kotlinx.coroutines.flow.map

class DataStoreRepository(context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "hub_preferences")
        val EMAIL_KEY = stringPreferencesKey(name = "email_key")
        val LOGIN_KEY = booleanPreferencesKey(name = "login_key")
        val IMAGE_URI_KEY = stringPreferencesKey(name = "image_uri")
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

    suspend fun saveImageUri(imageUri: String) {
        dataStore.edit { preferences ->
            preferences[IMAGE_URI_KEY] = imageUri
        }
    }

    fun getImageUri(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences())
                else throw exception
            }
            .map { preferences ->
                val savedImageUri: String = preferences[IMAGE_URI_KEY] ?: ""
                savedImageUri
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
                val savedState: Boolean = preferences[LOGIN_KEY] ?: false
                savedState
            }
    }
}