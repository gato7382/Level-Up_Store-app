// Ruta: com/example/levelupstore_app/ui/components/CartIcon.kt
package com.example.levelupstore_app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class) // Necesario para Badge
@Composable
fun CartIcon(
    totalItems: Int,
    onIconClick: () -> Unit
) {
    // --- Definición de colores ---
    val NeonBlue = Color(0xFF1E90FF)
    val BadgeColor = Color(0xFFFF3914) // Rojo/Naranja de tu .cart-count
    // --- Fin de definición de colores ---

    IconButton(onClick = onIconClick) {
        BadgedBox(
            badge = {
                if (totalItems > 0) {
                    Badge(
                        containerColor = BadgeColor,
                        contentColor = Color.White
                    ) {
                        Text(text = "$totalItems")
                    }
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Carrito de Compras",
                tint = NeonBlue
            )
        }
    }
}