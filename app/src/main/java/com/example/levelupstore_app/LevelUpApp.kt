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
    // 'lazy' significa que se crean la primera vez que se usan
    private val userPreferences by lazy { UserPreferences(this) }
    private val cartStorage by lazy { CartStorage(this) }
    // (Estos storage ya no se usan en los repositorios de red, pero los dejamos por si acaso)
    private val reviewStorage by lazy { ReviewStorage(this) }
    private val orderStorage by lazy { OrderStorage(this) }

    // --- 2. Repositorios (Mensajeros) ---

    // AuthRepository: SÍ necesita storage (para guardar la sesión)
    val authRepository by lazy {
        AuthRepository(this, userPreferences)
    }

    // ProductRepository: SÍ necesita context (para logs o compatibilidad)
    val productRepository by lazy {
        ProductRepository(this)
    }

    // CartRepository: SÍ necesita storage (el carrito sigue siendo local)
    val cartRepository by lazy {
        CartRepository(cartStorage)
    }

    // ReviewRepository: ¡YA NO necesita storage! (Usa Retrofit directo)
    val reviewRepository by lazy {
        ReviewRepository() // <-- CORREGIDO: Paréntesis vacíos
    }

    // OrderRepository: ¡YA NO necesita storage! (Usa Retrofit directo)
    val orderRepository by lazy {
        OrderRepository() // <-- CORREGIDO: Paréntesis vacíos
    }
}