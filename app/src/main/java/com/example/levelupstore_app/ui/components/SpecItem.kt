// Ruta: com/example/levelupstore_app/ui/components/SpecItem.kt
package com.example.levelupstore_app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Molécula para un solo ítem en la lista de especificaciones.
 * Basado en .spec-item
 *
 * @param name El nombre de la especificación (ej. "Fabricante")
 * @param value El valor (ej. "Sony")
 */
@Composable
fun SpecItem(name: String, value: String) {
    Column (modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween // Alinea a los extremos
        ) {
            // Nombre (ej. "Fabricante")
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray, // Color de .spec-name
                fontWeight = FontWeight.Bold
            )
            // Valor (ej. "Sony")
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White, // Color de .spec-value
                fontWeight = FontWeight.SemiBold
            )
        }
        Divider(modifier = Modifier.padding(top = 12.dp), color = Color.DarkGray.copy(alpha = 0.3f))
    }
}