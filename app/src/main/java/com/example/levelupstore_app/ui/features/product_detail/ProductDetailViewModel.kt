// Ruta: com/example/levelupstore_app/ui/features/product_detail/ProductDetailViewModel.kt
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

/**
 * Estado de la UI para la pantalla de Detalle de Producto.
 */
data class ProductDetailUiState(
    val product: Product? = null,
    val reviews: List<Review> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false // Para saber si mostrar el formulario de reseña
)

/**
 * El "Cerebro" (ViewModel) de la pantalla de Detalle de Producto.
 */
class ProductDetailViewModel(
    savedStateHandle: SavedStateHandle, // Objeto para leer los argumentos de navegación
    private val productRepository: ProductRepository,
    private val reviewRepository: ReviewRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    // 1. Obtenemos el ID del producto desde la ruta de navegación
    private val productId: String = checkNotNull(savedStateHandle["productId"])

    // 2. EL ESTADO (Privado y Mutable)
    private val _uiState = MutableStateFlow(ProductDetailUiState())
    // EL ESTADO (Público e Inmutable) que la UI observará
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    // 3. BLOQUE DE INICIALIZACIÓN (Se ejecuta al crear el ViewModel)
    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                // 1. Cargar el producto (dato estático)
                val product = productRepository.getProductById(productId)

                if (product == null) {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Producto no encontrado") }
                    return@launch
                }

                // 2. Definir los streams (flujos) que queremos observar
                val reviewsStream = reviewRepository.getReviewsStream(productId)
                val userStream = authRepository.getActiveUserStream()

                // 3. Combinar los dos streams
                combine(reviewsStream, userStream) { reviews, user ->
                    // 4. Por cada cambio en CUALQUIERA de los streams,
                    //    crear un nuevo objeto UiState
                    ProductDetailUiState(
                        isLoading = false, // La carga inicial ya terminó
                        product = product, // El producto estático
                        reviews = reviews.sortedByDescending { it.date }, // Muestra nuevas primero
                        isLoggedIn = (user != null) // El estado de sesión actualizado
                    )
                }
                    // --- 5. ¡AQUÍ ESTÁ LA CORRECCIÓN! ---
                    // 'collect' ahora recibe el 'newState' (creado por 'combine')
                    // y lo asigna a nuestro '_uiState.value'
                    .collect { newState ->
                        _uiState.value = newState
                    }
                // --- FIN DE LA CORRECCIÓN ---

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error al cargar el producto") }
            }
        }
    }

    /**
     * Añade una nueva reseña para este producto (reemplaza 'submitReview' en reviews.js)
     */
    fun addReview(text: String, rating: Int) {
        viewModelScope.launch {
            val user = authRepository.getActiveUserStream().first() // Obtiene el usuario actual
            if (user == null) return@launch // No debería pasar si isLoggedIn es true, pero por seguridad

            val newReview = Review(
                name = user.nombre, // Usa el nombre del usuario logueado
                rating = rating,
                text = text,
                date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            )

            // Llama al "mensajero" de reseñas para guardarla
            reviewRepository.addReview(productId, newReview)
        }
    }
}