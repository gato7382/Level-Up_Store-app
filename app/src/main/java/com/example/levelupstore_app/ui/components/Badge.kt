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
    val NeonBlue = Color(0xFF1E90FF)
    val GreenGlow = Color(0xFF39FF14)

    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(NeonBlue, GreenGlow)
    )
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(gradientBrush)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = Color.Black,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}