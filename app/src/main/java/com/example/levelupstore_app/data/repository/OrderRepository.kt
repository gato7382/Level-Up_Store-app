// Ruta: com/example/levelupstore_app/data/repository/OrderRepository.kt
package com.example.levelupstore_app.data.repository

import com.example.levelupstore_app.data.model.Order
import com.example.levelupstore_app.data.storage.OrderStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Repositorio para la lógica del Historial de Pedidos (Mis Pedidos).
 */
class OrderRepository(private val orderStorage: OrderStorage) {

    /**
     * Obtiene un "stream" de la lista de pedidos para UN usuario específico.
     */
    fun getOrdersStream(userEmail: String): Flow<List<Order>> {
        return orderStorage.ordersMapFlow.map { map ->
            map[userEmail] ?: emptyList()
        }
    }

    /**
     * Añade un nuevo pedido al historial de un usuario.
     */
    suspend fun addOrder(userEmail: String, order: Order) {
        val currentMap = orderStorage.ordersMapFlow.first()
        val currentList = currentMap[userEmail] ?: emptyList()
        val newList = listOf(order) + currentList
        orderStorage.saveOrdersMap(currentMap + (userEmail to newList))
    }
}