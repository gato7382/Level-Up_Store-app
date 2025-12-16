package com.example.levelupstore_app.ui.features.common

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupstore_app.ui.components.AppButton
import com.example.levelupstore_app.ui.components.CartItemRow
import com.example.levelupstore_app.ui.features.cart.CartViewModel
import com.example.levelupstore_app.ui.utils.AppViewModelFactory
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

// Formateador de moneda
private val chileLocale = Locale("es", "CL")
private val currencyFormatter = NumberFormat.getCurrencyInstance(chileLocale)

/**
 * Organismo que muestra el contenido del carrito en un panel deslizable.
 * Reemplaza .cart-dropdown
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartDropdown(
    cartViewModel: CartViewModel = viewModel(factory = AppViewModelFactory)
) {
    // Observa el estado de los datos del carrito
    val cartDataState by cartViewModel.cartDataState.collectAsState()
    // Observa el estado de si el panel está abierto
    val isCartOpen by cartViewModel.isCartOpen.collectAsState()

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // 1. Lógica para manejar el estado del panel
    if (isCartOpen) {
        ModalBottomSheet(
            onDismissRequest = { cartViewModel.toggleCart() }, // Cierra al tocar fuera
            sheetState = sheetState
        ) {
            // 2. Contenido del panel
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Título
                Text(
                    text = "Mi Carrito 🛒",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // 3. Lógica de Carrito Vacío o Lleno
                if (cartDataState.items.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Tu carrito está vacío")
                    }
                } else {
                    // 4. Lista de Items (Moléculas)
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f) // Ocupa el espacio disponible
                    ) {
                        items(cartDataState.items) { item ->
                            CartItemRow(
                                item = item,
                                // CORRECCIÓN: Agregamos '!!' porque el ID es necesario y asumimos que existe
                                onIncrease = { cartViewModel.updateQuantity(item.product.id!!, item.quantity + 1) },
                                onDecrease = { cartViewModel.updateQuantity(item.product.id!!, item.quantity - 1) },
                                onRemove = { cartViewModel.removeFromCart(item.product.id!!) }
                            )
                            Divider()
                        }
                    }

                    // 5. Total y Descuento
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total:",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = currencyFormatter.format(cartDataState.totalPrice),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    if (cartDataState.discountApplied) {
                        Text(
                            text = "¡Descuento Duoc aplicado!",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }

                    // 6. Botón de Checkout
                    Spacer(modifier = Modifier.height(16.dp))
                    AppButton(
                        onClick = {
                            scope.launch {
                                val message = cartViewModel.checkout() // Llama a la función suspend
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                            }
                        },
                        text = "🚀 Proceder al Checkout"
                    )
                }
            }
        }
    }
}