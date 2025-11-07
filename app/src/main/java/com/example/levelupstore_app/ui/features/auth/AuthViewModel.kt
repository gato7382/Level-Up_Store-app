// Ruta: com/example/levelupstore_app/ui/features/auth/AuthViewModel.kt
package com.example.levelupstore_app.ui.features.auth

import android.util.Patterns // <-- ¡IMPORTACIÓN AÑADIDA!
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupstore_app.data.model.User
import com.example.levelupstore_app.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeParseException

/**
 * Estado de la UI para las pantallas de Login y Registro.
 * (¡ACTUALIZADO! Se quitó 'age', se añadió 'birthDate')
 */
data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val birthDate: String = "", // <-- CAMBIADO (antes era 'age')
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loginSuccess: Boolean = false,
    val registerSuccess: Boolean = false
)

/**
 * El "Cerebro" (ViewModel) que maneja toda la lógica de autenticación.
 */
class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    // --- ESTADO ---
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    /**
     * Un "stream" que la app observará para saber si hay un usuario logueado.
     */
    val sessionState: Flow<User?> = authRepository.getActiveUserStream()

    // --- EVENTOS (Funciones que la UI llamará) ---
    // (¡ACTUALIZADO! 'onAgeChange' se reemplazó por 'onBirthDateChange')

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, errorMessage = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name, errorMessage = null) }
    }

    fun onBirthDateChange(date: String) {
        _uiState.update { it.copy(birthDate = date, errorMessage = null) }
    }

    /**
     * Lógica de Login (Sin cambios)
     */
    fun login() {
        if (uiState.value.isLoading) return
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val user = authRepository.login(
                email = uiState.value.email,
                clave = uiState.value.password
            )
            if (user != null) {
                _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
            } else {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Usuario o contraseña incorrectos")
                }
            }
        }
    }


    /**
     * Lógica de Registro (¡ACTUALIZADA con validación de email y fecha!)
     */
    fun register() {
        if (uiState.value.isLoading) return
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        // --- 1. VALIDACIÓN DE EMAIL (¡NUEVO!) ---
        val email = uiState.value.email
        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update { it.copy(isLoading = false, errorMessage = "Por favor, ingresa un email válido.") }
            return
        }

        // --- 2. VALIDACIÓN DE EDAD (¡NUEVO!) ---
        val birthDateString = uiState.value.birthDate
        var age = 0
        try {
            // Intenta parsear la fecha (espera AAAA-MM-DD)
            val birthDate = LocalDate.parse(birthDateString)
            val today = LocalDate.now()
            age = Period.between(birthDate, today).years

            if (age < 18) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Debes ser mayor de 18 años.") }
                return
            }
        } catch (e: DateTimeParseException) {
            _uiState.update { it.copy(isLoading = false, errorMessage = "Formato de fecha inválido. Usa AAAA-MM-DD.") }
            return
        }

        // --- 3. LÓGICA DE REGISTRO (¡ACTUALIZADA!) ---
        viewModelScope.launch {
            // Llama al repositorio (actualizado)
            val success = authRepository.register(
                nombre = uiState.value.name,
                birthDate = birthDateString, // Pasa la fecha de nacimiento
                email = uiState.value.email,
                clave = uiState.value.password
            )

            if (success) {
                _uiState.update { it.copy(isLoading = false, registerSuccess = true) }
            } else {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "El email ya está registrado")
                }
            }
        }
    }

    /**
     * Lógica de Logout (Sin cambios)
     */
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}