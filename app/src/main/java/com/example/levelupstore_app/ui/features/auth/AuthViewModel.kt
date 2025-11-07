// Ruta: com/example/levelupstore_app/ui/features/auth/AuthViewModel.kt
package com.example.levelupstore_app.ui.features.auth

import android.util.Patterns
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
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException // <-- Este es el que teníamos
import java.time.DateTimeException         // <-- ¡AÑADIMOS ESTE!

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val birthDate: String = "", // Guarda solo los 8 dígitos (DDMMYYYY)
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loginSuccess: Boolean = false,
    val registerSuccess: Boolean = false
)

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val sessionState: Flow<User?> = authRepository.getActiveUserStream()

    // --- EVENTOS (Funciones que la UI llamará) ---
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

    fun login() {
        if (uiState.value.isLoading) return
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            val user = authRepository.login(uiState.value.email, uiState.value.password)
            if (user != null) {
                _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Usuario o contraseña incorrectos") }
            }
        }
    }

    fun register() {
        if (uiState.value.isLoading) return
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        val email = uiState.value.email
        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update { it.copy(isLoading = false, errorMessage = "Por favor, ingresa un email válido.") }
            return
        }

        val birthDateString = uiState.value.birthDate // "27101995"

        if (birthDateString.length != 8) {
            _uiState.update { it.copy(isLoading = false, errorMessage = "Formato de fecha inválido. Usa DD-MM-YYYY.") }
            return
        }

        var age: Int
        var dateToSave: String // Formato AAAA-MM-DD para guardar

        // --- ¡BLOQUE CATCH CORREGIDO! ---
        try {
            val inputFormatter = DateTimeFormatter.ofPattern("ddMMuuuu")
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            val birthDate = LocalDate.parse(birthDateString, inputFormatter)
            val today = LocalDate.now()
            age = Period.between(birthDate, today).years

            if (age < 18) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Debes ser mayor de 18 años.") }
                return
            }

            dateToSave = birthDate.format(outputFormatter)

        } catch (e: DateTimeException) { // <-- ¡CAMBIO! Atrapa DateTimeException (más general)
            _uiState.update { it.copy(isLoading = false, errorMessage = "Fecha inválida (ej: día 32 o mes 13).") }
            return
        }
        // --- FIN DEL BLOQUE CATCH ---

        viewModelScope.launch {
            val success = authRepository.register(
                nombre = uiState.value.name,
                birthDate = dateToSave,
                email = uiState.value.email,
                clave = uiState.value.password
            )
            if (success) {
                _uiState.update { it.copy(isLoading = false, registerSuccess = true) }
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = "El email ya está registrado") }
            }
        }
    }

    fun logout() {
        viewModelScope.launch { authRepository.logout() }
    }
}