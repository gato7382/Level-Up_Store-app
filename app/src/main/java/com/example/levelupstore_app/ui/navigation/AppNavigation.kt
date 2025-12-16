package com.example.levelupstore_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.levelupstore_app.ui.features.admin.AdminProductScreen
import com.example.levelupstore_app.ui.features.auth.AuthViewModel
import com.example.levelupstore_app.ui.features.auth.LoginScreen
import com.example.levelupstore_app.ui.features.auth.RegisterScreen
import com.example.levelupstore_app.ui.features.catalog.CatalogScreen
import com.example.levelupstore_app.ui.features.home.HomeScreen
import com.example.levelupstore_app.ui.features.product_detail.ProductDetailScreen
import com.example.levelupstore_app.ui.features.profile.ProfileScreen
import com.example.levelupstore_app.ui.utils.AppViewModelFactory

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel(factory = AppViewModelFactory)
) {
    val sessionState by authViewModel.sessionState.collectAsState(initial = null)
    val startDestination = Screen.Login.route

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // --- AUTH ---
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(route = Screen.Register.route) {
            RegisterScreen(navController = navController)
        }

        // --- PRINCIPALES ---
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(route = Screen.Catalog.route) {
            CatalogScreen(navController = navController)
        }

        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) {
            ProductDetailScreen(navController = navController)
        }

        // --- ADMIN (Ruta con argumento opcional) ---
        // Usamos una ruta manual extendida para soportar el parámetro opcional
        composable(
            route = "admin_panel?productId={productId}",
            arguments = listOf(navArgument("productId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) {
            AdminProductScreen(navController = navController)
        }

        // --- PERFIL ---
        composable(route = Screen.ProfileGraph.route) {
            if (sessionState != null) {
                ProfileScreen(mainNavController = navController)
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.ProfileGraph.route) { inclusive = true }
                    }
                }
            }
        }
    }
}
