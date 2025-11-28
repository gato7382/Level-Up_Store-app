package com.example.levelupstore_app.data.repository

import android.util.Log
import com.example.levelupstore_app.data.model.Review
import com.example.levelupstore_app.data.network.RetrofitClient

class ReviewRepository {

    // Nota: Cambiamos de Flow a List directa o suspend function
    // porque las llamadas a red son "one-shot" (una sola vez), no un stream continuo
    // a menos que uses algo avanzado como Polling o WebSockets.
    suspend fun getReviews(productId: String): List<Review> {
        return try {
            RetrofitClient.instance.getReviewsByProduct(productId)
        } catch (e: Exception) {
            Log.e("ReviewRepo", "Error: ${e.message}")
            emptyList()
        }
    }

    suspend fun addReview(review: Review) {
        try {
            RetrofitClient.instance.createReview(review)
        } catch (e: Exception) {
            Log.e("ReviewRepo", "Error al enviar reseña: ${e.message}")
        }
    }
}