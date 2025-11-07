// Ruta: com/example/levelupstore_app/data/repository/ReviewRepository.kt
package com.example.levelupstore_app.data.repository

import com.example.levelupstore_app.data.model.Review
import com.example.levelupstore_app.data.storage.ReviewStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Repositorio para la lógica de Reseñas. Reemplaza reviews.js
 */
class ReviewRepository(private val reviewStorage: ReviewStorage) {

    /**
     * Obtiene un "stream" de la lista de reseñas para UN producto específico.
     */
    fun getReviewsStream(productId: String): Flow<List<Review>> {
        return reviewStorage.reviewsMapFlow.map { map ->
            map[productId] ?: emptyList()
        }
    }

    /**
     * Añade una nueva reseña a un producto.
     */
    suspend fun addReview(productId: String, review: Review) {
        val currentMap = reviewStorage.reviewsMapFlow.first()
        val currentList = currentMap[productId] ?: emptyList()
        val newList = listOf(review) + currentList
        reviewStorage.saveReviewsMap(currentMap + (productId to newList))
    }
}