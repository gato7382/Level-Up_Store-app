package com.example.levelupstore_app.ui.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupstore_app.data.model.Order
import com.example.levelupstore_app.data.model.User
import com.example.levelupstore_app.data.repository.AuthRepository
import com.example.levelupstore_app.data.repository.OrderRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ProfileUiState(
    val user: User? = null,
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = true
)

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val userStream: Flow<User?> = authRepository.getActiveUserStream()

    private val ordersStream: Flow<List<Order>> = userStream.flatMapLatest { user ->
        if (user != null) {
            flow {
                // CORRECCIÓN: Usamos getOrders() sin argumentos (usa el token interno)
                val ordersFromApi = orderRepository.getOrders()
                emit(ordersFromApi)
            }
        } else {
            flowOf(emptyList())
        }
    }

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

    fun updateProfile(updatedUser: User) {
        viewModelScope.launch {
            authRepository.updateProfile(updatedUser)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
