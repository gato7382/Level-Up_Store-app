package com.example.levelupstore_app.data.repository

import com.example.levelupstore_app.data.model.Review
import com.example.levelupstore_app.data.storage.ReviewStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class ReviewRepository(private val reviewStorage: ReviewStorage) {

    fun getReviewsStream(productId: String): Flow<List<Review>> {
        return reviewStorage.reviewsMapFlow.map { map ->
            map[productId] ?: emptyList()
        }
    }

    suspend fun addReview(productId: String, review: Review) {
        val currentMap = reviewStorage.reviewsMapFlow.first()
        val currentList = currentMap[productId] ?: emptyList()
        val newList = listOf(review) + currentList
        reviewStorage.saveReviewsMap(currentMap + (productId to newList))
    }
}