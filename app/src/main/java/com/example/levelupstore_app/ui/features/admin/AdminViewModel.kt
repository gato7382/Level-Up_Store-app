package com.example.levelupstore_app.ui.features.admin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupstore_app.data.model.Product
import com.example.levelupstore_app.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale

data class AdminUiState(
    val id: Long? = null,
    val name: String = "",
    val price: String = "",
    val category: String = "",
    val description: String = "",
    val imageUrl: String = "", 
    val isLoading: Boolean = false,
    val message: String? = null,
    val success: Boolean = false,
    val isDeleteSuccess: Boolean = false
)

class AdminViewModel(
    savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    init {
        val productIdString = savedStateHandle.get<String>("productId")
        val productId = productIdString?.toLongOrNull()
        
        if (productId != null) {
            loadProductForEdit(productId)
        }
    }

    private fun loadProductForEdit(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val product = productRepository.getProductById(id)
            if (product != null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        id = product.id,
                        name = product.name,
                        price = product.price?.toString() ?: "",
                        category = product.category,
                        description = product.description,
                        imageUrl = product.imageUrl ?: ""
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false, message = "No se pudo cargar el producto") }
            }
        }
    }

    fun onNameChange(v: String) = _uiState.update { it.copy(name = v) }
    fun onPriceChange(v: String) = _uiState.update { it.copy(price = v) }
    fun onCategoryChange(v: String) = _uiState.update { it.copy(category = v) }
    fun onDescriptionChange(v: String) = _uiState.update { it.copy(description = v) }
    fun onImageChange(v: String) = _uiState.update { it.copy(imageUrl = v) }
    fun clearMessage() = _uiState.update { it.copy(message = null, success = false, isDeleteSuccess = false) }

    fun saveProduct() {
        val state = _uiState.value
        if (state.name.isBlank() || state.price.isBlank()) {
            _uiState.update { it.copy(message = "Faltan datos") }
            return
        }
        _uiState.update { it.copy(isLoading = true, message = null) }

        // --- CORRECCIÓN: Formateo de Categoría (Capitalize) ---
        // "tEST" -> "Test", " consolas " -> "Consolas"
        val rawCategory = state.category.trim()
        val formattedCategory = if (rawCategory.isNotEmpty()) {
            rawCategory.lowercase(Locale.ROOT)
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        } else {
            "General" // Valor por defecto si está vacío
        }

        viewModelScope.launch {
            val isEditing = state.id != null
            
            val isRemoteImage = state.imageUrl.startsWith("http")
            val imageFile = if (state.imageUrl.isNotBlank() && !isRemoteImage) {
                File(state.imageUrl)
            } else {
                null
            }

            val product = Product(
                id = state.id,
                name = state.name.trim(), // Limpiamos espacios extra
                price = state.price.toDoubleOrNull(),
                category = formattedCategory, // Usamos la categoría formateada
                description = state.description.trim(),
                imageUrl = null
            )

            val result = if (isEditing) {
                 productRepository.updateProduct(product, imageFile)
            } else {
                if (imageFile != null) {
                    productRepository.addProduct(product, imageFile)
                } else {
                    Result.failure(Exception("La imagen es obligatoria"))
                }
            }

            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false, message = "Guardado con éxito", success = true) }
            } else {
                val error = result.exceptionOrNull()?.message ?: "Error desconocido"
                _uiState.update { it.copy(isLoading = false, message = "Error: $error") }
            }
        }
    }

    fun deleteProduct() {
        val id = _uiState.value.id ?: return
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = productRepository.deleteProduct(id)
            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false, message = "Producto eliminado", isDeleteSuccess = true) }
            } else {
                _uiState.update { it.copy(isLoading = false, message = "Error al eliminar") }
            }
        }
    }
}
