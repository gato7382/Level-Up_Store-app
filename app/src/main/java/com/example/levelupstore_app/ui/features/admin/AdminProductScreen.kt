package com.example.levelupstore_app.ui.features.admin

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.levelupstore_app.ui.components.AppButton
import com.example.levelupstore_app.ui.components.SectionTitle
import com.example.levelupstore_app.ui.utils.AppViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductScreen(
    navController: NavController,
    viewModel: AdminViewModel = viewModel(factory = AppViewModelFactory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // 1. Efecto para mostrar mensajes (Toasts)
    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearMessage()
        }
    }

    // 2. Efecto para salir si se eliminó correctamente
    LaunchedEffect(uiState.isDeleteSuccess) {
        if (uiState.isDeleteSuccess) {
            navController.popBackStack() // Vuelve a la pantalla anterior
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                // Cambia el título según si estamos editando o creando
                title = { Text(if (uiState.id != null) "Editar Producto" else "Nuevo Producto") },
                actions = {
                    // Botón ELIMINAR: Solo visible si estamos editando (id no es null)
                    if (uiState.id != null) {
                        IconButton(onClick = { viewModel.deleteProduct() }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar Producto",
                                tint = MaterialTheme.colorScheme.error // Color rojo
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionTitle(title = if (uiState.id != null) "Editar Datos" else "Datos del Nuevo Producto")

            // --- Campo: Nombre ---
            OutlinedTextField(
                value = uiState.name,
                onValueChange = { viewModel.onNameChange(it) },
                label = { Text("Nombre del Producto") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // --- Campo: Precio ---
            OutlinedTextField(
                value = uiState.price,
                onValueChange = { viewModel.onPriceChange(it) },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            // --- Campo: Categoría ---
            OutlinedTextField(
                value = uiState.category,
                onValueChange = { viewModel.onCategoryChange(it) },
                label = { Text("Categoría (Ej: Consolas, Accesorios)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // --- Campo: URL Imagen ---
            OutlinedTextField(
                value = uiState.imageUrl,
                onValueChange = { viewModel.onImageChange(it) },
                label = { Text("Ruta Imagen (Ej: imgs/foto.jpg)") },
                modifier = Modifier.fillMaxWidth(),
                supportingText = { Text("Asegúrate de que la imagen esté en la carpeta assets/imgs/") },
                singleLine = true
            )

            // --- Campo: Descripción ---
            OutlinedTextField(
                value = uiState.description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- Botón Guardar ---
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                AppButton(
                    // Cambia el texto del botón según el modo
                    text = if (uiState.id != null) "Guardar Cambios" else "Crear Producto",
                    onClick = { viewModel.saveProduct() }
                )
            }
        }
    }
}