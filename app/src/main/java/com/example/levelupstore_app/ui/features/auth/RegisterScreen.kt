// Ruta: com/example/levelupstore_app/ui/features/auth/RegisterScreen.kt
package com.example.levelupstore_app.ui.features.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.levelupstore_app.ui.utils.AppViewModelFactory

@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(factory = AppViewModelFactory)
) {
    val uiState by authViewModel.uiState.collectAsState()

    // Estado local solo para la confirmación de contraseña
    var confirmPassword by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(uiState.registerSuccess) {
        if (uiState.registerSuccess) {
            // Si el registro es exitoso, navega a "home"
            navController.navigate("home") {
                // Borra todo el historial de login/registro
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Crear Cuenta",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // --- CAMPO DE "NOMBRE" ---
        OutlinedTextField(
            value = uiState.name,
            onValueChange = { authViewModel.onNameChange(it) },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.errorMessage != null || localError != null,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        // --- CAMPO DE "FECHA DE NACIMIENTO" (ANTES EDAD) ---
        OutlinedTextField(
            value = uiState.birthDate,
            onValueChange = { authViewModel.onBirthDateChange(it) },
            label = { Text("Fecha de Nacimiento") },
            placeholder = { Text("AAAA-MM-DD") }, // <-- Placeholder añadido
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // Mantenemos teclado numérico
            isError = uiState.errorMessage != null || localError != null,
            singleLine = true
        )
        // --- FIN DEL CAMPO REEMPLAZADO ---

        Spacer(modifier = Modifier.height(16.dp))

        // --- CAMPO DE "EMAIL" ---
        OutlinedTextField(
            value = uiState.email,
            onValueChange = { authViewModel.onEmailChange(it) },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = uiState.errorMessage != null || localError != null,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        // --- CAMPO DE "CONTRASEÑA" ---
        OutlinedTextField(
            value = uiState.password,
            onValueChange = { authViewModel.onPasswordChange(it) },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = uiState.errorMessage != null || localError != null,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        // --- CAMPO DE "CONFIRMAR CONTRASEÑA" ---
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it; localError = null },
            label = { Text("Confirmar Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = uiState.errorMessage != null || localError != null,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(24.dp))

        // --- MENSAJES DE ERROR ---
        val errorMessage = localError ?: uiState.errorMessage
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // --- BOTÓN DE REGISTRO ---
        Button(
            onClick = {
                // Validación local (solo contraseñas)
                if (uiState.password != confirmPassword) {
                    localError = "Las contraseñas no coinciden"
                } else {
                    localError = null
                    authViewModel.register() // Llama al ViewModel (que ahora valida email y fecha)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Registrarse")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.popBackStack() }) { // popBackStack() = "volver atrás"
            Text("¿Ya tienes una cuenta? Inicia sesión")
        }
    }
}