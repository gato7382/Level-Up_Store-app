@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)
package com.example.levelupstore_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val items: List<CartItem>,
    val total: Double,
    val date: String
)