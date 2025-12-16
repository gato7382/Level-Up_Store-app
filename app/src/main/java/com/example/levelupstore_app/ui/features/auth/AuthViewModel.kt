package com.example.levelupstore_app.ui.features.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupstore_app.data.model.User
import com.example.levelupstore_app.data.network.RetrofitClient
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
import java.time.DateTimeException

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val birthDate: String = "", 
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loginSuccess: Boolean = false, // Indica si el LOGIN MANUAL fue exitoso
    val registerSuccess: Boolean = false
)

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val sessionState: Flow<User?> = authRepository.getActiveUserStream()

    init {
        viewModelScope.launch {
            authRepository.getAuthTokenStream().collect { token ->
                RetrofitClient.setAuthToken(token)
            }
        }
    }

    // --- EVENTOS ---
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

    // Resetea los estados de éxito para evitar navegaciones fantasma al volver al Login
    fun resetAuthStates() {
        _uiState.update { it.copy(loginSuccess = false, registerSuccess = false, errorMessage = null, isLoading = false) }
    }

    fun login() {
        if (uiState.value.isLoading) return
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            val result = authRepository.login(uiState.value.email, uiState.value.password)
            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Usuario o contraseña incorrectos"
                _uiState.update { it.copy(isLoading = false, errorMessage = errorMsg) }
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

        val birthDateString = uiState.value.birthDate 

        if (birthDateString.length != 8) {
            _uiState.update { it.copy(isLoading = false, errorMessage = "Formato de fecha inválido. Usa DD-MM-YYYY.") }
            return
        }

        var dateToSave: String 

        try {
            val inputFormatter = DateTimeFormatter.ofPattern("ddMMuuuu")
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            val birthDate = LocalDate.parse(birthDateString, inputFormatter)
            val today = LocalDate.now()
            val age = Period.between(birthDate, today).years

            if (age < 18) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Debes ser mayor de 18 años.") }
                return
            }

            dateToSave = birthDate.format(outputFormatter)

        } catch (e: DateTimeException) {
            _uiState.update { it.copy(isLoading = false, errorMessage = "Fecha inválida.") }
            return
        }

        viewModelScope.launch {
            val newUser = User(
                email = email,
                clave = uiState.value.password,
                nombre = uiState.value.name,
                fechaNacimiento = dateToSave
            )
            val result = authRepository.register(newUser)
            
            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false, registerSuccess = true) }
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Error en el registro"
                _uiState.update { it.copy(isLoading = false, errorMessage = errorMsg) }
            }
        }
    }

    fun logout() {
        viewModelScope.launch { 
            authRepository.logout()
            resetAuthStates() // IMPORTANTE: Limpiamos los flags de éxito para que al volver al Login no nos redirija automáticamente
        }
    }
}
