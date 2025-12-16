package com.example.levelupstore_app.data.repository

import android.util.Log
import com.example.levelupstore_app.data.model.Review
import com.example.levelupstore_app.data.model.ReviewRequest
import com.example.levelupstore_app.data.network.RetrofitClient

class ReviewRepository {

    suspend fun getReviews(productId: Long): List<Review> {
        return try {
            RetrofitClient.instance.getReviewsByProduct(productId)
        } catch (e: Exception) {
            Log.e("ReviewRepo", "Error fetching reviews", e)
            emptyList()
        }
    }

    suspend fun addReview(productId: Long, rating: Int, comment: String): Result<Review> {
        return try {
            val request = ReviewRequest(rating, comment)
            val review = RetrofitClient.instance.createReview(productId, request)
            Result.success(review)
        } catch (e: Exception) {
            Log.e("ReviewRepo", "Error creating review", e)
            Result.failure(e)
        }
    }
}
