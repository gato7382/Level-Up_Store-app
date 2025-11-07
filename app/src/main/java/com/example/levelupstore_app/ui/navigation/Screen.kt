// Ruta: com/example/levelupstore_app/ui/navigation/Screen.kt
package com.example.levelupstore_app.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Login : Screen("login")
    object Register : Screen("register")
    object Catalog : Screen("catalog")

    object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }

    // --- RUTAS DE PERFIL ACTUALIZADAS ---
    // Esta será la "ruta padre" del gráfico de perfil
    object ProfileGraph : Screen("profile_graph")

    // Sub-rutas dentro del gráfico de perfil
    object ProfileData : Screen("profile_data")   // Para "Mis Datos"
    object OrderHistory : Screen("order_history") // Para "Mis Pedidos"
    // --- FIN DE ACTUALIZACIÓN ---
}