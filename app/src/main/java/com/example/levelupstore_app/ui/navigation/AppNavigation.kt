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
// --- Imports de tus pantallas ---
import com.example.levelupstore_app.ui.features.admin.AdminProductScreen // <-- ¡Importante para el Admin!
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
    // 1. Recibimos el AuthViewModel para verificar la sesión ("El Guardia")
    authViewModel: AuthViewModel = viewModel(factory = AppViewModelFactory)
) {
    // 2. Observamos el estado de la sesión (usuario logueado o null)
    val sessionState by authViewModel.sessionState.collectAsState(initial = null)

    // 3. Ruta de inicio (siempre Login, que redirige si ya hay sesión)
    val startDestination = Screen.Login.route

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        // --- RUTAS DE AUTENTICACIÓN ---
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(route = Screen.Register.route) {
            RegisterScreen(navController = navController)
        }

        // --- RUTAS PRINCIPALES ---
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(route = Screen.Catalog.route) {
            CatalogScreen(navController = navController)
        }

        // Ruta con argumento (ID del producto)
        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) {
            ProductDetailScreen(navController = navController)
        }

        // --- RUTA DE ADMINISTRADOR (¡NUEVA!) ---
        composable(route = Screen.Admin.route) {
            AdminProductScreen(navController = navController)
        }

        // --- RUTA DE PERFIL (PROTEGIDA) ---
        composable(route = Screen.ProfileGraph.route) {

            // LÓGICA DEL GUARDIA:
            // Comprueba si el usuario está logueado ANTES de mostrar la pantalla
            if (sessionState != null) {
                // Si SÍ hay usuario, muestra la pantalla de Perfil
                // Le pasamos 'navController' para que pueda navegar fuera (ej. al Admin o Login)
                ProfileScreen(mainNavController = navController)
            } else {
                // Si NO hay usuario, redirige automáticamente a Login
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        // Borra el intento de ir a perfil del historial
                        popUpTo(Screen.ProfileGraph.route) { inclusive = true }
                    }
                }
            }
        }
        composable(
            route = Screen.Admin.route,
            arguments = listOf(navArgument("productId") {
                type = NavType.StringType
                nullable = true // Es opcional (null = crear nuevo)
            })
        ) {
            AdminProductScreen(navController = navController)
        }
    }
}