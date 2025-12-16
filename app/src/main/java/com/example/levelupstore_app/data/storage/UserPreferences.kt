@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)
package com.example.levelupstore_app.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.levelupstore_app.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

class UserPreferences(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_prefs")
        val KEY_ACTIVE_USER = stringPreferencesKey("active_user_json")
        val KEY_AUTH_TOKEN = stringPreferencesKey("auth_token")
    }

    suspend fun saveActiveUser(user: User) {
        context.dataStore.edit { preferences ->
            val jsonString = Json.encodeToString(user)
            preferences[KEY_ACTIVE_USER] = jsonString
        }
    }

    suspend fun clearActiveUser() {
        context.dataStore.edit { preferences ->
            preferences.remove(KEY_ACTIVE_USER)
        }
    }

    val activeUserFlow: Flow<User?> = context.dataStore.data.map { preferences ->
        preferences[KEY_ACTIVE_USER]?.let {
            try {
                Json.decodeFromString<User>(it)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_AUTH_TOKEN] = token
        }
    }

    suspend fun clearAuthToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(KEY_AUTH_TOKEN)
        }
    }

    val authTokenFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[KEY_AUTH_TOKEN]
    }
}