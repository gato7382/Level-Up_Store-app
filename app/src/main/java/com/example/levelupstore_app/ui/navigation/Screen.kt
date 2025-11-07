package com.example.levelupstore_app.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Login : Screen("login")
    object Register : Screen("register")
    object Catalog : Screen("catalog")

    object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }

    object ProfileGraph : Screen("profile_graph")

    object ProfileData : Screen("profile_data")
    object OrderHistory : Screen("order_history")
}