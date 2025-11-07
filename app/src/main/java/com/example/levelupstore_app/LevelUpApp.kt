// Ruta: com/example/levelupstore_app/LevelUpApp.kt
package com.example.levelupstore_app

import android.app.Application
import com.example.levelupstore_app.data.repository.*
import com.example.levelupstore_app.data.storage.*

/**
 * Clase de Aplicación personalizada.
 * Se inicia antes que cualquier pantalla y guardará
 * instancias únicas (singletons) de nuestros repositorios.
 */
class LevelUpApp : Application() {

    // --- Almacenamiento (Buzones) ---
    // 'lazy' significa que solo se crearán la primera vez que se necesiten
    private val userPreferences by lazy { UserPreferences(this) }
    private val cartStorage by lazy { CartStorage(this) }
    private val reviewStorage by lazy { ReviewStorage(this) }
    private val orderStorage by lazy { OrderStorage(this) }

    // --- Repositorios (Mensajeros) ---
    // Hacemos públicos los repositorios para que la Fábrica de ViewModels pueda usarlos
    val authRepository by lazy { AuthRepository(this, userPreferences) }
    val productRepository by lazy { ProductRepository(this) }
    val cartRepository by lazy { CartRepository(cartStorage) }
    val reviewRepository by lazy { ReviewRepository(reviewStorage) }
    val orderRepository by lazy { OrderRepository(orderStorage) }
}