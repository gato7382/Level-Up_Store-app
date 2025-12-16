package com.example.levelupstore_app.ui.features.profile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.levelupstore_app.data.model.User
import com.example.levelupstore_app.ui.components.AppButton
import com.example.levelupstore_app.ui.components.SectionTitle
import com.example.levelupstore_app.ui.utils.DateVisualTransformation
import java.time.DateTimeException
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

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

private val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
private val inputFormatter = DateTimeFormatter.ofPattern("ddMMuuuu")

private fun formatToInput(dateString: String?): String {
    if (dateString.isNullOrEmpty()) return ""
    return try {
        val date = LocalDate.parse(dateString, outputFormatter)
        date.format(inputFormatter)
    } catch (e: Exception) {
        ""
    }
}

// Clase para formatear visualmente el teléfono chileno
// Entrada: "12345678" -> Salida Visual: "+56 9 1234 5678"
class ChilePhoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Prefijo fijo
        val prefix = "+56 9 "
        val raw = text.text
        
        // Construimos el string final
        val out = StringBuilder(prefix)
        for (i in raw.indices) {
            out.append(raw[i])
            if (i == 3) out.append(" ") // Espacio después de los primeros 4 dígitos
        }

        val phoneNumberTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // +56 9 (6 chars)
                if (offset <= 0) return 6
                if (offset <= 4) return 6 + offset
                return 6 + offset + 1 // +1 por el espacio extra
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 6) return 0
                if (offset <= 10) return offset - 6
                return offset - 7
            }
        }

        return TransformedText(AnnotatedString(out.toString()), phoneNumberTranslator)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDataScreen(
    user: User,
    onSave: (User) -> Unit
) {
    var nombre by remember(user.nombre) { mutableStateOf(user.nombre) }
    var apellidos by remember(user.apellidos) { mutableStateOf(user.apellidos ?: "") }
    
    // Telefono: Extraemos solo los últimos 8 dígitos si viene formateado
    // Asumimos que el backend puede mandar "+56912345678" o "12345678"
    // Queremos editar solo los 8 dígitos finales.
    var telefonoInput by remember(user.telefono) {
        val raw = user.telefono ?: ""
        // Si viene con 569, lo quitamos para la edición
        val clean = if (raw.startsWith("569")) raw.substring(3) 
                   else if (raw.startsWith("+569")) raw.substring(4)
                   else raw
        mutableStateOf(clean)
    }

    var fechaNacimientoInput by remember(user.fechaNacimiento) {
        mutableStateOf(formatToInput(user.fechaNacimiento))
    }

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        SectionTitle(title = "Información Personal")
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombres") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = apellidos, onValueChange = { apellidos = it }, label = { Text("Apellidos") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = user.email, onValueChange = {}, label = { Text("Correo Electrónico") }, modifier = Modifier.fillMaxWidth(), enabled = false)
        Spacer(modifier = Modifier.height(16.dp))
        
        // --- CAMPO TELÉFONO ACTUALIZADO ---
        OutlinedTextField(
            value = telefonoInput,
            onValueChange = { newText ->
                // Solo dígitos y máximo 8 (ej: 91234567)
                if (newText.length <= 8 && newText.all { it.isDigit() }) {
                    telefonoInput = newText
                }
            },
            label = { Text("Teléfono") },
            placeholder = { Text("12345678") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            visualTransformation = ChilePhoneVisualTransformation(), // Visual: +56 9 ...
            singleLine = true
        )
        // ----------------------------------
        
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = fechaNacimientoInput,
            onValueChange = { newText ->
                if (newText.length <= 8 && newText.all { it.isDigit() }) {
                    fechaNacimientoInput = newText
                }
            },
            label = { Text("Fecha de Nacimiento") },
            placeholder = { Text("DD-MM-YYYY") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            visualTransformation = DateVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

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

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = newsletter, onCheckedChange = { newsletter = it })
            Text("Deseo recibir novedades y ofertas", modifier = Modifier.padding(start = 8.dp))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = notificaciones, onCheckedChange = { notificaciones = it })
            Text("Activar notificaciones de eventos", modifier = Modifier.padding(start = 8.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))
        AppButton(
            onClick = {
                var dateToSave: String? = null
                if (fechaNacimientoInput.isNotBlank()) {
                    if (fechaNacimientoInput.length != 8) {
                        Toast.makeText(context, "Fecha inválida, usa 8 dígitos.", Toast.LENGTH_SHORT).show()
                        return@AppButton
                    }
                    try {
                        val birthDate = LocalDate.parse(fechaNacimientoInput, inputFormatter)
                        val today = LocalDate.now()
                        val age = Period.between(birthDate, today).years
                        if (age < 18) {
                            Toast.makeText(context, "Debes ser mayor de 18 años.", Toast.LENGTH_SHORT).show()
                            return@AppButton
                        }
                        dateToSave = birthDate.format(outputFormatter)
                    } catch (e: DateTimeException) {
                        Toast.makeText(context, "Fecha inválida (ej: día 32).", Toast.LENGTH_SHORT).show()
                        return@AppButton
                    }
                }
                
                // Formatear teléfono para guardar: Agregamos +569 si no está vacío
                val telefonoToSend = if (telefonoInput.isNotBlank()) "+569$telefonoInput" else null

                val updatedUser = user.copy(
                    nombre = nombre,
                    apellidos = apellidos,
                    telefono = telefonoToSend,
                    fechaNacimiento = dateToSave,
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
