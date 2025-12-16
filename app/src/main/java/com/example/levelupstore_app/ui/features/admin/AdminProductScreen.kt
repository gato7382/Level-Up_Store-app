package com.example.levelupstore_app.ui.features.admin

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.levelupstore_app.ui.components.AppButton
import com.example.levelupstore_app.ui.components.SectionTitle
import com.example.levelupstore_app.ui.navigation.Screen
import com.example.levelupstore_app.ui.utils.AppViewModelFactory
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductScreen(
    navController: NavController,
    viewModel: AdminViewModel = viewModel(factory = AppViewModelFactory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showUpdateDialog by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            val file = uriToFile(context, uri)
            if (file != null) {
                viewModel.onImageChange(file.absolutePath)
                Toast.makeText(context, "Imagen seleccionada", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error al procesar imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearMessage()
        }
    }

    // --- NAVEGACIÓN AUTOMÁTICA AL ELIMINAR ---
    LaunchedEffect(uiState.isDeleteSuccess) {
        if (uiState.isDeleteSuccess) {
            navController.navigate(Screen.Catalog.route) {
                popUpTo(Screen.Catalog.route) { inclusive = true }
            }
        }
    }

    // --- CORRECCIÓN: NAVEGACIÓN AUTOMÁTICA AL GUARDAR (CREAR O EDITAR) ---
    LaunchedEffect(uiState.success) {
         if (uiState.success) {
             // Volvemos atrás para evitar duplicados y limpiamos los campos implícitamente al salir
             navController.popBackStack()
         }
    }

    // --- DIÁLOGO ELIMINAR MEJORADO ---
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Producto") },
            text = {
                // Texto enriquecido para destacar el nombre
                Text(buildAnnotatedString {
                    append("¿Estás seguro de eliminar ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)) {
                        append(uiState.name)
                    }
                    append("?")
                })
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteProduct()
                        showDeleteDialog = false
                    },
                    colors = androidx.compose.material3.ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // --- DIÁLOGO ACTUALIZAR MEJORADO ---
    if (showUpdateDialog) {
        AlertDialog(
            onDismissRequest = { showUpdateDialog = false },
            title = { Text("Actualizar Producto") },
            text = {
                Text(buildAnnotatedString {
                    append("¿Seguro que quieres actualizar los datos de ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)) {
                        append(uiState.name)
                    }
                    append("?")
                })
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.saveProduct()
                    showUpdateDialog = false
                }) {
                    Text("Actualizar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showUpdateDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (uiState.id != null) "Editar Producto" else "Nuevo Producto") },
                actions = {
                    if (uiState.id != null) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar Producto",
                                tint = MaterialTheme.colorScheme.error
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

            OutlinedTextField(
                value = uiState.name,
                onValueChange = { viewModel.onNameChange(it) },
                label = { Text("Nombre del Producto") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.price,
                onValueChange = { newText ->
                    if (newText.all { it.isDigit() || it == '.' } && newText.count { it == '.' } <= 1) {
                        viewModel.onPriceChange(newText)
                    }
                },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.category,
                onValueChange = { viewModel.onCategoryChange(it) },
                label = { Text("Categoría (Ej: Consolas, Accesorios)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val displayText = if (uiState.imageUrl.startsWith("http")) {
                    "Imagen guardada en el servidor (Segura)"
                } else {
                    uiState.imageUrl
                }
                
                OutlinedTextField(
                    value = displayText,
                    onValueChange = { },
                    label = { Text("Imagen") },
                    modifier = Modifier.weight(1f),
                    readOnly = true,
                    singleLine = true,
                    supportingText = {
                        if (uiState.id == null && uiState.imageUrl.isBlank()) {
                            Text("Obligatoria para nuevos productos", color = MaterialTheme.colorScheme.error)
                        } else {
                            Text("Selecciona una de la galería para cambiarla")
                        }
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { imagePickerLauncher.launch("image/*") }) {
                    Icon(imageVector = Icons.Default.Image, contentDescription = "Seleccionar Imagen")
                }
            }

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

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                AppButton(
                    text = if (uiState.id != null) "Guardar Cambios" else "Crear Producto",
                    onClick = {
                        if (uiState.id != null) {
                            showUpdateDialog = true
                        } else {
                            viewModel.saveProduct()
                        }
                    }
                )
            }
        }
    }
}

private fun uriToFile(context: Context, uri: Uri): File? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
        val outputStream = FileOutputStream(tempFile)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
