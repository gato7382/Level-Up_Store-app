// Ruta: com/example/levelupstore_app/ui/features/auth/LoginScreen.kt
package com.example.levelupstore_app.ui.features.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.levelupstore_app.ui.navigation.Screen // <-- Importa las rutas
import com.example.levelupstore_app.ui.utils.AppViewModelFactory

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(factory = AppViewModelFactory)
) {
    val uiState by authViewModel.uiState.collectAsState()

    // --- ¡AQUÍ ESTÁ LA LÓGICA DE AUTO-LOGIN! ---
    // 1. Observa el estado de la sesión global (si hay usuario o no)
    //    'initial = Unit' es un truco para saber si ya se cargó el estado o no.
    val sessionState by authViewModel.sessionState.collectAsState(initial = Unit)

    LaunchedEffect(sessionState) {
        // 2. Si el estado de la sesión cambia y SÍ hay un usuario (no es null)...
        if (sessionState != null && sessionState != Unit) {
            // ...No te quedes en Login, ¡ve a Home!
            navController.navigate(Screen.Home.route) {
                // Borra la pantalla de Login del historial para no volver a ella
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }
    // --- FIN DE LA LÓGICA DE AUTO-LOGIN ---

    // Este LaunchedEffect es para cuando el login manual es exitoso
    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    // --- 3. QUÉ MOSTRAR ---
    // Si la sesión es null (significa que NO hay usuario logueado),
    // muestra el formulario de login.
    if (sessionState == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Iniciar Sesión",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = uiState.email,
                onValueChange = { authViewModel.onEmailChange(it) },
                label = { Text("Ingresa tu Correo") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = uiState.errorMessage != null,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.password,
                onValueChange = { authViewModel.onPasswordChange(it) },
                label = { Text("Ingresa tu contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                isError = uiState.errorMessage != null,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Button(
                onClick = { authViewModel.login() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Iniciar Sesión")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate(Screen.Register.route) }) {
                Text("¿No tienes una cuenta? Regístrate aquí")
            }
        }
    }
    // Si no (es decir, si 'sessionState' es 'Unit' (cargando) o
    // si 'sessionState' tiene un usuario (redirigiendo)),
    // muestra una pantalla de carga.
    else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}