@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)
package com.example.levelupstore_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val category: String,
    val description: String,
    val images: List<String>,
    val specs: List<Spec>
)

@Serializable
data class Spec(
    val name: String,
    val value: String
)