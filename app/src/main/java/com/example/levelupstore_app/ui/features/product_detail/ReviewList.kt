// Ruta: com/example/levelupstore_app/ui/features/product_detail/ReviewList.kt
package com.example.levelupstore_app.ui.features.product_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.levelupstore_app.data.model.Review
import com.example.levelupstore_app.ui.components.ReviewItem

@Composable
fun ReviewList(reviews: List<Review>) {
    Column(
        modifier = Modifier.padding(top = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre reseñas
    ) {
        if (reviews.isEmpty()) {
            Text(
                text = "No hay reseñas todavía. ¡Sé el primero en opinar!",
                modifier = Modifier.padding(vertical = 32.dp)
            )
        } else {
            // Itera sobre la lista y muestra una molécula ReviewItem por cada una
            reviews.forEach { review ->
                ReviewItem(review = review)
            }
        }
    }
}