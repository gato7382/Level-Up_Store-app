// Ruta: com/example/levelupstore_app/ui/features/auth/LoginScreen.kt
package com.example.levelupstore_app.ui.features.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.* // Importa Material 3 completo
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
import com.example.levelupstore_app.ui.utils.AppViewModelFactory

/**
 * Esta es la "Página" de Login (un @Composable).
 * Es el equivalente a tu 'LoginForm.jsx' y 'LoginPage.jsx'.
 */
@Composable
fun LoginScreen(
    navController: NavController,
    // 1. Pedimos el ViewModel, USANDO nuestra Fábrica
    authViewModel: AuthViewModel = viewModel(factory = AppViewModelFactory)
) {
    // 2. Observamos el estado del ViewModel (como 'useContext')
    //    'uiState' contiene el email, password, error, etc.
    val uiState by authViewModel.uiState.collectAsState()

    // 3. "Efecto" que se dispara cuando 'loginSuccess' cambia
    //    (Equivalente a un 'useEffect' de React)
    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            // Si el login fue exitoso, navega a "home"
            // y borra la pantalla de login del historial
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    // --- 4. Esta es tu UI (el JSX de Kotlin) ---
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

        // Campo de Email (Equivalente a FormField + Input)
        OutlinedTextField(
            value = uiState.email,
            // 5. Conectamos la UI al ViewModel
            onValueChange = { authViewModel.onEmailChange(it) },
            label = { Text("Ingresa tu Correo") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = uiState.errorMessage != null,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Contraseña
        OutlinedTextField(
            value = uiState.password,
            // 5. Conectamos la UI al ViewModel
            onValueChange = { authViewModel.onPasswordChange(it) },
            label = { Text("Ingresa tu contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = uiState.errorMessage != null,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Muestra el error si existe
        if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage!!, // El '!!' dice "estoy seguro que no es nulo aquí"
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Botón de Login (Equivalente a Button)
        Button(
            // 5. Conectamos la UI al ViewModel
            onClick = { authViewModel.login() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading // Deshabilita si está cargando
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

        // --- CÓDIGO AÑADIDO ---
        // Equivalente al <p>¿No tienes una cuenta?...</p> de tu ini_sesion.html
        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.navigate("register") }) {
            Text("¿No tienes una cuenta? Regístrate aquí")
        }
        // --- FIN DEL CÓDIGO AÑADIDO ---
    }
}