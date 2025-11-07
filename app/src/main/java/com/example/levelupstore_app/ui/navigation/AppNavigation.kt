// Ruta: com/example/levelupstore_app/ui/navigation/AppNavigation.kt
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
import androidx.navigation.navigation // <-- ¡IMPORTACIÓN IMPORTANTE!
import com.example.levelupstore_app.ui.features.auth.AuthViewModel
import com.example.levelupstore_app.ui.features.auth.LoginScreen
import com.example.levelupstore_app.ui.features.auth.RegisterScreen
import com.example.levelupstore_app.ui.features.catalog.CatalogScreen
import com.example.levelupstore_app.ui.features.home.HomeScreen
import com.example.levelupstore_app.ui.features.product_detail.ProductDetailScreen
import com.example.levelupstore_app.ui.features.profile.ProfileScreen // <-- Importa la página
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

        // --- Rutas de Autenticación ---
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(route = Screen.Register.route) {
            RegisterScreen(navController = navController)
        }

        // --- Rutas Principales de la App ---
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

        // --- ¡RUTA DE PERFIL CORREGIDA! ---
        // Aquí le decimos a Navigation que CUALQUIER ruta que empiece con
        // "profile_graph" debe ser manejada por el Composable 'ProfileScreen'.
        // 'ProfileScreen' se convierte en el "dueño" de su propia navegación interna.
        composable(route = Screen.ProfileGraph.route) { // "profile_graph"

            // EL "GUARDIA" DE SEGURIDAD
            if (sessionState != null) {
                // Si SÍ hay usuario, muestra la pantalla de Perfil
                ProfileScreen(mainNavController = navController)
            } else {
                // Si NO hay usuario, redirige a Login
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.ProfileGraph.route) { inclusive = true }
                    }
                }
            }
        }
    }
}