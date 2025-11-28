package com.example.levelupstore_app.data.repository

import android.content.Context
import android.util.Log
import com.example.levelupstore_app.data.model.User
import com.example.levelupstore_app.data.network.RetrofitClient
import com.example.levelupstore_app.data.storage.UserPreferences
import kotlinx.coroutines.flow.Flow

class AuthRepository(
    private val context: Context,
    private val userPreferences: UserPreferences
) {

    /**
     * Inicia sesión contra el Backend.
     * El backend debe devolver el objeto User con el campo 'isAdmin' correcto.
     */
    suspend fun login(email: String, clave: String): User? {
        return try {
            val loginBody = mapOf("email" to email, "password" to clave)

            // 1. Llamada a la API
            val user = RetrofitClient.instance.login(loginBody)

            // 2. Si la API responde con éxito, guardamos el usuario (y su rol admin) localmente
            userPreferences.saveActiveUser(user)

            Log.d("AuthRepo", "Login exitoso: ${user.nombre} (Admin: ${user.isAdmin})")
            user
        } catch (e: Exception) {
            Log.e("AuthRepo", "Error en login: ${e.message}")
            e.printStackTrace()
            null // Login fallido
        }
    }

    /**
     * Registra un usuario nuevo en el Backend.
     */
    suspend fun register(nombre: String, birthDate: String, email: String, clave: String): Boolean {
        return try {
            // 1. Crear el objeto User
            // Por defecto isAdmin es false, a menos que tu backend lo cambie
            val newUser = User(
                email = email,
                clave = clave,
                nombre = nombre,
                fechaNacimiento = birthDate,
                isAdmin = false
            )

            // 2. Llamada POST a la API
            val createdUser = RetrofitClient.instance.register(newUser)

            // 3. Auto-login: Guardamos el usuario creado como activo
            userPreferences.saveActiveUser(createdUser)

            Log.d("AuthRepo", "Registro exitoso: ${createdUser.email}")
            true
        } catch (e: Exception) {
            Log.e("AuthRepo", "Error en registro: ${e.message}")
            // Aquí podrías manejar errores específicos (ej. "Email ya existe")
            // dependiendo de lo que devuelva tu backend (400, 409, etc.)
            false
        }
    }

    suspend fun logout() {
        userPreferences.clearActiveUser()
    }

    fun getActiveUserStream(): Flow<User?> {
        return userPreferences.activeUserFlow
    }

    suspend fun updateProfile(updatedUser: User) {
        userPreferences.saveActiveUser(updatedUser)
    }
}