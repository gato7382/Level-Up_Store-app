package com.example.levelupstore_app.ui.features.product_detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.levelupstore_app.ui.components.AppButton
import com.example.levelupstore_app.ui.components.RatingInput

@Composable
fun ReviewForm(
    navController: NavController,
    isLoggedIn: Boolean,
    onReviewSubmit: (text: String, rating: Int) -> Unit
) {
    var rating by remember { mutableStateOf(0) }
    var text by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
        if (isLoggedIn) {
            Text(
                text = "Escribe tu reseña",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            RatingInput(
                rating = rating,
                onRatingChange = { newRating -> rating = newRating }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Comparte tu experiencia...") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppButton(
                onClick = {
                    if (text.isNotBlank() && rating > 0) {
                        onReviewSubmit(text, rating)
                        text = ""
                        rating = 0
                    }
                },
                text = "Enviar Reseña"
            )

        } else {
            Text("Debes iniciar sesión para dejar una reseña.")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { navController.navigate("login") }) {
                Text("Ir a Iniciar Sesión")
            }
        }
    }
}