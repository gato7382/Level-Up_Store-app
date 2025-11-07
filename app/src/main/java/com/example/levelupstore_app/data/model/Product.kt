@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)
package com.example.levelupstore_app.data.model

import kotlinx.serialization.Serializable

/**
 * Define la estructura de un producto, coincidiendo con 'products.json'.
 *
 * @Serializable le dice a la librería de Kotlinx que puede convertir
 * esta clase desde y hacia JSON (gracias al plugin que instalamos).
 *
 * "data class" es una clase especial de Kotlin para guardar datos.
 */
@Serializable
data class Product(
    val id: String,
    val name: String,
    val price: Double, // Usamos Double para precios
    val category: String,
    val description: String,
    val images: List<String>, // Una lista de strings para las URLs
    val specs: List<Spec>     // Una lista de objetos 'Spec'
)

/**
 * Clase auxiliar para las especificaciones (specs) anidadas
 * dentro de cada producto.
 */
@Serializable
data class Spec(
    val name: String,
    val value: String
)