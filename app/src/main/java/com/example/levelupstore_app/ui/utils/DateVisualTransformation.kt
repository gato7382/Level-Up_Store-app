// Ruta: com/example/levelupstore_app/ui/utils/DateVisualTransformation.kt
package com.example.levelupstore_app.ui.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * Añade guiones automáticamente a un texto de fecha: DD-MM-YYYY.
 * El usuario escribe "27101995" y ve "27-10-1995".
 */
class DateVisualTransformation : VisualTransformation {

    // --- LÓGICA DE FILTRO CORREGIDA ---
    override fun filter(text: AnnotatedString): TransformedText {
        // 'text' es lo que el usuario ha escrito (solo números, ej: "271019")
        val inputText = text.text

        val formattedText = buildString {
            if (inputText.length >= 1) {
                append(inputText.substring(0, minOf(inputText.length, 2)))
            }
            if (inputText.length >= 3) {
                append('-')
                append(inputText.substring(2, minOf(inputText.length, 4)))
            }
            if (inputText.length >= 5) {
                append('-')
                append(inputText.substring(4, minOf(inputText.length, 8)))
            }
        }

        // --- Mapeo de Cursor Corregido ---
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // offset es la posición del cursor en el texto original (sin guiones)
                if (offset <= 2) return offset
                if (offset <= 4) return offset + 1 // Suma 1 por el primer guion
                if (offset <= 8) return offset + 2 // Suma 2 por ambos guiones
                return 10 // Longitud máxima (DD-MM-YYYY)
            }

            override fun transformedToOriginal(offset: Int): Int {
                // offset es la posición del cursor en el texto formateado (con guiones)
                if (offset <= 2) return offset
                if (offset <= 5) return offset - 1 // Resta 1 por el primer guion
                if (offset <= 10) return offset - 2 // Resta 2 por ambos guiones
                return 8 // Máximo original
            }
        }

        return TransformedText(
            text = AnnotatedString(formattedText),
            offsetMapping = offsetMapping
        )
    }
}