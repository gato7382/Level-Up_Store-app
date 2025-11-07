// Ruta: com/example/levelupstore_app/ui/components/RatingInput.kt
package com.example.levelupstore_app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun RatingInput(
    modifier: Modifier = Modifier,
    rating: Int, // El estado actual (0 a 5)
    onRatingChange: (Int) -> Unit // La función a llamar cuando se presiona
) {
    // --- Definición de colores ---
    val StarColor = Color(0xFFFFD700) // Amarillo/Dorado para la estrella
    // --- Fin de definición de colores ---

    Row(modifier = modifier) {
        // Itera 5 veces (para 5 estrellas)
        (1..5).forEach { index ->
            Icon(
                imageVector = if (index <= rating) {
                    Icons.Filled.Star // Estrella Rellena
                } else {
                    Icons.Outlined.StarOutline // Estrella Vacía
                },
                contentDescription = "Rating $index",
                tint = StarColor,
                modifier = Modifier.clickable {
                    onRatingChange(index) // Llama a la función con el nuevo rating
                }
            )
        }
    }
}