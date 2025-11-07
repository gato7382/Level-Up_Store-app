package com.example.levelupstore_app.ui.features.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupstore_app.data.model.CartItem
import com.example.levelupstore_app.data.model.Order
import com.example.levelupstore_app.data.model.Product
import com.example.levelupstore_app.data.repository.AuthRepository
import com.example.levelupstore_app.data.repository.CartRepository
import com.example.levelupstore_app.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.flow.SharingStarted

data class CartDataState(
    val items: List<CartItem> = emptyList(),
    val totalItems: Int = 0,
    val totalPrice: Double = 0.0,
    val discountApplied: Boolean = false
)

class CartViewModel(
    private val cartRepository: CartRepository,
    private val authRepository: AuthRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    val cartDataState: StateFlow<CartDataState> =
        combine(
            cartRepository.getCartItemsStream(),
            authRepository.getActiveUserStream()
        ) { items, user ->

            val totalItems = items.sumOf { it.quantity }
            var totalPrice = items.sumOf { it.product.price * it.quantity }
            var discountApplied = false

            if (user?.email?.endsWith("@duocuc.cl") == true) {
                totalPrice *= 0.8
                discountApplied = true
            }

            CartDataState(
                items = items,
                totalItems = totalItems,
                totalPrice = totalPrice,
                discountApplied = discountApplied
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CartDataState()
        )

    private val _isCartOpen = MutableStateFlow(false)
    val isCartOpen: StateFlow<Boolean> = _isCartOpen.asStateFlow()

    fun toggleCart() {
        _isCartOpen.update { !it }
    }


    fun addToCart(product: Product) {
        viewModelScope.launch {
            cartRepository.addToCart(product)
        }
    }

    fun removeFromCart(productId: String) {
        viewModelScope.launch {
            cartRepository.removeFromCart(productId)
        }
    }

    fun updateQuantity(productId: String, newQuantity: Int) {
        viewModelScope.launch {
            cartRepository.updateQuantity(productId, newQuantity)
        }
    }

    suspend fun checkout(): String {
        val currentState = cartDataState.value
        val user = authRepository.getActiveUserStream().first()

        if (user == null) {
            return "Debes iniciar sesión para pagar."
        }
        if (currentState.items.isEmpty()) {
            return "Tu carrito está vacío."
        }

        val newOrder = Order(
            items = currentState.items,
            total = currentState.totalPrice,
            date = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
        )

        orderRepository.addOrder(user.email, newOrder)
        cartRepository.clearCart()
        _isCartOpen.update { false }

        return "¡Pedido realizado con éxito!"
    }
}