// Ruta: com/example/levelupstore_app/ui/features/home/FeatureSection.kt
package com.example.levelupstore_app.ui.features.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.levelupstore_app.ui.components.FeatureItem
import com.example.levelupstore_app.ui.components.SectionTitle

// Un data class simple para los datos de esta sección
private data class Feature(val icon: String, val title: String, val description: String)

// Lista privada de los features (basada en tu inicio.html)
private val featuresList = listOf(
    Feature("🚚", "Envío Nacional", "Despachos a todo Chile para que puedas recibir tus productos..."),
    Feature("🎯", "Sistema LevelUp", "Gana puntos con cada compra y referido..."),
    Feature("🎓", "Descuento Duoc", "¿Eres estudiante de Duoc? ¡Obtén un 20% de descuento..."),
    Feature("🛠️", "Soporte Técnico", "Contamos con servicio técnico especializado..."),
    Feature("👥", "Programa de Referidos", "Invita a tus amigos y gana puntos LevelUp..."),
    Feature("⭐", "Productos de Calidad", "Trabajamos solo con los mejores fabricantes...")
)

@Composable
fun FeatureSection(modifier: Modifier = Modifier) {
    // Basado en .features de inicio.html
    Column(modifier = modifier.padding(16.dp)) {
        // 1. Título de la sección (Molécula)
        SectionTitle(
            title = "¿Por qué elegir Level-Up Gamer?",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 2. Cuadrícula de Features (Moléculas)
        // LazyVerticalGrid crea una cuadrícula eficiente que recicla elementos
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 300.dp), // Se adapta a 1 o más columnas
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(800.dp) // Damos una altura fija a la cuadrícula (ajusta según necesites)
            // Ojo: Poner grids "scrolleables" dentro de pantallas "scrolleables"
            // (como el LazyColumn de HomeScreen) puede ser complejo.
            // Una altura fija lo soluciona.
        ) {
            items(featuresList) { feature ->
                // 3. Renderiza una tarjeta por cada item
                FeatureItem(
                    icon = feature.icon,
                    title = feature.title,
                    description = feature.description
                )
            }
        }
    }
}