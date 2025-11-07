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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    ProductGallery(images = product.images)
                }

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
                    ProductSpecs(specs = product.specs)
                }

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
        }
    }
}