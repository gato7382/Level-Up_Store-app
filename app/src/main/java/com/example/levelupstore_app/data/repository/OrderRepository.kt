package com.example.levelupstore_app.data.repository

import android.util.Log
import com.example.levelupstore_app.data.model.Order
import com.example.levelupstore_app.data.network.RetrofitClient
// Ya no necesitamos OrderStorage aquí, usamos la API

class OrderRepository(
    // Quitamos OrderStorage del constructor si ya no lo usas localmente
) {

    suspend fun getOrdersStream(userEmail: String): List<Order> {
        return try {
            // Pide al servidor las órdenes de este usuario
            RetrofitClient.instance.getOrdersByUser(userEmail)
        } catch (e: Exception) {
            Log.e("OrderRepo", "Error al obtener pedidos: ${e.message}")
            emptyList()
        }
    }

    suspend fun addOrder(userEmail: String, order: Order) {
        try {
            // Envía la orden al servidor
            // Nota: El servidor debería vincularla al email, o asegúrate
            // de que tu objeto Order incluya el campo 'userEmail'.
            RetrofitClient.instance.createOrder(order)
        } catch (e: Exception) {
            Log.e("OrderRepo", "Error al crear pedido: ${e.message}")
        }
    }
}