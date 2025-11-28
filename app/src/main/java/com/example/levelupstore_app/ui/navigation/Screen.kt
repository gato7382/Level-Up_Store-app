// Ruta: com/example/levelupstore_app/ui/navigation/Screen.kt
package com.example.levelupstore_app.ui.navigation

sealed class Screen(val route: String) {
    // Rutas principales
    object Home : Screen("home")
    object Login : Screen("login")
    object Register : Screen("register")
    object Catalog : Screen("catalog")


    object Admin : Screen("admin_panel")
    // Ruta con argumento (para un producto)
    object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }

    // --- GRÁFICO DE PERFIL (RUTAS ANIDADAS) ---
    // Esta es la "ruta padre" que agrupa todas las pantallas de perfil
    object ProfileGraph : Screen("profile_graph")

    // Estas son las pantallas HIJAS dentro del gráfico de perfil
    object ProfileData : Screen("profile_data")   // Para "Mis Datos"
    object OrderHistory : Screen("order_history") // Para "Mis Pedidos"
    // --- FIN DE RUTAS DE PERFIL ---
}