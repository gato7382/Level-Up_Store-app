// Ruta: com/example/levelupstore_app/ui/features/profile/ProfileScreen.kt
package com.example.levelupstore_app.ui.features.profile

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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

// --- Define los items de la barra de navegación inferior ---
private sealed class ProfileNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Data : ProfileNavItem(Screen.ProfileData.route, "Mis Datos", Icons.Default.Person)
    object Orders : ProfileNavItem(Screen.OrderHistory.route, "Mis Pedidos", Icons.Default.List)
}
private val profileNavItems = listOf(ProfileNavItem.Data, ProfileNavItem.Orders)
// --- Fin de la definición ---


/**
 * La PÁGINA principal de Perfil.
 * Contiene un Scaffold con navegación inferior anidada.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    mainNavController: NavController, // El router principal de la app
    profileViewModel: ProfileViewModel = viewModel(factory = AppViewModelFactory)
) {
    // 1. Observa el estado del ViewModel (el "cerebro")
    val uiState by profileViewModel.uiState.collectAsState()
    val user = uiState.user

    // 2. Creamos un NUEVO NavController SÓLO para esta sección (el router anidado)
    val profileNavController = rememberNavController()

    Scaffold(
        // 3. Barra Superior (TopBar) con título y botón de "Cerrar Sesión"
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                actions = {
                    IconButton(onClick = {
                        profileViewModel.logout()
                        // Vuelve al login y borra todo el historial
                        mainNavController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar Sesión")
                    }
                }
            )
        },
        // 4. Barra de Navegación Inferior (para "Mis Datos" y "Mis Pedidos")
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by profileNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                // Itera sobre nuestros 2 items de navegación
                profileNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        // Comprueba si la ruta actual es la seleccionada
                        selected = currentDestination?.route == screen.route,
                        onClick = {
                            // Navega a la ruta de la sub-pantalla
                            profileNavController.navigate(screen.route) {
                                // Lógica estándar para navegación inferior:
                                // Evita acumular pantallas en el historial
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
    ) { innerPadding -> // 'innerPadding' es el espacio que ocupan las barras

        // 5. Contenido Principal (el "router anidado")
        NavHost(
            navController = profileNavController,
            startDestination = Screen.ProfileData.route, // Inicia en "Mis Datos"
            modifier = Modifier.padding(innerPadding) // Aplica el padding
        ) {

            // Ruta para "Mis Datos"
            composable(Screen.ProfileData.route) {
                if (user != null) { // Muestra solo si el usuario ha cargado
                    ProfileDataScreen(
                        user = user,
                        onSave = { updatedUser ->
                            profileViewModel.updateProfile(updatedUser)
                        }
                    )
                }
            }

            // Ruta para "Mis Pedidos"
            composable(Screen.OrderHistory.route) {
                OrderHistoryScreen(orders = uiState.orders)
            }
        }
    }
}