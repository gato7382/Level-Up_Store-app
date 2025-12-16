package com.example.levelupstore_app.data.repository

import com.example.levelupstore_app.data.model.CartItem
import com.example.levelupstore_app.data.model.Product
import com.example.levelupstore_app.data.storage.CartStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class CartRepository(private val cartStorage: CartStorage) {

    fun getCartItemsStream(): Flow<List<CartItem>> {
        return cartStorage.cartItemsFlow
    }

    suspend fun addToCart(product: Product) {
        val currentItems = cartStorage.cartItemsFlow.first()
        val existingItem = currentItems.find { it.product.id == product.id }

        val newItems = if (existingItem != null) {
            currentItems.map {
                if (it.product.id == product.id) it.copy(quantity = it.quantity + 1) else it
            }
        } else {
            currentItems + CartItem(product = product, quantity = 1)
        }
        cartStorage.saveCartItems(newItems)
    }

    suspend fun removeFromCart(productId: Long) {
        val currentItems = cartStorage.cartItemsFlow.first()
        val newItems = currentItems.filter { it.product.id != productId }
        cartStorage.saveCartItems(newItems)
    }

    suspend fun updateQuantity(productId: Long, newQuantity: Int) {
        if (newQuantity < 1) {
            removeFromCart(productId)
            return
        }
        val currentItems = cartStorage.cartItemsFlow.first()
        val newItems = currentItems.map {
            if (it.product.id == productId) it.copy(quantity = newQuantity) else it
        }
        cartStorage.saveCartItems(newItems)
    }

    suspend fun clearCart() {
        cartStorage.saveCartItems(emptyList())
    }
}
