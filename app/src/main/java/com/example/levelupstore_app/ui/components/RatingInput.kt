package com.example.levelupstore_app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun RatingInput(
    modifier: Modifier = Modifier,
    rating: Int,
    onRatingChange: (Int) -> Unit
) {
    val StarColor = Color(0xFFFFD700)

    Row(modifier = modifier) {
        (1..5).forEach { index ->
            Icon(
                imageVector = if (index <= rating) {
                    Icons.Filled.Star
                } else {
                    Icons.Outlined.StarOutline
                },
                contentDescription = "Rating $index",
                tint = StarColor,
                modifier = Modifier.clickable {
                    onRatingChange(index)
                }
            )
        }
    }
}