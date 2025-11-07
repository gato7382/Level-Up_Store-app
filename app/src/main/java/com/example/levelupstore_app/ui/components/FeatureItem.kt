// Ruta: com/example/levelupstore_app/ui/components/FeatureItem.kt
package com.example.levelupstore_app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun FeatureItem(
    icon: String, // Usamos el emoji como en tu inicio.html
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    // --- Definición de colores (puedes moverlos a ui/theme/Color.kt) ---
    val NeonBlue = Color(0xFF1E90FF) // Color azul de tu CSS
    // --- Fin de definición de colores ---

    // Basado en tu .feature-card de inicio.css
    Card(
        modifier = modifier
            .fillMaxWidth() // Ocupa el ancho disponible en la cuadrícula
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            // Fondo oscuro semi-transparente
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Centra el contenido
        ) {
            // Icono (emoji)
            Text(
                text = icon,
                style = MaterialTheme.typography.displaySmall // Tamaño grande para el emoji
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Título
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = NeonBlue, // Color azul
                // fontFamily = OrbitronFontFamily, // (Si la configuras)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Descripción
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center, // Texto centrado
                color = Color.White.copy(alpha = 0.8f) // Color de texto atenuado
            )
        }
    }
}