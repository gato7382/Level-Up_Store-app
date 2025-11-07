// Ruta: com/example/levelupstore_app/ui/utils/AppViewModelFactory.kt
package com.example.levelupstore_app.ui.utils

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.levelupstore_app.LevelUpApp
import com.example.levelupstore_app.ui.features.auth.AuthViewModel
import com.example.levelupstore_app.ui.features.cart.CartViewModel
import com.example.levelupstore_app.ui.features.catalog.CatalogViewModel
import com.example.levelupstore_app.ui.features.product_detail.ProductDetailViewModel
import com.example.levelupstore_app.ui.features.profile.ProfileViewModel // <-- 1. IMPORTA EL NUEVO VM

/**
 * Fábrica (Factory) que sabe cómo crear TODOS nuestros ViewModels.
 */
object AppViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as LevelUpApp
        val savedStateHandle = extras.createSavedStateHandle()

        // Devolvemos una instancia de nuestra fábrica interna
        return VmFactory(application, savedStateHandle).create(modelClass)
    }
}

@Suppress("UNCHECKED_CAST")
private class VmFactory(
    private val app: LevelUpApp,
    private val savedStateHandle: SavedStateHandle
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(app.authRepository) as T
        }

        else if (modelClass.isAssignableFrom(CatalogViewModel::class.java)) {
            return CatalogViewModel(app.productRepository) as T
        }

        else if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            return CartViewModel(
                app.cartRepository,
                app.authRepository,
                app.orderRepository
            ) as T
        }

        else if (modelClass.isAssignableFrom(ProductDetailViewModel::class.java)) {
            return ProductDetailViewModel(
                savedStateHandle,
                app.productRepository,
                app.reviewRepository,
                app.authRepository
            ) as T
        }

        // --- 2. ¡CÓDIGO AÑADIDO! ---
        else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            // Si piden un ProfileViewModel...
            return ProfileViewModel(
                app.authRepository,
                app.orderRepository
            ) as T
        }
        // --- FIN DEL CÓDIGO AÑADIDO ---

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}