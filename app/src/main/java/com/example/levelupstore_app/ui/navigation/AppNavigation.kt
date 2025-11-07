// Ruta: com/example/levelupstore_app/ui/navigation/AppNavigation.kt
package com.example.levelupstore_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.levelupstore_app.ui.features.auth.LoginScreen
import com.example.levelupstore_app.ui.features.auth.RegisterScreen
import com.example.levelupstore_app.ui.features.catalog.CatalogScreen
import com.example.levelupstore_app.ui.features.home.HomeScreen
import com.example.levelupstore_app.ui.features.product_detail.ProductDetailScreen
import com.example.levelupstore_app.ui.features.profile.ProfileScreen // <-- Importa la página

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {

        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController)
        }

        composable(route = Screen.Register.route) {
            RegisterScreen(navController = navController)
        }

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

        // --- RUTA DE PERFIL (CORREGIDA) ---
        // Simplemente le decimos que CUALQUIER ruta que empiece con "profile"
        // debe ser manejada por nuestro ProfileScreen (que tiene su propio router anidado).
        composable(route = Screen.ProfileGraph.route) { // "profile_graph"
            ProfileScreen(mainNavController = navController)
        }
    }
}