package com.example.levelupstore_app.ui.features.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Login
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
import com.example.levelupstore_app.ui.features.auth.AuthViewModel
import com.example.levelupstore_app.ui.features.cart.CartViewModel
import com.example.levelupstore_app.ui.navigation.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(
    navController: NavController,
    cartViewModel: CartViewModel,
    authViewModel: AuthViewModel
) {
    val cartDataState by cartViewModel.cartDataState.collectAsState()
    val sessionState by authViewModel.sessionState.collectAsState(initial = null)

    TopAppBar(
        title = {
            Image(
                painter = painterResource(id = R.drawable.logo_level_up),
                contentDescription = "Logo Level-Up Gamer",
                modifier = Modifier
                    .height(40.dp)
                    .clickable { navController.navigate(Screen.Home.route) }
            )
        },
        actions = {
            if (sessionState == null) {
                IconButton(onClick = { navController.navigate(Screen.Login.route) }) {
                    Icon(
                        imageVector = Icons.Default.Login,
                        contentDescription = "Iniciar Sesión"
                    )
                }
            } else {
                IconButton(onClick = { navController.navigate(Screen.ProfileGraph.route) }) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Mi Perfil"
                    )
                }
            }

            CartIcon(
                totalItems = cartDataState.totalItems,
                onIconClick = { cartViewModel.toggleCart() }
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}