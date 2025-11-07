// Ruta: com/example/levelupstore_app/ui/features/catalog/CatalogScreen.kt
package com.example.levelupstore_app.ui.features.catalog

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.levelupstore_app.ui.components.ProductCard
import com.example.levelupstore_app.ui.components.SectionTitle
import com.example.levelupstore_app.ui.features.cart.CartViewModel
import com.example.levelupstore_app.ui.utils.AppViewModelFactory

@Composable
fun CatalogScreen(
    navController: NavController,
    // 1. Pedimos AMBOS ViewModels usando nuestra Fábrica
    catalogViewModel: CatalogViewModel = viewModel(factory = AppViewModelFactory),
    cartViewModel: CartViewModel = viewModel(factory = AppViewModelFactory)
) {
    // 2. Observamos el estado del Catálogo
    val uiState by catalogViewModel.uiState.collectAsState()
    val context = LocalContext.current // Para mostrar notificaciones (Toast)

    // --- RENDERIZADO ---
    Box(modifier = Modifier.fillMaxSize()) {

        // --- Estado de Carga ---
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        // --- Estado de Error ---
        if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // --- Estado de Éxito ---
        // Usamos LazyColumn para la página entera sea "scrolleable"
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            // Título de la página
            item {
                Text(
                    text = "Catálogo de Productos",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                    // fontFamily = OrbitronFontFamily // (Si la configuras)
                )
            }

            // 3. Itera sobre el Mapa de productos agrupados
            uiState.productsByCategory.forEach { (categoryName, productsInCategory) ->

                // 4. Renderiza el Título de la Categoría
                item {
                    SectionTitle(
                        title = categoryName,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }

                // 5. Renderiza la Cuadrícula de Productos
                // (Usamos un LazyVerticalGrid DENTRO de un item. No es lo más eficiente
                // pero es la forma más simple de tener una lista mixta)
                item {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 160.dp), // 2 columnas en móvil
                        modifier = Modifier.heightIn(max = 2000.dp), // Altura máxima
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(productsInCategory) { product ->
                            // 6. Renderiza la Molécula ProductCard
                            ProductCard(
                                product = product,
                                onCardClick = {
                                    // Navega a la pantalla de detalle (crearemos esta ruta)
                                    navController.navigate("product_detail/${product.id}")
                                },
                                onAddToCartClick = {
                                    // 7. Llama al "Cerebro" del Carrito
                                    cartViewModel.addToCart(product)
                                    // Muestra una notificación simple
                                    Toast.makeText(context, "${product.name} añadido al carrito", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}