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

data class ProductDetailUiState(
    val product: Product? = null,
    val reviews: List<Review> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false,
    val isAdmin: Boolean = false // <-- NUEVO CAMPO
)

class ProductDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository,
    private val reviewRepository: ReviewRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val productIdString: String = checkNotNull(savedStateHandle["productId"])
    private val productId: Long? = productIdString.toLongOrNull()

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        if (productId == null) {
            _uiState.update { it.copy(isLoading = false, errorMessage = "ID de producto inválido") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val product = productRepository.getProductById(productId)

                if (product == null) {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Producto no encontrado") }
                    return@launch
                }

                val reviews = reviewRepository.getReviews(productId)
                val user = authRepository.getActiveUserStream().first()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        product = product,
                        reviews = reviews.sortedByDescending { r -> r.date },
                        isLoggedIn = (user != null),
                        isAdmin = (user?.admin == true) // <-- VERIFICACIÓN DE ADMIN
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error al cargar") }
            }
        }
    }

    fun addReview(text: String, rating: Int) {
        if (productId == null) return

        viewModelScope.launch {
            val user = authRepository.getActiveUserStream().first() ?: return@launch

            val result = reviewRepository.addReview(productId, rating, text)

            if (result.isSuccess) {
                loadData()
            }
        }
    }
}
