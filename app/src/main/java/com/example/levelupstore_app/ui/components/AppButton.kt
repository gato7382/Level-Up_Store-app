package com.example.levelupstore_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
// Asegúrate de tener estos colores definidos en ui/theme/Color.kt
// import com.example.levelupstore_app.ui.theme.GreenGlow
// import com.example.levelupstore_app.ui.theme.NeonBlue

@Composable
fun AppButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    // --- Definición de colores (puedes moverlos a Color.kt) ---
    val NeonBlue = Color(0xFF1E90FF) // Color azul de tu CSS
    val GreenGlow = Color(0xFF39FF14) // Color verde de tu CSS
    // --- Fin de definición de colores ---

    // Esto crea el fondo degradado de tus botones
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(NeonBlue, GreenGlow)
    )

    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent, // Hacemos el color del botón transparente
            contentColor = Color.Black // Texto negro
        ),
        // Aplicamos el degradado al fondo
        contentPadding = PaddingValues(), // Quitamos padding interno
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp) // Añade sombra
    ) {
        Box(
            modifier = Modifier
                .background(gradientBrush)
                .fillMaxWidth() // Asegura que el degradado ocupe todo
                .padding(vertical = 12.dp), // Padding vertical
            contentAlignment = Alignment.Center
        ) {
            Text(text = text, fontWeight = FontWeight.Bold)
        }
    }
}