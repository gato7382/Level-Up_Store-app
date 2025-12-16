package com.example.levelupstore_app.data.repository

import android.util.Log
import com.example.levelupstore_app.data.model.Order
import com.example.levelupstore_app.data.network.RetrofitClient

class OrderRepository {

    suspend fun getOrders(): List<Order> {
        return try {
            RetrofitClient.instance.getOrders()
        } catch (e: Exception) {
            Log.e("OrderRepo", "Error fetching orders", e)
            emptyList()
        }
    }

    suspend fun createOrder(order: Order): Result<Order> {
        return try {
            val createdOrder = RetrofitClient.instance.createOrder(order)
            Result.success(createdOrder)
        } catch (e: Exception) {
            Log.e("OrderRepo", "Error creating order", e)
            Result.failure(e)
        }
    }
}
