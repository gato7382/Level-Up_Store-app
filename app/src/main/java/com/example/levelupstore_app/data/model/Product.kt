package com.example.levelupstore_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Long? = null,
    val name: String,
    val description: String,
    val price: Double? = null,
    val stock: Int? = null,
    val category: String,
    val imageUrl: String? = null,
    val reviews: List<Review> = emptyList()
) {
    // Propiedad de compatibilidad para la UI:
    // Devuelve una lista con la única imagen (o vacía), para que componentes antiguos no fallen.
    val images: List<String>
        get() = if (imageUrl != null) listOf(imageUrl) else emptyList()
        
    val specs: List<Spec>
        get() = emptyList()
}

@Serializable
data class Spec(
    val name: String,
    val value: String
)
