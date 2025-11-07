// Ruta: com/example/levelupstore_app/ui/features/home/HomeScreen.kt
package com.example.levelupstore_app.ui.features.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    // LazyColumn permite que toda la página sea "scrollable"
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // Item 1: El Hero
        item {
            HeroSection(navController = navController)
        }

        // Item 2: Las Features
        item {
            FeatureSection()
        }

        // Item 3: La Comunidad (¡AÑADIDO!)
        item {
            CommunitySection()
        }
    }
}