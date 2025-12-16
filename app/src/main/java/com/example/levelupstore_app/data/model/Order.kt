package com.example.levelupstore_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val id: Long? = null,
    val total: Double,
    val dateCreated: String? = null,
    val items: List<OrderItem>
)
