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
import kotlinx.coroutines.flow.combine
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

                val reviewsStream = reviewRepository.getReviewsStream(productId)
                val userStream = authRepository.getActiveUserStream()

                combine(reviewsStream, userStream) { reviews, user ->
                    ProductDetailUiState(
                        isLoading = false,
                        product = product,
                        reviews = reviews.sortedByDescending { it.date },
                        isLoggedIn = (user != null)
                    )
                }
                    .collect { newState ->
                        _uiState.value = newState
                    }

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error al cargar el producto") }
            }
        }
    }

    fun addReview(text: String, rating: Int) {
        viewModelScope.launch {
            val user = authRepository.getActiveUserStream().first()
            if (user == null) return@launch

            val newReview = Review(
                name = user.nombre,
                rating = rating,
                text = text,
                date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            )

            reviewRepository.addReview(productId, newReview)
        }
    }
}