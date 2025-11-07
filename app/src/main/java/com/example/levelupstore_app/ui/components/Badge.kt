// Ruta: com/example/levelupstore_app/ui/components/Badge.kt
package com.example.levelupstore_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun Badge(text: String, modifier: Modifier = Modifier) {
    // --- Definición de colores (puedes moverlos a ui/theme/Color.kt) ---
    val NeonBlue = Color(0xFF1E90FF) // Color azul de tu CSS
    val GreenGlow = Color(0xFF39FF14) // Color verde de tu CSS
    // --- Fin de definición de colores ---

    // Basado en tu .level-up-badge de inicio.css
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(NeonBlue, GreenGlow)
    )
    Box(
        modifier = modifier
            .clip(CircleShape) // Forma de píldora
            .background(gradientBrush)
            .padding(horizontal = 16.dp, vertical = 8.dp) // Padding interno
    ) {
        Text(
            text = text,
            color = Color.Black, // Texto negro
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}