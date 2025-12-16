package com.example.levelupstore_app

import android.app.Application
import com.example.levelupstore_app.data.repository.AuthRepository
import com.example.levelupstore_app.data.repository.CartRepository
import com.example.levelupstore_app.data.repository.OrderRepository
import com.example.levelupstore_app.data.repository.ProductRepository
import com.example.levelupstore_app.data.repository.ReviewRepository
import com.example.levelupstore_app.data.storage.CartStorage
import com.example.levelupstore_app.data.storage.OrderStorage
import com.example.levelupstore_app.data.storage.ReviewStorage
import com.example.levelupstore_app.data.storage.UserPreferences

/**
 * Clase de Aplicación personalizada.
 * Se inicia antes que cualquier pantalla y guardará
 * instancias únicas (singletons) de nuestros repositorios.
 */
class LevelUpApp : Application() {

    // --- 1. Almacenamiento Local (Buzones) ---
    private val userPreferences by lazy { UserPreferences(this) }
    private val cartStorage by lazy { CartStorage(this) }
    
    // --- 2. Repositorios (Mensajeros) ---

    // AuthRepository: Usa Retrofit y UserPreferences
    val authRepository by lazy {
        AuthRepository(userPreferences)
    }

    // ProductRepository: Usa Retrofit
    val productRepository by lazy {
        ProductRepository()
    }

    // CartRepository: SÍ necesita storage (el carrito sigue siendo local)
    val cartRepository by lazy {
        CartRepository(cartStorage)
    }

    // ReviewRepository: Usa Retrofit
    val reviewRepository by lazy {
        ReviewRepository()
    }

    // OrderRepository: Usa Retrofit
    val orderRepository by lazy {
        OrderRepository()
    }
}
