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

private val chileLocale = Locale("es", "CL")
private val currencyFormatter = NumberFormat.getCurrencyInstance(chileLocale)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartDropdown(
    cartViewModel: CartViewModel = viewModel(factory = AppViewModelFactory)
) {
    val cartDataState by cartViewModel.cartDataState.collectAsState()
    val isCartOpen by cartViewModel.isCartOpen.collectAsState()

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    if (isCartOpen) {
        ModalBottomSheet(
            onDismissRequest = { cartViewModel.toggleCart() },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Mi Carrito 🛒",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

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
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(cartDataState.items) { item ->
                            CartItemRow(
                                item = item,
                                onIncrease = { cartViewModel.updateQuantity(item.product.id, item.quantity + 1) },
                                onDecrease = { cartViewModel.updateQuantity(item.product.id, item.quantity - 1) },
                                onRemove = { cartViewModel.removeFromCart(item.product.id) }
                            )
                            Divider()
                        }
                    }

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

                    Spacer(modifier = Modifier.height(16.dp))
                    AppButton(
                        onClick = {
                            scope.launch {
                                val message = cartViewModel.checkout()
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