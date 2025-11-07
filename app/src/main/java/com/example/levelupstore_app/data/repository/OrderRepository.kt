package com.example.levelupstore_app.data.repository

import com.example.levelupstore_app.data.model.Order
import com.example.levelupstore_app.data.storage.OrderStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class OrderRepository(private val orderStorage: OrderStorage) {

    fun getOrdersStream(userEmail: String): Flow<List<Order>> {
        return orderStorage.ordersMapFlow.map { map ->
            map[userEmail] ?: emptyList()
        }
    }

    suspend fun addOrder(userEmail: String, order: Order) {
        val currentMap = orderStorage.ordersMapFlow.first()
        val currentList = currentMap[userEmail] ?: emptyList()
        val newList = listOf(order) + currentList
        orderStorage.saveOrdersMap(currentMap + (userEmail to newList))
    }
}