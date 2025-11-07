// Ruta: com/example/levelupstore_app/ui/components/ReviewItem.kt
package com.example.levelupstore_app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.example.levelupstore_app.data.model.Review

@Composable
fun ReviewItem(review: Review, modifier: Modifier = Modifier) {
    // --- Definición de colores ---
    val StarColor = Color(0xFFFFD700) // Amarillo/Dorado
    val CardBackground = Color(0xFF1a1a1a) // Fondo de .review-item
    val TextGray = Color(0xFFD3D3D3) // Color de .review-text
    val NeonBlue = Color(0xFF1E90FF) // Color de .reviewer-name
    // --- Fin de definición de colores ---

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header (Nombre y Fecha)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = review.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = NeonBlue
                    // fontFamily = OrbitronFontFamily // (Si la configuras)
                )
                Text(
                    text = review.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Estrellas (Rating)
            Row {
                (1..5).forEach { index ->
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = if (index <= review.rating) StarColor else Color.DarkGray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Texto de la reseña
            Text(
                text = review.text,
                style = MaterialTheme.typography.bodyMedium,
                color = TextGray
            )
        }
    }
}