package com.example.levelupstore_app.ui.features.home

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

private val badgesList = listOf(
    "🏆 Eventos Nacionales",
    "🎉 Puntos LevelUp",
    "👑 Niveles VIP",
    "💬 Comunidad Activa"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CommunitySection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SectionTitle(
            title = "Únete a la Comunidad Level-Up",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Más que una tienda, somos una comunidad de gamers apasionados. Participa en eventos, gana puntos LevelUp y conecta con otros jugadores en todo Chile.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        FlowRow(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            badgesList.forEach { badgeText ->
                Badge(text = badgeText)
            }
        }
    }
}