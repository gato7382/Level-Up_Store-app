package com.example.levelupstore_app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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

// Definimos colores locales si no están en tu tema global,
// o puedes usar los de MaterialTheme directamente.
private val NeonBlue = Color(0xFF00E5FF)
private val StarColor = Color(0xFFFFD700)
private val CardBackground = Color(0xFF1E1E1E)
private val TextGray = Color(0xFFB0B0B0)

@Composable
fun ReviewItem(review: Review, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Cabecera (Nombre y Fecha)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    // CORRECCIÓN: Si el nombre es nulo, mostramos "Anónimo"
                    text = review.name ?: "Anónimo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = NeonBlue
                )
                Text(
                    // CORRECCIÓN: Si la fecha es nula, mostramos cadena vacía
                    text = review.date ?: "",
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
                        // Verificamos el rating, asumiendo 0 si es nulo
                        tint = if (index <= (review.rating ?: 0)) StarColor else Color.DarkGray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Texto de la Reseña
            Text(
                // CORRECCIÓN: Texto por defecto si es nulo
                text = review.text ?: "Sin comentarios.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextGray
            )
        }
    }
}