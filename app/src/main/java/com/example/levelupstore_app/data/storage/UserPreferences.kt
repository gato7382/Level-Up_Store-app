@file:OptIn(kotlinx.serialization.InternalSerializationApi::class) // Tu solución para el Opt-In
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

/**
 * Gestiona el almacenamiento persistente de los datos del usuario.
 * (Nuestro "localStorage" para 'usuarioActivo' y 'usuariosNuevos').
 */
class UserPreferences(private val context: Context) {

    // 1. Configuración del DataStore
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_prefs")
        val KEY_ACTIVE_USER = stringPreferencesKey("active_user_json")
        val KEY_NEW_USERS = stringPreferencesKey("new_users_json")
    }

    // 2. Funciones para el USUARIO ACTIVO

    /** Guarda el objeto User (como JSON) en DataStore */
    suspend fun saveActiveUser(user: User) {
        context.dataStore.edit { preferences ->
            val jsonString = Json.encodeToString(user)
            preferences[KEY_ACTIVE_USER] = jsonString
        }
    }

    /** Limpia el usuario activo (para logout) */
    suspend fun clearActiveUser() {
        context.dataStore.edit { preferences ->
            preferences.remove(KEY_ACTIVE_USER)
        }
    }

    /** Devuelve un "Flow" (stream) que emite el usuario activo */
    val activeUserFlow: Flow<User?> = context.dataStore.data.map { preferences ->
        preferences[KEY_ACTIVE_USER]?.let { jsonString ->
            try {
                Json.decodeFromString<User>(jsonString)
            } catch (e: Exception) {
                null
            }
        }
    }

    // 3. Funciones para la LISTA DE USUARIOS NUEVOS

    /** Devuelve un "Flow" (stream) de la lista de usuarios nuevos. */
    val newUsersFlow: Flow<List<User>> = context.dataStore.data.map { preferences ->
        preferences[KEY_NEW_USERS]?.let { jsonString ->
            try {
                Json.decodeFromString<List<User>>(jsonString)
            } catch (e: Exception) {
                emptyList()
            }
        } ?: emptyList()
    }

    /** Añade un nuevo usuario a la lista persistente */
    suspend fun addNewUser(newUser: User) {
        context.dataStore.edit { preferences ->
            val currentListJson = preferences[KEY_NEW_USERS]
            val currentList = currentListJson?.let {
                try {
                    Json.decodeFromString<List<User>>(it)
                } catch (e: Exception) { emptyList() }
            } ?: emptyList()

            val newList = currentList + newUser
            preferences[KEY_NEW_USERS] = Json.encodeToString(newList)
        }
    }

    /** Actualiza un usuario existente en la lista de 'usuariosNuevos' */
    suspend fun updateUserInNewList(updatedUser: User) {
        context.dataStore.edit { preferences ->
            val currentListJson = preferences[KEY_NEW_USERS]
            val currentList = currentListJson?.let {
                try {
                    Json.decodeFromString<List<User>>(it)
                } catch (e: Exception) { emptyList() }
            } ?: emptyList()

            val updatedList = currentList.map { existingUser ->
                if (existingUser.email == updatedUser.email) {
                    updatedUser
                } else {
                    existingUser
                }
            }
            preferences[KEY_NEW_USERS] = Json.encodeToString(updatedList)
        }
    }
}