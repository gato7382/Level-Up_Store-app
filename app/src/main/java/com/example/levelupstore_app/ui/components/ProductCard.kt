package com.example.levelupstore_app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.levelupstore_app.R
import com.example.levelupstore_app.data.model.Product
import java.text.NumberFormat
import java.util.Locale

private val chileLocale = Locale("es", "CL")
private val currencyFormatter = NumberFormat.getCurrencyInstance(chileLocale)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(
    product: Product,
    onCardClick: () -> Unit,
    onAddToCartClick: () -> Unit
) {
    val GreenGlow = Color(0xFF39FF14)
    val NeonBlue = Color(0xFF1E90FF)
    val CardBackground = Color(0xFF141414)
    val CardBorder = Color(0xFF1E90FF).copy(alpha = 0.3f)

    Card(
        onClick = onCardClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Column {
            // IMAGEN REMOTA CORRECTA
            Image(
                painter = rememberAsyncImagePainter(
                    model = product.imageUrl, // Usa la URL del backend
                    placeholder = painterResource(id = R.drawable.logo_level_up),
                    error = painterResource(id = R.drawable.logo_level_up) // Muestra logo si falla
                ),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = NeonBlue,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = currencyFormatter.format(product.price ?: 0.0), // Manejo seguro de nulos
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                    color = GreenGlow
                )
            }

            AppButton(
                onClick = onAddToCartClick,
                text = "Agregar al Carrito",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}
