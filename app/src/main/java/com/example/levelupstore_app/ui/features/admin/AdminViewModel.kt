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
import java.util.UUID

data class AdminUiState(
    val id: String? = null, // Si es null, es modo CREAR. Si tiene valor, es modo EDITAR.
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
    savedStateHandle: SavedStateHandle, // Para recibir ID si venimos a editar
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    // Si recibimos un productId al abrir la pantalla, cargamos los datos
    init {
        val productId = savedStateHandle.get<String>("productId")
        if (productId != null && productId != "{productId}") { // Validación simple
            loadProductForEdit(productId)
        }
    }

    private fun loadProductForEdit(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val product = productRepository.getProductById(id)
            if (product != null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        id = product.id,
                        name = product.name,
                        price = product.price.toInt().toString(), // Simplificación a Int
                        category = product.category,
                        description = product.description,
                        imageUrl = product.images.firstOrNull() ?: ""
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false, message = "No se pudo cargar el producto") }
            }
        }
    }

    // --- Setters ---
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

        viewModelScope.launch {
            val isEditing = state.id != null

            val product = Product(
                id = state.id ?: UUID.randomUUID().toString(), // Usa ID existente o genera uno
                name = state.name,
                price = state.price.toDoubleOrNull() ?: 0.0,
                category = state.category,
                description = state.description,
                images = listOf(state.imageUrl),
                specs = emptyList()
            )

            val result = if (isEditing) {
                productRepository.updateProduct(product)
            } else {
                productRepository.addProduct(product)
            }

            if (result) {
                _uiState.update { it.copy(isLoading = false, message = "Guardado con éxito", success = true) }
            } else {
                _uiState.update { it.copy(isLoading = false, message = "Error al guardar") }
            }
        }
    }

    fun deleteProduct() {
        val id = _uiState.value.id ?: return
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = productRepository.deleteProduct(id)
            if (result) {
                _uiState.update { it.copy(isLoading = false, message = "Producto eliminado", isDeleteSuccess = true) }
            } else {
                _uiState.update { it.copy(isLoading = false, message = "Error al eliminar") }
            }
        }
    }
}