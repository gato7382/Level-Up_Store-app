// Ruta: com/example/levelupstore_app/ui/features/catalog/CatalogViewModel.kt
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

/**
 * Estado de la UI para la pantalla de Catálogo.
 * Contiene el mapa de productos agrupados por categoría.
 */
data class CatalogUiState(
    // Usamos un Mapa para agrupar: "Categoría" -> Lista de Productos
    // ej: "Consolas" -> [ProductoPS5, ProductoXbox]
    val productsByCategory: Map<String, List<Product>> = emptyMap(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * El "Cerebro" (ViewModel) de la pantalla de Catálogo.
 * Obtiene los productos del repositorio y los agrupa por categoría.
 */
class CatalogViewModel(
    private val productRepository: ProductRepository // "Inyectamos" el mensajero
) : ViewModel() {

    // 1. EL ESTADO (Privado y Mutable)
    private val _uiState = MutableStateFlow(CatalogUiState())
    // EL ESTADO (Público e Inmutable) que la UI observará
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    // 2. BLOQUE DE INICIALIZACIÓN
    // Esto se ejecuta automáticamente cuando se crea el ViewModel
    init {
        loadProducts()
    }

    /**
     * Carga los productos del repositorio y los agrupa.
     */
    private fun loadProducts() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        // Usamos viewModelScope para la corrutina (trabajo asíncrono)
        viewModelScope.launch {
            try {
                // 1. Llama al "mensajero" para obtener los productos
                val products = productRepository.getProducts() // (Esta función lee el JSON)

                // 2. Agrupa los productos por su categoría
                val groupedProducts = products.groupBy { it.category }

                // 3. Actualiza el estado con los productos agrupados
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        productsByCategory = groupedProducts
                    )
                }
            } catch (e: IOException) {
                // Maneja errores de lectura de archivo
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Error al cargar el catálogo.")
                }
            }
        }
    }
}