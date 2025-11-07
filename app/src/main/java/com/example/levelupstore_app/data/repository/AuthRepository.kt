// Ruta: com/example/levelupstore_app/data/repository/AuthRepository.kt
package com.example.levelupstore_app.data.repository

import android.content.Context
import com.example.levelupstore_app.data.model.User
import com.example.levelupstore_app.data.storage.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import java.io.IOException

class AuthRepository(
    private val context: Context,
    private val userPreferences: UserPreferences
) {

    private val json = Json { ignoreUnknownKeys = true }

    private fun getBaseUsers(): List<User> {
        return try {
            val jsonString = context.assets.open("usuarios.json")
                .bufferedReader()
                .use { it.readText() }
            json.decodeFromString<List<User>>(jsonString)
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun login(email: String, clave: String): User? {
        val baseUsers = getBaseUsers()
        val newUsers = userPreferences.newUsersFlow.first()
        val allUsers = baseUsers + newUsers
        val foundUser = allUsers.find { it.email == email && it.clave == clave }

        if (foundUser != null) {
            userPreferences.saveActiveUser(foundUser)
        }
        return foundUser
    }

    /**
     * Registra un nuevo usuario (¡ACTUALIZADO!)
     * Ahora recibe 'birthDate' en lugar de 'edad'.
     */
    suspend fun register(nombre: String, birthDate: String, email: String, clave: String): Boolean {
        // 1. Verifica si el email ya existe
        val baseUsers = getBaseUsers()
        val newUsers = userPreferences.newUsersFlow.first()
        val emailExists = (baseUsers + newUsers).any { it.email == email }

        if (emailExists) {
            return false // Falla: el email ya está en uso
        }

        // 2. Si no existe, crea y guarda el nuevo usuario
        val newUser = User(
            email = email,
            clave = clave,
            nombre = nombre,
            fechaNacimiento = birthDate // <-- CAMBIADO (antes era 'edad')
        )

        userPreferences.addNewUser(newUser) // Lo añade a 'usuariosNuevos'
        userPreferences.saveActiveUser(newUser) // Lo define como 'usuarioActivo'
        return true
    }

    suspend fun logout() {
        userPreferences.clearActiveUser()
    }

    fun getActiveUserStream(): Flow<User?> {
        return userPreferences.activeUserFlow
    }

    suspend fun updateProfile(updatedUser: User) {
        userPreferences.saveActiveUser(updatedUser)
        userPreferences.updateUserInNewList(updatedUser)
    }
}