package com.example.levelupstore_app.data.model

import kotlinx.serialization.Serializable

// Mantenemos @Serializable SOLO si vas a guardar productos en el carrito local (DataStore).
// Gson ignorará esta anotación y funcionará bien.
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