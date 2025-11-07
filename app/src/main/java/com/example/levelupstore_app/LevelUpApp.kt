package com.example.levelupstore_app

import android.app.Application
import com.example.levelupstore_app.data.repository.*
import com.example.levelupstore_app.data.storage.*

class LevelUpApp : Application() {

    private val userPreferences by lazy { UserPreferences(this) }
    private val cartStorage by lazy { CartStorage(this) }
    private val reviewStorage by lazy { ReviewStorage(this) }
    private val orderStorage by lazy { OrderStorage(this) }

    val authRepository by lazy { AuthRepository(this, userPreferences) }
    val productRepository by lazy { ProductRepository(this) }
    val cartRepository by lazy { CartRepository(cartStorage) }
    val reviewRepository by lazy { ReviewRepository(reviewStorage) }
    val orderRepository by lazy { OrderRepository(orderStorage) }
}