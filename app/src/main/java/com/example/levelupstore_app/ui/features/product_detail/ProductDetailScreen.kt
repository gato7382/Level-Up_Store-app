// Ruta: com/example/levelupstore_app/ui/features/product_detail/ProductDetailScreen.kt
package com.example.levelupstore_app.ui.features.product_detail

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.levelupstore_app.ui.features.cart.CartViewModel
import com.example.levelupstore_app.ui.utils.AppViewModelFactory

@Composable
fun ProductDetailScreen(
    navController: NavController,
    // Pedimos ambos "cerebros": el de esta página y el del carrito
    detailViewModel: ProductDetailViewModel = viewModel(factory = AppViewModelFactory),
    cartViewModel: CartViewModel = viewModel(factory = AppViewModelFactory)
) {
    // Observamos el estado de la página
    val uiState by detailViewModel.uiState.collectAsState()
    val context = LocalContext.current // Para notificaciones

    Box(modifier = Modifier.fillMaxSize()) {

        // --- ESTADO DE CARGA ---
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            return@Box // Sale temprano si está cargando
        }

        // --- ESTADO DE ERROR ---
        if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Center)
            )
            return@Box // Sale temprano si hay error
        }

        // --- ESTADO DE ÉXITO (Producto cargado) ---
        val product = uiState.product
        if (product != null) {
            // Usamos LazyColumn para que toda la página sea "scrolleable"
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                // 1. Organismo: Galería de Imágenes
                item {
                    ProductGallery(images = product.images)
                }

                // 2. Organismo: Información del Producto
                item {
                    ProductInfo(
                        product = product,
                        onAddToCartClick = {
                            // Llama al "cerebro" del carrito
                            cartViewModel.addToCart(product)
                            Toast.makeText(context, "${product.name} añadido", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                // 3. Organismo: Especificaciones
                item {
                    ProductSpecs(specs = product.specs)
                }

                // 4. Organismo: Sección de Reseñas
                item {
                    ReviewSection(
                        navController = navController,
                        reviews = uiState.reviews,
                        isLoggedIn = uiState.isLoggedIn,
                        onReviewSubmit = { text, rating ->
                            // Llama al "cerebro" de esta página
                            detailViewModel.addReview(text, rating)
                            Toast.makeText(context, "Reseña enviada", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}