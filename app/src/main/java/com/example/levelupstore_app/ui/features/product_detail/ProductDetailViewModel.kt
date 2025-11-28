package com.example.levelupstore_app.ui.features.product_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupstore_app.data.model.Product
import com.example.levelupstore_app.data.model.Review
import com.example.levelupstore_app.data.repository.AuthRepository
import com.example.levelupstore_app.data.repository.ProductRepository
import com.example.levelupstore_app.data.repository.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ProductDetailUiState(
    val product: Product? = null,
    val reviews: List<Review> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false
)

class ProductDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository,
    private val reviewRepository: ReviewRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    // 1. AQUÍ TENEMOS EL ID DEL PRODUCTO ACTUAL (ej: "ps5")
    private val productId: String = checkNotNull(savedStateHandle["productId"])

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val product = productRepository.getProductById(productId)

                if (product == null) {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Producto no encontrado") }
                    return@launch
                }

                // 2. PEDIMOS SOLO LAS RESEÑAS DE ESTE PRODUCTO
                // (El repositorio llama a la API filtrando por ?productId=...)
                val reviews = reviewRepository.getReviews(productId)
                val user = authRepository.getActiveUserStream().first()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        product = product,
                        reviews = reviews.sortedByDescending { r -> r.date },
                        isLoggedIn = (user != null)
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error al cargar") }
            }
        }
    }

    fun addReview(text: String, rating: Int) {
        viewModelScope.launch {
            val user = authRepository.getActiveUserStream().first() ?: return@launch

            // 3. CREAMOS LA RESEÑA VINCULADA AL PRODUCTO
            val newReview = Review(
                productId = productId, // <-- ¡AQUÍ ESTÁ LA CLAVE! Usamos el ID de arriba
                name = user.nombre,
                rating = rating, // Guardamos las estrellas
                text = text,
                date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            )

            // 4. ENVIAMOS AL SERVIDOR
            reviewRepository.addReview(newReview)

            // 5. RECARGAMOS LOS DATOS PARA VER LA NUEVA RESEÑA
            loadData()
        }
    }
}