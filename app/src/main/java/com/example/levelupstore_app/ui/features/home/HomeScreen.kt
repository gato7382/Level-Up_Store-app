package com.example.levelupstore_app.ui.features.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            HeroSection(navController = navController)
        }

        item {
            FeatureSection()
        }

        item {
            CommunitySection()
        }
    }
}