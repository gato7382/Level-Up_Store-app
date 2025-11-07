// Ruta: com/example/levelupstore_app/ui/features/common/AppHeader.kt
package com.example.levelupstore_app.ui.features.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle // <-- 1. IMPORTA ICONO PERFIL
import androidx.compose.material.icons.filled.Login          // <-- 1. IMPORTA ICONO LOGIN
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.levelupstore_app.R
import com.example.levelupstore_app.ui.components.CartIcon
import com.example.levelupstore_app.ui.features.auth.AuthViewModel // <-- 2. IMPORTA AUTHVIEWMODEL
import com.example.levelupstore_app.ui.features.cart.CartViewModel
import com.example.levelupstore_app.ui.navigation.Screen

/**
 * Organismo que representa el Header (TopAppBar) global de la app.
 * Reemplaza <header>
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(
    navController: NavController,
    cartViewModel: CartViewModel, // <-- 3. AHORA RECIBE LOS VIEWMODELS
    authViewModel: AuthViewModel  // <-- 3. AHORA RECIBE LOS VIEWMODELS
) {
    // 4. Observa AMBOS estados
    val cartDataState by cartViewModel.cartDataState.collectAsState()
    val sessionState by authViewModel.sessionState.collectAsState(initial = null) // Observa el usuario

    TopAppBar(
        title = {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo_level_up),
                contentDescription = "Logo Level-Up Gamer",
                modifier = Modifier
                    .height(40.dp)
                    .clickable { navController.navigate(Screen.Home.route) }
            )
        },
        actions = {
            // --- 5. LÓGICA DE ICONO DE PERFIL/LOGIN ---
            if (sessionState == null) {
                // No hay usuario: Muestra icono de "Login"
                IconButton(onClick = { navController.navigate(Screen.Login.route) }) {
                    Icon(
                        imageVector = Icons.Default.Login,
                        contentDescription = "Iniciar Sesión"
                    )
                }
            } else {
                // Sí hay usuario: Muestra icono de "Perfil"
                IconButton(onClick = { navController.navigate(Screen.ProfileGraph.route) }) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Mi Perfil"
                    )
                }
            }
            // --- FIN DE LA LÓGICA ---

            // Icono del Carrito (Molécula)
            CartIcon(
                totalItems = cartDataState.totalItems,
                onIconClick = { cartViewModel.toggleCart() } // Abre/cierra el dropdown
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}