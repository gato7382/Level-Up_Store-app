// Ruta: com/example/levelupstore_app/ui/components/ProductThumbnail.kt
package com.example.levelupstore_app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.levelupstore_app.R

/**
 * Molécula para una miniatura (thumbnail) en la galería de producto.
 * Basado en .product-thumbnail
 *
 * @param imageUrl La ruta de la imagen (ej. "/imgs/ps51.webp")
 * @param isActive Si esta miniatura está seleccionada.
 * @param onClick Lambda que se ejecuta al presionar.
 */
@Composable
fun ProductThumbnail(
    imageUrl: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val GreenGlow = Color(0xFF39FF14) // Color de borde activo
    val CardBackground = Color(0xFF141414)
    val CardBorder = if (isActive) GreenGlow else Color.Transparent

    Card(
        modifier = Modifier
            .padding(4.dp)
            .aspectRatio(1f) // Cuadrada
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        border = BorderStroke(2.dp, CardBorder) // Borde verde si está activa
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                // Usamos la misma lógica corregida para la ruta
                model = "file:///android_asset/${imageUrl.removePrefix("/")}",
                placeholder = painterResource(id = R.drawable.logo_level_up)
            ),
            contentDescription = "Miniatura de producto",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}