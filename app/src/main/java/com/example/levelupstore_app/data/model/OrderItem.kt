package com.example.levelupstore_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class OrderItem(
    val id: Long? = null,
    val productId: Long,
    val quantity: Int,
    val price: Double,
    val productName: String,
    val productImageUrl: String
)
