// Ruta: com/example/levelupstore_app/ui/theme/Theme.kt
package com.example.levelupstore_app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// --- ¡ESTE ES TU NUEVO TEMA OSCURO PERSONALIZADO! ---
private val LevelUpDarkColorScheme = darkColorScheme(
    primary = GreenGlow,      // El color principal (botones, acentos)
    onPrimary = DarkText,       // Texto sobre el color principal (ej. "Añadir" en botón verde)
    secondary = NeonBlue,       // El segundo color de acento
    onSecondary = DarkText,     // Texto sobre el color secundario

    background = DarkBackground,  // El color de fondo principal de la app (#0a0a0a)
    onBackground = LightText,   // Texto sobre el fondo

    surface = DarkSurface,      // Color para tarjetas, menús, barras (#1a1a1a)
    onSurface = LightText,      // Texto sobre las tarjetas

    // Puedes definir otros colores si quieres (error, terciario, etc.)
)

// (El LightColorScheme por defecto - no lo usaremos pero lo dejamos por si acaso)
/*
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
    // ... otros colores claros
)
*/

@Composable
fun LevelUpStoreappTheme(
    darkTheme: Boolean = true, // <-- Cambiado: Por defecto es oscuro
    // dynamicColor: Boolean = false, // (Dynamic color lo quitamos)
    content: @Composable () -> Unit
) {
    // 1. Usamos siempre nuestro esquema de color oscuro
    val colorScheme = LevelUpDarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb() // Barra de estado oscura
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // (Usa la tipografía de Type.kt)
        content = content
    )
}