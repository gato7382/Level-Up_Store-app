// Ruta: com/example/levelupstore_app/ui/features/profile/OrderHistoryScreen.kt
package com.example.levelupstore_app.ui.features.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.levelupstore_app.R
import com.example.levelupstore_app.data.model.Order
import com.example.levelupstore_app.ui.components.SectionTitle
import java.text.NumberFormat
import java.util.Locale

// Formateador de moneda
private val chileLocale = Locale("es", "CL")
private val currencyFormatter = NumberFormat.getCurrencyInstance(chileLocale)

@Composable
fun OrderHistoryScreen(
    orders: List<Order> // Recibe la lista de pedidos del ViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SectionTitle(title = "Mis Pedidos")

        Spacer(modifier = Modifier.height(16.dp))

        if (orders.isEmpty()) {
            Text(
                text = "Aún no has realizado ningún pedido.",
                modifier = Modifier.padding(vertical = 32.dp)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(orders.sortedByDescending { it.date }) { order -> // Ordena por fecha
                    OrderCard(order = order)
                }
            }
        }
    }
}

// Molécula interna para mostrar UNA tarjeta de pedido
@Composable
private fun OrderCard(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Column {
            // Cabecera del Pedido (Fecha y Total)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Pedido: ${order.date}", // Muestra la fecha
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = currencyFormatter.format(order.total),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Divider()

            // Lista de Items en el Pedido
            Column(modifier = Modifier.padding(16.dp)) {
                order.items.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = "file:///android_asset/${item.product.images.firstOrNull()?.removePrefix("/")}",
                                placeholder = painterResource(id = R.drawable.logo_level_up)
                            ),
                            contentDescription = item.product.name,
                            modifier = Modifier.size(40.dp),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "${item.quantity}x ${item.product.name}")
                    }
                }
            }
        }
    }
}