package com.example.levelupstore_app.ui.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class DateVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
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

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 4) return offset + 1
                if (offset <= 8) return offset + 2
                return 10
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset - 1
                if (offset <= 10) return offset - 2
                return 8
            }
        }

        return TransformedText(
            text = AnnotatedString(formattedText),
            offsetMapping = offsetMapping
        )
    }
}