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
import com.example.levelupstore_app.ui.features.auth.AuthViewModel
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
            LevelUpAppRoot()
        }
    }
}

@Composable
fun LevelUpAppRoot() {
    LevelUpStoreappTheme(darkTheme = true) {

        val navController = rememberNavController()
        val cartViewModel: CartViewModel = viewModel(factory = AppViewModelFactory)
        val authViewModel: AuthViewModel = viewModel(factory = AppViewModelFactory)

        Scaffold(
            topBar = {
                AppHeader(
                    navController = navController,
                    cartViewModel = cartViewModel,
                    authViewModel = authViewModel
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