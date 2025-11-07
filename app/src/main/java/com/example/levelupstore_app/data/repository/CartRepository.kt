// Ruta: com/example/levelupstore_app/data/repository/CartRepository.kt
package com.example.levelupstore_app.data.repository

import com.example.levelupstore_app.data.model.CartItem
import com.example.levelupstore_app.data.model.Product
import com.example.levelupstore_app.data.storage.CartStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/**
 * Repositorio para la lógica del Carrito de Compras. Reemplaza cart.js
 *
 * @param cartStorage "Inyectamos" nuestro "localStorage" del carrito.
 */
class CartRepository(private val cartStorage: CartStorage) {

    /**
     * Obtiene un "stream" de la lista de items del carrito.
     */
    fun getCartItemsStream(): Flow<List<CartItem>> {
        return cartStorage.cartItemsFlow
    }

    /**
     * Añade un producto al carrito (como tu 'addItem' en cart.js)
     */
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

    /**
     * Elimina un producto del carrito (como tu 'removeItem' en cart.js)
     */
    suspend fun removeFromCart(productId: String) {
        val currentItems = cartStorage.cartItemsFlow.first()
        val newItems = currentItems.filter { it.product.id != productId }
        cartStorage.saveCartItems(newItems)
    }

    /**
     * Actualiza la cantidad de un item (como tu 'updateQuantity' en cart.js)
     */
    suspend fun updateQuantity(productId: String, newQuantity: Int) {
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

    /**
     * Vacía el carrito (como tu 'clearCart' en cart.js)
     */
    suspend fun clearCart() {
        cartStorage.saveCartItems(emptyList())
    }
}