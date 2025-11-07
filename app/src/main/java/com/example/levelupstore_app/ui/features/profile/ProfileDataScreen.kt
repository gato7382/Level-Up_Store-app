// Ruta: com/example/levelupstore_app/ui/features/profile/ProfileDataScreen.kt
package com.example.levelupstore_app.ui.features.profile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions // <-- CAMBIO: Importado
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType // <-- CAMBIO: Importado
import androidx.compose.ui.unit.dp
import com.example.levelupstore_app.data.model.User
import com.example.levelupstore_app.ui.components.AppButton
import com.example.levelupstore_app.ui.components.SectionTitle
import com.example.levelupstore_app.ui.utils.DateVisualTransformation // <-- CAMBIO: Importado
import java.time.DateTimeException // <-- CAMBIO: Importado
import java.time.LocalDate // <-- CAMBIO: Importado
import java.time.Period // <-- CAMBIO: Importado
import java.time.format.DateTimeFormatter // <-- CAMBIO: Importado

// --- ¡CAMBIO AQUÍ! ---
// Definimos los Mapas (diccionarios) para 'valor' y 'etiqueta'
private val generosOptions = mapOf(
    "male" to "Masculino",
    "female" to "Femenino",
    "other" to "Otro / Prefiero no decir"
)
private val categoriasOptions = mapOf(
    "" to "Selecciona una categoría...",
    "consoles" to "Consolas",
    "computers" to "Computadores Gamers",
    "accessories" to "Accesorios",
    "boardgames" to "Juegos de Mesa",
    "chairs" to "Sillas Gamers",
    "clothing" to "Ropa Gamer"
)
private val plataformasOptions = mapOf(
    "" to "Selecciona una plataforma...",
    "pc" to "PC",
    "playstation" to "PlayStation",
    "xbox" to "Xbox",
    "nintendo" to "Nintendo",
    "mobile" to "Móvil"
)

// --- ¡CAMBIO AQUÍ! ---
// Formateadores para la fecha (igual que en AuthViewModel)
private val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // Formato guardado
private val inputFormatter = DateTimeFormatter.ofPattern("ddMMuuuu")   // Formato de entrada (8 dígitos)

/**
 * Función auxiliar para convertir la fecha de la BD (AAAA-MM-DD)
 * al formato de 8 dígitos (DDMMYYYY) para el campo de texto.
 */
