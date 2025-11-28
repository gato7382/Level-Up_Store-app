package com.example.levelupstore_app.ui.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupstore_app.data.model.Order
import com.example.levelupstore_app.data.model.User
import com.example.levelupstore_app.data.repository.AuthRepository
import com.example.levelupstore_app.data.repository.OrderRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow // <-- IMPORTANTE
import kotlinx.coroutines.flow.flowOf // <-- IMPORTANTE
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Estado de la UI para la sección de Perfil (Datos y Pedidos).
 */
data class ProfileUiState(
    val user: User? = null,
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = true
)

/**
 * El "Cerebro" (ViewModel) de la sección de Perfil.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    // 1. Observa al usuario activo
    private val userStream: Flow<User?> = authRepository.getActiveUserStream()

    // 2. Observa los pedidos (CORREGIDO)
    private val ordersStream: Flow<List<Order>> = userStream.flatMapLatest { user ->
        if (user != null) {
            // --- CORRECCIÓN AQUÍ ---
            // Como el repositorio devuelve una List (no un Flow),
            // usamos 'flow { emit(...) }' para convertirlo.
            flow {
                // Llamamos a la API (suspend function)
                val ordersFromApi = orderRepository.getOrdersStream(user.email)
                // Emitimos el resultado al flujo
                emit(ordersFromApi)
            }
        } else {
            // Si no hay usuario, emitimos una lista vacía inmediatamente
            flowOf(emptyList())
        }
    }

    // 3. Combina ambos streams en un solo 'uiState'
    val uiState: StateFlow<ProfileUiState> =
        combine(userStream, ordersStream) { user, orders ->
            ProfileUiState(
                user = user,
                orders = orders,
                isLoading = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileUiState(isLoading = true)
        )

    /**
     * Llama al repositorio para actualizar el perfil.
     */
    fun updateProfile(updatedUser: User) {
        viewModelScope.launch {
            authRepository.updateProfile(updatedUser)
        }
    }

    /**
     * Llama al repositorio para cerrar la sesión.
     */
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}