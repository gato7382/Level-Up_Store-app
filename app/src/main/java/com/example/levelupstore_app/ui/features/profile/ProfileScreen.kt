package com.example.levelupstore_app.ui.features.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build // Icono de Herramienta
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.levelupstore_app.ui.navigation.Screen
import com.example.levelupstore_app.ui.utils.AppViewModelFactory

// Definimos los items del menú inferior
private sealed class ProfileNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Data : ProfileNavItem(Screen.ProfileData.route, "Mis Datos", Icons.Default.Person)
    object Orders : ProfileNavItem(Screen.OrderHistory.route, "Mis Pedidos", Icons.Default.List)
}
private val profileNavItems = listOf(ProfileNavItem.Data, ProfileNavItem.Orders)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    mainNavController: NavController, // Router principal (para salir o ir a Admin)
    profileViewModel: ProfileViewModel = viewModel(factory = AppViewModelFactory)
) {
    val uiState by profileViewModel.uiState.collectAsState()
    val user = uiState.user

    // Router anidado para las pestañas internas de perfil
    val profileNavController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                actions = {
                    // 1. BOTÓN DE ADMIN (Tu lógica)
                    // Solo se muestra si el usuario tiene isAdmin = true
                    if (user?.isAdmin == true) {
                        IconButton(onClick = { mainNavController.navigate(Screen.Admin.route) }) {
                            Icon(
                                imageVector = Icons.Default.Build,
                                contentDescription = "Panel de Admin",
                                tint = MaterialTheme.colorScheme.error // Rojo para destacar
                            )
                        }
                    }

                    // 2. BOTÓN DE CERRAR SESIÓN
                    IconButton(onClick = {
                        profileViewModel.logout()
                        // Vuelve al login y borra todo el historial de navegación
                        mainNavController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar Sesión")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by profileNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                profileNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.route == screen.route,
                        onClick = {
                            profileNavController.navigate(screen.route) {
                                popUpTo(profileNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->

        // Contenido Principal (NavHost Anidado)
        NavHost(
            navController = profileNavController,
            startDestination = Screen.ProfileData.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Pestaña: Mis Datos
            composable(Screen.ProfileData.route) {
                if (user != null) {
                    ProfileDataScreen(
                        user = user,
                        onSave = { updatedUser ->
                            profileViewModel.updateProfile(updatedUser)
                        }
                    )
                } else {
                    // Muestra carga si el usuario aún no está listo
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }

            // Pestaña: Mis Pedidos
            composable(Screen.OrderHistory.route) {
                OrderHistoryScreen(orders = uiState.orders)
            }
        }
    }
}