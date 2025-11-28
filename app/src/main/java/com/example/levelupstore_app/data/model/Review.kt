package com.example.levelupstore_app.data.model

data class Review(
    val id: String = "", // ID único generado por la BD
    val productId: String, // <-- ¡NUEVO! Para saber de qué producto es
    val name: String,
    val rating: Int,
    val text: String,
    val date: String
)