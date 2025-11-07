// Ruta: com/example/levelupstore_app/ui/features/product_detail/ProductInfo.kt
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

// --- Formateador de Moneda (Eficiente) ---
// (Podemos mover esto a un archivo 'utils' si se repite mucho)
private val chileLocale = Locale("es", "CL")
private val currencyFormatter = NumberFormat.getCurrencyInstance(chileLocale)
// --- Fin del Formateador ---

/**
 * Organismo que muestra la información principal del producto y el botón de compra.
 * Reemplaza .product-info
 *
 * @param product El producto a mostrar.
 * @param onAddToCartClick La lambda (función) a ejecutar cuando se presiona el botón.
 */
@Composable
fun ProductInfo(
    product: Product,
    onAddToCartClick: () -> Unit // Recibimos la función desde el ViewModel
) {
    // --- Definición de colores ---
    val GreenGlow = Color(0xFF39FF14) // Color verde de tu CSS
    val NeonBlue = Color(0xFF1E90FF) // Color azul de tu CSS
    val TextGray = Color(0xFFD3D3D3) // Color de .product-description
    // --- Fin de definición de colores ---

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre cada elemento
    ) {
        // 1. Título del Producto (el <h1>)
        Text(
            text = product.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = NeonBlue
            // fontFamily = OrbitronFontFamily // (Si la configuras)
        )

        // 2. Precio del Producto (el .product-price)
        Text(
            text = currencyFormatter.format(product.price),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = GreenGlow
            // fontFamily = OrbitronFontFamily
        )

        // 3. Descripción (el .product-description)
        Text(
            text = product.description,
            style = MaterialTheme.typography.bodyLarge,
            color = TextGray,
            lineHeight = 24.sp // (equivalente a line-height: 1.6)
        )

        Spacer(modifier = Modifier.height(8.dp)) // Espacio extra

        // 4. Botón "Añadir al Carrito" (el .btn-add-cart)
        AppButton(
            onClick = onAddToCartClick, // Llama a la función del ViewModel
            text = "🛒 Agregar al Carrito"
        )
    }
}