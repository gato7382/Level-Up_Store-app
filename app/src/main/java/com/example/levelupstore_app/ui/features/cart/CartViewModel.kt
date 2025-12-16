package com.example.levelupstore_app.ui.features.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupstore_app.data.model.CartItem
import com.example.levelupstore_app.data.model.Order
import com.example.levelupstore_app.data.model.OrderItem
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
            var totalPrice = items.sumOf { (it.product.price ?: 0.0) * it.quantity }
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

    // Aceptamos Long o String y convertimos
    fun removeFromCart(productId: Any) {
        val id = when (productId) {
            is Long -> productId
            is String -> productId.toLongOrNull()
            else -> null
        }
        if (id != null) {
            viewModelScope.launch {
                cartRepository.removeFromCart(id)
            }
        }
    }

    fun updateQuantity(productId: Any, newQuantity: Int) {
        val id = when (productId) {
            is Long -> productId
            is String -> productId.toLongOrNull()
            else -> null
        }
        if (id != null) {
            viewModelScope.launch {
                cartRepository.updateQuantity(id, newQuantity)
            }
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

        // Mapear CartItems a OrderItems
        val orderItems = currentState.items.map { cartItem ->
            OrderItem(
                productId = cartItem.product.id ?: 0,
                quantity = cartItem.quantity,
                price = cartItem.product.price ?: 0.0,
                productName = cartItem.product.name,
                productImageUrl = cartItem.product.imageUrl ?: ""
            )
        }

        val newOrder = Order(
            items = orderItems,
            total = currentState.totalPrice
        )

        val result = orderRepository.createOrder(newOrder)
        
        if (result.isSuccess) {
            cartRepository.clearCart()
            _isCartOpen.update { false }
            return "¡Pedido realizado con éxito!"
        } else {
            return "Error al procesar el pedido: ${result.exceptionOrNull()?.message}"
        }
    }
}
