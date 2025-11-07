package com.example.levelupstore_app.ui.features.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupstore_app.data.model.Product
import com.example.levelupstore_app.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

data class CatalogUiState(
    val productsByCategory: Map<String, List<Product>> = emptyMap(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class CatalogViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogUiState())
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val products = productRepository.getProducts()

                val groupedProducts = products.groupBy { it.category }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        productsByCategory = groupedProducts
                    )
                }
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Error al cargar el catálogo.")
                }
            }
        }
    }
}