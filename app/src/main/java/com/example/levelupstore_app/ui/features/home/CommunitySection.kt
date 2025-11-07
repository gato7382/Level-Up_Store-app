// Ruta: com/example/levelupstore_app/ui/features/home/CommunitySection.kt
package com.example.levelupstore_app.ui.features.home

// Importante: FlowRow requiere esta importación
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.levelupstore_app.ui.components.Badge
import com.example.levelupstore_app.ui.components.SectionTitle

// Lista privada de las insignias (basada en tu inicio.html)
private val badgesList = listOf(
    "🏆 Eventos Nacionales",
    "🎉 Puntos LevelUp",
    "👑 Niveles VIP",
    "💬 Comunidad Activa"
)

// Opt-In es necesario para FlowRow (que es el "flex-wrap: wrap" de CSS)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CommunitySection(modifier: Modifier = Modifier) {
    // Basado en .community de inicio.html
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Centra todo el contenido
    ) {
        // 1. Título de la sección
        SectionTitle(
            title = "Únete a la Comunidad Level-Up",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 2. Párrafo de descripción
        Text(
            text = "Más que una tienda, somos una comunidad de gamers apasionados. Participa en eventos, gana puntos LevelUp y conecta con otros jugadores en todo Chile.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Contenedor de Insignias (FlowRow se ajusta automáticamente)
        FlowRow(
            modifier = Modifier.padding(8.dp),
            // Espaciado horizontal y vertical entre las insignias
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            badgesList.forEach { badgeText ->
                Badge(text = badgeText)
            }
        }
    }
}