// Ruta: com/example/levelupstore_app/data/model/CartItem.kt
@file:OptIn(kotlinx.serialization.InternalSerializationApi::class) // Tu solución para el Opt-In
package com.example.levelupstore_app.data.model

import kotlinx.serialization.Serializable

/**
 * Define la estructura de un ítem DENTRO del carrito de compras.
 * Es una combinación de un Producto y una Cantidad.
 * La marcaremos como @Serializable para poder guardarla fácilmente
 * en el DataStore (nuestro "localStorage").
 */
@Serializable
data class CartItem(
    val product: Product, // El producto completo (lo anidamos)
    val quantity: Int
)