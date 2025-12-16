package com.example.levelupstore_app.data.repository

import android.util.Log
import com.example.levelupstore_app.data.model.User
import com.example.levelupstore_app.data.network.RetrofitClient
import com.example.levelupstore_app.data.storage.UserPreferences
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

class AuthRepository(
    private val userPreferences: UserPreferences
) {

    suspend fun login(email: String, clave: String): Result<User> {
        return try {
            val loginBody = mapOf("email" to email, "clave" to clave)

            val loginResponse = RetrofitClient.instance.login(loginBody)
            val token = loginResponse.token

            userPreferences.saveAuthToken(token)
            RetrofitClient.setAuthToken(token)

            val user = RetrofitClient.instance.getProfile()
            userPreferences.saveActiveUser(user)

            Result.success(user)
        } catch (e: HttpException) {
            val errorMsg = when (e.code()) {
                401, 403 -> "Credenciales incorrectas"
                404 -> "Usuario no encontrado"
                else -> "Error del servidor: ${e.code()}"
            }
            Result.failure(Exception(errorMsg))
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión. Revisa tu internet."))
        } catch (e: Exception) {
            Log.e("AuthRepo", "Error en login", e)
            Result.failure(Exception("Error inesperado: ${e.message}"))
        }
    }

    suspend fun register(user: User): Result<Boolean> {
        return try {
            RetrofitClient.instance.register(user)

            if (user.clave != null) {
                 val loginResult = login(user.email, user.clave)
                 if (loginResult.isSuccess) {
                     Result.success(true)
                 } else {
                     Result.success(false) 
                 }
            } else {
                Result.success(true)
            }
        } catch (e: HttpException) {
            val errorMsg = when (e.code()) {
                409 -> "El correo ya está registrado"
                400 -> "Datos inválidos"
                else -> "Error del servidor: ${e.code()}"
            }
            Result.failure(Exception(errorMsg))
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión"))
        } catch (e: Exception) {
            Log.e("AuthRepo", "Error en registro", e)
            Result.failure(e)
        }
    }

    suspend fun logout() {
        userPreferences.clearActiveUser()
        userPreferences.clearAuthToken()
        RetrofitClient.setAuthToken(null)
    }

    fun getActiveUserStream(): Flow<User?> {
        return userPreferences.activeUserFlow
    }
    
    fun getAuthTokenStream(): Flow<String?> {
        return userPreferences.authTokenFlow
    }

    suspend fun updateProfile(updatedUser: User): Result<User> {
        return try {
            val resultUser = RetrofitClient.instance.updateProfile(updatedUser)
            userPreferences.saveActiveUser(resultUser)
            Result.success(resultUser)
        } catch (e: Exception) {
             Log.e("AuthRepo", "Error actualizando perfil", e)
             Result.failure(e)
        }
    }
}
