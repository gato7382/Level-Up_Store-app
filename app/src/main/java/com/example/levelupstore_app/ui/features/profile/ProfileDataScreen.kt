// Ruta: com/example/levelupstore_app/ui/features/profile/ProfileDataScreen.kt
package com.example.levelupstore_app.ui.features.profile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.levelupstore_app.data.model.User
import com.example.levelupstore_app.ui.components.AppButton
import com.example.levelupstore_app.ui.components.SectionTitle

/**
 * Organismo/Pantalla para "Mis Datos".
 * Muestra el formulario para editar el perfil
 */
@OptIn(ExperimentalMaterial3Api::class) // Necesario para ExposedDropdownMenuBox
@Composable
fun ProfileDataScreen(
    user: User, // Recibe el usuario actual del ViewModel
    onSave: (User) -> Unit // Recibe la función de "guardar" del ViewModel
) {
    // --- ESTADO LOCAL PARA TODOS LOS CAMPOS ---
    // Inicializa el estado local con los datos del usuario
    var nombre by remember(user.nombre) { mutableStateOf(user.nombre) }
    var apellidos by remember(user.apellidos) { mutableStateOf(user.apellidos ?: "") }
    var telefono by remember(user.telefono) { mutableStateOf(user.telefono ?: "") }
    var fechaNacimiento by remember(user.fechaNacimiento) { mutableStateOf(user.fechaNacimiento ?: "") }
    var genero by remember(user.genero) { mutableStateOf(user.genero ?: "other") }
    var categoriaFavorita by remember(user.categoriaFavorita) { mutableStateOf(user.categoriaFavorita ?: "") }
    var plataformaPrincipal by remember(user.plataformaPrincipal) { mutableStateOf(user.plataformaPrincipal ?: "") }
    var gamerTag by remember(user.gamerTag) { mutableStateOf(user.gamerTag ?: "") }
    var bio by remember(user.bio) { mutableStateOf(user.bio ?: "") }
    var newsletter by remember(user.newsletter) { mutableStateOf(user.newsletter ?: false) }
    var notificaciones by remember(user.notificaciones) { mutableStateOf(user.notificaciones ?: false) }

    val context = LocalContext.current

    // Opciones para los menús desplegables (Selects)
    val generos = listOf("male", "female", "other")
    val categorias = listOf("consoles", "computers", "accessories", "boardgames", "chairs", "clothing")
    val plataformas = listOf("pc", "playstation", "xbox", "nintendo", "mobile")

    // Estado para los menús desplegables
    var generoExpanded by remember { mutableStateOf(false) }
    var categoriaExpanded by remember { mutableStateOf(false) }
    var plataformaExpanded by remember { mutableStateOf(false) }

    // --- INICIO DE LA UI ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()) // Hace la columna "scrolleable"
    ) {
        SectionTitle(title = "Información Personal")
        Spacer(modifier = Modifier.height(16.dp))

        // Campos de Texto
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombres") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = apellidos, onValueChange = { apellidos = it }, label = { Text("Apellidos") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = user.email, onValueChange = {}, label = { Text("Correo Electrónico") }, modifier = Modifier.fillMaxWidth(), enabled = false)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = fechaNacimiento, onValueChange = { fechaNacimiento = it }, label = { Text("Fecha de Nacimiento (AAAA-MM-DD)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        // Campo 'Select' para Género
        ExposedDropdownMenuBox(expanded = generoExpanded, onExpandedChange = { generoExpanded = !generoExpanded }) {
            OutlinedTextField(
                value = genero,
                onValueChange = {},
                readOnly = true,
                label = { Text("Género") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = generoExpanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = generoExpanded, onDismissRequest = { generoExpanded = false }) {
                generos.forEach { g ->
                    DropdownMenuItem(text = { Text(g) }, onClick = { genero = g; generoExpanded = false })
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        SectionTitle(title = "Preferencias Gamer")
        Spacer(modifier = Modifier.height(16.dp))

        // Campo 'Select' para Categoría
        ExposedDropdownMenuBox(expanded = categoriaExpanded, onExpandedChange = { categoriaExpanded = !categoriaExpanded }) {
            OutlinedTextField(
                value = categoriaFavorita,
                onValueChange = {},
                readOnly = true,
                label = { Text("Categoría Favorita") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoriaExpanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = categoriaExpanded, onDismissRequest = { categoriaExpanded = false }) {
                categorias.forEach { c ->
                    DropdownMenuItem(text = { Text(c) }, onClick = { categoriaFavorita = c; categoriaExpanded = false })
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Campo 'Select' para Plataforma
        ExposedDropdownMenuBox(expanded = plataformaExpanded, onExpandedChange = { plataformaExpanded = !plataformaExpanded }) {
            OutlinedTextField(
                value = plataformaPrincipal,
                onValueChange = {},
                readOnly = true,
                label = { Text("Plataforma Principal") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = plataformaExpanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = plataformaExpanded, onDismissRequest = { plataformaExpanded = false }) {
                plataformas.forEach { p ->
                    DropdownMenuItem(text = { Text(p) }, onClick = { plataformaPrincipal = p; plataformaExpanded = false })
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = gamerTag, onValueChange = { gamerTag = it }, label = { Text("Gamer Tag") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = bio, onValueChange = { bio = it }, label = { Text("Biografía") }, modifier = Modifier.fillMaxWidth().height(120.dp))

        Spacer(modifier = Modifier.height(24.dp))
        SectionTitle(title = "Notificaciones")
        Spacer(modifier = Modifier.height(16.dp))

        // Checkbox para Newsletter
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = newsletter, onCheckedChange = { newsletter = it })
            Text("Deseo recibir novedades y ofertas", modifier = Modifier.padding(start = 8.dp))
        }
        // Checkbox para Notificaciones
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = notificaciones, onCheckedChange = { notificaciones = it })
            Text("Activar notificaciones de eventos", modifier = Modifier.padding(start = 8.dp))
        }

        // Botón de Guardar
        Spacer(modifier = Modifier.height(24.dp))
        AppButton(
            onClick = {
                // Crea el objeto User actualizado y lo envía al ViewModel
                val updatedUser = user.copy(
                    nombre = nombre,
                    apellidos = apellidos,
                    telefono = telefono,
                    fechaNacimiento = fechaNacimiento,
                    genero = genero,
                    categoriaFavorita = categoriaFavorita,
                    plataformaPrincipal = plataformaPrincipal,
                    gamerTag = gamerTag,
                    bio = bio,
                    newsletter = newsletter,
                    notificaciones = notificaciones
                )
                onSave(updatedUser)
                Toast.makeText(context, "Perfil guardado", Toast.LENGTH_SHORT).show()
            },
            text = "Guardar Cambios"
        )
    }
}