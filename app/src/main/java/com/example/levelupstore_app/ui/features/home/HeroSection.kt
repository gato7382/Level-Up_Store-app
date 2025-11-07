package com.example.levelupstore_app.ui.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.levelupstore_app.R // ¡Importante para acceder a res/drawable!
import com.example.levelupstore_app.ui.components.AppButton

@Composable
fun HeroSection(navController: NavController) {

    // --- Definición de colores (puedes moverlos a ui/theme/Color.kt) ---
    val GreenGlow = Color(0xFF39FF14) // Color verde de tu CSS
    // --- Fin de definición de colores ---

    // Box es como un <div> con position: relative
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp), // Altura fija para el "Hero"
        contentAlignment = Alignment.Center // Centra el contenido
    ) {
        // 1. Imagen de Fondo
        Image(
            // ¡Aquí usa la imagen de res/drawable!
            painter = painterResource(id = R.drawable.background1),
            contentDescription = "Fondo Hero",
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.3f), // Opacidad (como en tu CSS)
            contentScale = ContentScale.Crop // Rellena el espacio
        )

        // 2. Contenido (el div .hero-content)
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Título (el h1)
            Text(
                text = "¡Desafía tus límites con Level-Up Gamer!",
                style = MaterialTheme.typography.headlineMedium, // Tamaño de fuente
                textAlign = TextAlign.Center,
                color = Color.White,
                fontWeight = FontWeight.Bold
                // fontFamily = OrbitronFontFamily // (Si configuras la fuente Orbitron)
            )

            // Subtítulo (el span verde)
            Text(
                text = "El Siguiente Nivel Es Solo El Comienzo",
                style = MaterialTheme.typography.headlineMedium,
                color = GreenGlow, // Tu span verde
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
                // fontFamily = OrbitronFontFamily
            )

            Spacer(modifier = Modifier.height(16.dp)) // Espaciador

            // Párrafo (el <p>)
            Text(
                text = "Encuentra el equipamiento gamer que necesitas para dominar. Calidad, rendimiento y estilo en un solo lugar.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = Color.White.copy(alpha = 0.8f) // Color de texto atenuado
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón (el .cta-button)
            AppButton(
                onClick = { navController.navigate("catalog") }, // Asumimos ruta "catalog"
                text = "¡Explora el Catálogo!"
            )
        }
    }
}