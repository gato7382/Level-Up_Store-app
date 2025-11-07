// Ruta: com/example/levelupstore_app/MainActivity.kt
package com.example.levelupstore_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.levelupstore_app.ui.features.auth.AuthViewModel // <-- 1. IMPORTA AUTHVIEWMODEL
import com.example.levelupstore_app.ui.features.common.AppHeader
import com.example.levelupstore_app.ui.features.common.CartDropdown
import com.example.levelupstore_app.ui.features.cart.CartViewModel
import com.example.levelupstore_app.ui.navigation.AppNavigation
import com.example.levelupstore_app.ui.theme.LevelUpStoreappTheme
import com.example.levelupstore_app.ui.utils.AppViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LevelUpAppRoot() // Llama a nuestro composable raíz
        }
    }
}

/**
 * El Composable raíz que configura el layout global de la app.
 */
@Composable
fun LevelUpAppRoot() {
    LevelUpStoreappTheme(darkTheme = true) {

        // 2. Obtén AMBOS ViewModels globales aquí
        val navController = rememberNavController()
        val cartViewModel: CartViewModel = viewModel(factory = AppViewModelFactory)
        val authViewModel: AuthViewModel = viewModel(factory = AppViewModelFactory) // <-- AÑADIDO

        Scaffold(
            topBar = {
                // 3. Pasa AMBOS ViewModels al Header
                AppHeader(
                    navController = navController,
                    cartViewModel = cartViewModel,
                    authViewModel = authViewModel // <-- AÑADIDO
                )
            }
        ) { innerPadding ->

            AppNavigation(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )

            CartDropdown(cartViewModel = cartViewModel)
        }
    }
}