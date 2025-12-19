package com.example.levelupstore_app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.levelupstore_app.R
import com.example.levelupstore_app.data.model.CartItem
import java.text.NumberFormat
import java.util.Locale

// Formateador de moneda
private val chileLocale = Locale("es", "CL")
private val currencyFormatter = NumberFormat.getCurrencyInstance(chileLocale)

/**
 * Molécula que muestra un solo ítem dentro del dropdown/sheet del carrito.
 */
@Composable
fun CartItemRow(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen
        Image(
            painter = rememberAsyncImagePainter(
                model = item.product.imageUrl,
                placeholder = painterResource(id = R.drawable.logo_level_up)
            ),
            contentDescription = item.product.name,
            modifier = Modifier
                .size(60.dp)
                .padding(end = 8.dp),
            contentScale = ContentScale.Crop
        )

        // Nombre y Precio
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.product.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                // CORRECCIÓN AQUÍ: Usamos (item.product.price ?: 0.0) para evitar el error de nulo
                text = currencyFormatter.format((item.product.price ?: 0.0) * item.quantity),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Botones de Cantidad
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onDecrease, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Remove, contentDescription = "Quitar uno")
            }
            Text(
                text = "${item.quantity}",
                modifier = Modifier.padding(horizontal = 8.dp),
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onIncrease, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Add, contentDescription = "Añadir uno")
            }
        }

        // Botón de Eliminar
        IconButton(onClick = onRemove, modifier = Modifier.padding(start = 8.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Eliminar del carrito")
        }
    }
}