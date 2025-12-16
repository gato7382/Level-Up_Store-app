package com.example.levelupstore_app.ui.features.product_detail

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.levelupstore_app.R
import com.example.levelupstore_app.ui.features.cart.CartViewModel
import com.example.levelupstore_app.ui.utils.AppViewModelFactory

@Composable
fun ProductDetailScreen(
    navController: NavController,
    detailViewModel: ProductDetailViewModel = viewModel(factory = AppViewModelFactory),
    cartViewModel: CartViewModel = viewModel(factory = AppViewModelFactory)
) {
    val uiState by detailViewModel.uiState.collectAsState()
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {

        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            return@Box 
        }

        if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Center)
            )
            return@Box 
        }

        val product = uiState.product
        if (product != null) {
            // 1. EL CONTENIDO VA PRIMERO (Fondo)
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                // Imagen Principal
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().height(300.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = product.imageUrl,
                                placeholder = painterResource(id = R.drawable.logo_level_up),
                                error = painterResource(id = R.drawable.logo_level_up)
                            ),
                            contentDescription = product.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Info y Botón Carrito
                item {
                    ProductInfo(
                        product = product,
                        onAddToCartClick = {
                            cartViewModel.addToCart(product)
                            Toast.makeText(context, "${product.name} añadido", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Reseñas
                item {
                    ReviewSection(
                        navController = navController,
                        reviews = uiState.reviews,
                        isLoggedIn = uiState.isLoggedIn,
                        onReviewSubmit = { text, rating ->
                            detailViewModel.addReview(text, rating)
                            Toast.makeText(context, "Reseña enviada", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            // 2. BOTÓN DE "ATRÁS" (Esquina Superior Izquierda)
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .statusBarsPadding()
                    .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape) // Fondo semitransparente para contraste
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }

            // 3. EL BOTÓN FLOTANTE DE EDITAR VA AL FINAL (Esquina Superior Derecha)
            if (uiState.isAdmin) {
                FloatingActionButton(
                    onClick = { 
                        navController.navigate("admin_panel?productId=${product.id}") 
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .statusBarsPadding(), 
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar Producto")
                }
            }
        }
    }
}
