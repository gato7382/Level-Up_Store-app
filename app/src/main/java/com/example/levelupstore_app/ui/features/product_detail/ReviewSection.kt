package com.example.levelupstore_app.ui.features.product_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.levelupstore_app.data.model.Review
import com.example.levelupstore_app.ui.components.SectionTitle

@Composable
fun ReviewSection(
    navController: NavController,
    reviews: List<Review>,
    isLoggedIn: Boolean,
    onReviewSubmit: (text: String, rating: Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        SectionTitle(title = "Reseñas de Clientes (${reviews.size})")

        ReviewForm(
            navController = navController,
            isLoggedIn = isLoggedIn,
            onReviewSubmit = onReviewSubmit
        )

        ReviewList(reviews = reviews)
    }
}