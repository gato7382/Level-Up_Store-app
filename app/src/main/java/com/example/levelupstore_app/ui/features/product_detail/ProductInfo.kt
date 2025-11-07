package com.example.levelupstore_app.ui.features.product_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelupstore_app.data.model.Product
import com.example.levelupstore_app.ui.components.AppButton
import java.text.NumberFormat
import java.util.Locale

private val chileLocale = Locale("es", "CL")
private val currencyFormatter = NumberFormat.getCurrencyInstance(chileLocale)

@Composable
fun ProductInfo(
    product: Product,
    onAddToCartClick: () -> Unit
) {
    val GreenGlow = Color(0xFF39FF14)
    val NeonBlue = Color(0xFF1E90FF)
    val TextGray = Color(0xFFD3D3D3)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = product.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = NeonBlue
        )

        Text(
            text = currencyFormatter.format(product.price),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = GreenGlow
        )

        Text(
            text = product.description,
            style = MaterialTheme.typography.bodyLarge,
            color = TextGray,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        AppButton(
            onClick = onAddToCartClick,
            text = "🛒 Agregar al Carrito"
        )
    }
}