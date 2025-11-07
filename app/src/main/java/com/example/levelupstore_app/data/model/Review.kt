// Ruta: com/example/levelupstore_app/data/model/Review.kt
@file:OptIn(kotlinx.serialization.InternalSerializationApi::class) // Tu solución para el Opt-In
package com.example.levelupstore_app.data.model

import kotlinx.serialization.Serializable

/**
 * Define la estructura de una sola Reseña (Review),
 * basado en la lógica de 'reviews.js'.
 *
 * La marcaremos como @Serializable para poder guardarla fácilmente
 * en el DataStore (nuestro "localStorage").
 */
@Serializable
data class Review(
    val name: String,
    val rating: Int,
    val text: String,
    val date: String // Guardaremos la fecha como un String (ej. "2025-10-21")
)