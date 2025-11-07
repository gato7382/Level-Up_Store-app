// Ruta: com/example/levelupstore_app/data/model/Order.kt
@file:OptIn(kotlinx.serialization.InternalSerializationApi::class) // Tu solución para el Opt-In
package com.example.levelupstore_app.data.model

import kotlinx.serialization.Serializable

/**
 * Define la estructura de un Pedido (Order) que se guarda
 * en el historial del usuario después de un 'checkout'.
 */
@Serializable
data class Order(
    val items: List<CartItem>, // La lista de productos que compró
    val total: Double,         // El precio final que pagó (con descuento aplicado)
    val date: String           // La fecha de la compra (guardada como un String ISO)
)