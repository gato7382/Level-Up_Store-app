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
    val GreenGlow = Color(0xFF39FF14)

    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = GreenGlow,
            fontWeight = FontWeight.Bold
        )
        Divider(
            modifier = Modifier.padding(top = 8.dp),
            color = GreenGlow.copy(alpha = 0.3f),
            thickness = 2.dp
        )
    }
}