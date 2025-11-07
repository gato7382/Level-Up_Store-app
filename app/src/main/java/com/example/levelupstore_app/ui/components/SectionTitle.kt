// Ruta: com/example/levelupstore_app/ui/components/SectionTitle.kt
package com.example.levelupstore_app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    // --- Definición de colores (puedes moverlos a ui/theme/Color.kt) ---
    val GreenGlow = Color(0xFF39FF14) // Color verde de tu CSS
    // --- Fin de definición de colores ---

    // Basado en tu .section-title de productos.css
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall, // Tamaño de fuente
            color = GreenGlow, // Color verde característico
            fontWeight = FontWeight.Bold
            // fontFamily = OrbitronFontFamily // (Si configuras la fuente Orbitron en Type.kt)
        )
        // Línea divisoria de abajo
        Divider(
            modifier = Modifier.padding(top = 8.dp),
            color = GreenGlow.copy(alpha = 0.3f), // Línea con opacidad
            thickness = 2.dp
        )
    }
}