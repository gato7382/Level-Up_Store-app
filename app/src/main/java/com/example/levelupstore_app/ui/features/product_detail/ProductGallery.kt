// Ruta: com/example.levelupstore_app/ui/features/product_detail/ProductGallery.kt
package com.example.levelupstore_app.ui.features.product_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.levelupstore_app.R
import com.example.levelupstore_app.ui.components.ProductThumbnail

/**
 * Organismo que muestra la galería de imágenes del producto.
 * Reemplaza la lógica de images.js y .product-gallery
 *
 * @param images Una lista de strings con las rutas de las imágenes (ej. ["/imgs/ps51.webp", ...])
 */
@Composable
fun ProductGallery(images: List<String>) {

    // 1. ESTADO (STATE)
    // 'remember' le da memoria al componente, 'mutableStateOf' lo hace observable.
    // Esto reemplaza tu 'this.currentImageIndex' de JavaScript.
    // Guardamos el índice de la imagen seleccionada.
    var selectedImageIndex by remember { mutableStateOf(0) }

    // 2. LÓGICA
    // Aseguramos que el índice sea válido y obtenemos la URL de la imagen principal.
    val mainImageUrl = images.getOrNull(selectedImageIndex) ?: ""

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // 3. IMAGEN PRINCIPAL
        // (Basado en .product-main-image)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f), // Cuadrada
            colors = CardDefaults.cardColors(containerColor = Color(0xFF141414))
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = "file:///android_asset/${mainImageUrl.removePrefix("/")}",
                    placeholder = painterResource(id = R.drawable.logo_level_up)
                ),
                contentDescription = "Imagen principal del producto",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 4. LISTA DE MINIATURAS (THUMBNAILS)
        // (Basado en .product-thumbnails)
        // LazyRow es una lista horizontal "scrollable"
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(images) { index, imageUrl ->
                // Renderiza nuestra molécula ProductThumbnail
                ProductThumbnail(
                    imageUrl = imageUrl,
                    // La miniatura está "activa" si su índice es el seleccionado
                    isActive = (index == selectedImageIndex),
                    // Al hacer clic, actualiza el estado (el índice)
                    // Esto reemplaza tu 'changeImage(index)'
                    onClick = { selectedImageIndex = index }
                )
            }
        }
    }
}