private fun formatToInput(dateString: String?): String {
    if (dateString.isNullOrEmpty()) return ""
    return try {
        val date = LocalDate.parse(dateString, outputFormatter)
        date.format(inputFormatter)
    } catch (e: Exception) {
        "" // Devuelve vacío si la fecha guardada está corrupta
    }
}
// --- FIN DEL CAMBIO ---


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDataScreen(
    user: User, // Recibe el usuario actual del ViewModel
    onSave: (User) -> Unit // Recibe la función de "guardar" del ViewModel
) {
    // --- ESTADO LOCAL PARA TODOS LOS CAMPOS ---
    var nombre by remember(user.nombre) { mutableStateOf(user.nombre) }
    var apellidos by remember(user.apellidos) { mutableStateOf(user.apellidos ?: "") }
    var telefono by remember(user.telefono) { mutableStateOf(user.telefono ?: "") }

    // --- ¡CAMBIO AQUÍ! ---
    // El estado 'fechaNacimientoInput' guarda los 8 dígitos (ej: "27101995")
    var fechaNacimientoInput by remember(user.fechaNacimiento) {
        mutableStateOf(formatToInput(user.fechaNacimiento)) // Usa la función de conversión
    }
    // --- FIN DEL CAMBIO ---

    var genero by remember(user.genero) { mutableStateOf(user.genero ?: "other") }
    var categoriaFavorita by remember(user.categoriaFavorita) { mutableStateOf(user.categoriaFavorita ?: "") }
    var plataformaPrincipal by remember(user.plataformaPrincipal) { mutableStateOf(user.plataformaPrincipal ?: "") }
    var gamerTag by remember(user.gamerTag) { mutableStateOf(user.gamerTag ?: "") }
    var bio by remember(user.bio) { mutableStateOf(user.bio ?: "") }
    var newsletter by remember(user.newsletter) { mutableStateOf(user.newsletter ?: false) }
    var notificaciones by remember(user.notificaciones) { mutableStateOf(user.notificaciones ?: false) }

    val context = LocalContext.current

    var generoExpanded by remember { mutableStateOf(false) }
    var categoriaExpanded by remember { mutableStateOf(false) }
    var plataformaExpanded by remember { mutableStateOf(false) }

    // --- INICIO DE LA UI ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        SectionTitle(title = "Información Personal")
        Spacer(modifier = Modifier.height(16.dp))

        // ... (Campos Nombre, Apellidos, Email, Teléfono - Sin cambios)
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombres") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = apellidos, onValueChange = { apellidos = it }, label = { Text("Apellidos") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = user.email, onValueChange = {}, label = { Text("Correo Electrónico") }, modifier = Modifier.fillMaxWidth(), enabled = false)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        // --- ¡CAMBIO AQUÍ! ---
        // Campo de Fecha de Nacimiento (igual que en RegisterScreen)
        OutlinedTextField(
            value = fechaNacimientoInput, // El valor sin formato (ej. "27101995")
            onValueChange = { newText ->
                // Solo acepta 8 dígitos
                if (newText.length <= 8 && newText.all { it.isDigit() }) {
                    fechaNacimientoInput = newText
                }
            },
            label = { Text("Fecha de Nacimiento") },
            placeholder = { Text("DD-MM-YYYY") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            visualTransformation = DateVisualTransformation() // ¡Aplica el formateo automático!
        )
        // --- FIN DEL CAMBIO ---
        Spacer(modifier = Modifier.height(16.dp))

        // ... (Campos Select de Género, Categoría, Plataforma - Sin cambios)
        ExposedDropdownMenuBox(expanded = generoExpanded, onExpandedChange = { generoExpanded = !generoExpanded }) {
            OutlinedTextField(value = generosOptions[genero] ?: "", onValueChange = {}, readOnly = true, label = { Text("Género") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = generoExpanded) }, modifier = Modifier.fillMaxWidth().menuAnchor())
            ExposedDropdownMenu(expanded = generoExpanded, onDismissRequest = { generoExpanded = false }) {
                generosOptions.forEach { (key, label) ->
                    DropdownMenuItem(text = { Text(label) }, onClick = { genero = key; generoExpanded = false })
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        SectionTitle(title = "Preferencias Gamer")
        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(expanded = categoriaExpanded, onExpandedChange = { categoriaExpanded = !categoriaExpanded }) {
            OutlinedTextField(value = categoriasOptions[categoriaFavorita] ?: "", onValueChange = {}, readOnly = true, label = { Text("Categoría Favorita") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoriaExpanded) }, modifier = Modifier.fillMaxWidth().menuAnchor())
            ExposedDropdownMenu(expanded = categoriaExpanded, onDismissRequest = { categoriaExpanded = false }) {
                categoriasOptions.forEach { (key, label) ->
                    DropdownMenuItem(text = { Text(label) }, onClick = { categoriaFavorita = key; categoriaExpanded = false })
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(expanded = plataformaExpanded, onExpandedChange = { plataformaExpanded = !plataformaExpanded }) {
            OutlinedTextField(value = plataformasOptions[plataformaPrincipal] ?: "", onValueChange = {}, readOnly = true, label = { Text("Plataforma Principal") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = plataformaExpanded) }, modifier = Modifier.fillMaxWidth().menuAnchor())
            ExposedDropdownMenu(expanded = plataformaExpanded, onDismissRequest = { plataformaExpanded = false }) {
                plataformasOptions.forEach { (key, label) ->
                    DropdownMenuItem(text = { Text(label) }, onClick = { plataformaPrincipal = key; plataformaExpanded = false })
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

        // ... (Checkboxes - Sin cambios)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = newsletter, onCheckedChange = { newsletter = it })
            Text("Deseo recibir novedades y ofertas", modifier = Modifier.padding(start = 8.dp))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = notificaciones, onCheckedChange = { notificaciones = it })
            Text("Activar notificaciones de eventos", modifier = Modifier.padding(start = 8.dp))
        }

        // Botón de Guardar
        Spacer(modifier = Modifier.height(24.dp))
        AppButton(
            onClick = {
                // --- ¡CAMBIO AQUÍ! ---
                // Validación y Conversión de Fecha (igual que en AuthViewModel)
                var dateToSave: String? = null
                if (fechaNacimientoInput.isNotBlank()) {
                    if (fechaNacimientoInput.length != 8) {
                        Toast.makeText(context, "Fecha inválida, usa 8 dígitos.", Toast.LENGTH_SHORT).show()
                        return@AppButton // Detiene el guardado
                    }
                    try {
                        val birthDate = LocalDate.parse(fechaNacimientoInput, inputFormatter)
                        val today = LocalDate.now()
                        val age = Period.between(birthDate, today).years
                        if (age < 18) {
                            Toast.makeText(context, "Debes ser mayor de 18 años.", Toast.LENGTH_SHORT).show()
                            return@AppButton // Detiene el guardado
                        }
                        dateToSave = birthDate.format(outputFormatter) // Formato "AAAA-MM-DD"
                    } catch (e: DateTimeException) {
                        Toast.makeText(context, "Fecha inválida (ej: día 32).", Toast.LENGTH_SHORT).show()
                        return@AppButton // Detiene el guardado
                    }
                }
                // --- FIN DEL CAMBIO ---

                // Crea el objeto User actualizado y lo envía al ViewModel
                val updatedUser = user.copy(
                    nombre = nombre,
                    apellidos = apellidos,
                    telefono = telefono,
                    fechaNacimiento = dateToSave, // <-- Pasa la fecha convertida
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