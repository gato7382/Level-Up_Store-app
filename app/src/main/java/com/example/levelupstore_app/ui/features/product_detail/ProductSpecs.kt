// Ruta: com/example.levelupstore_app/ui/features/product_detail/ProductSpecs.kt
package com.example.levelupstore_app.ui.features.product_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.levelupstore_app.data.model.Spec
import com.example.levelupstore_app.ui.components.SectionTitle
import com.example.levelupstore_app.ui.components.SpecItem

/**
 * Organismo que muestra la lista de especificaciones del producto.
 * Reemplaza .product-specs
 *
 * @param specs La lista de objetos 'Spec' del producto.
 */
@Composable
fun ProductSpecs(specs: List<Spec>) {

    // Si no hay especificaciones, no muestra nada.
    if (specs.isEmpty()) {
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        // 1. Título de la Sección
        SectionTitle(title = "Especificaciones Técnicas")

        // 2. Lista de Moléculas 'SpecItem'
        Column(modifier = Modifier.padding(top = 16.dp)) {
            // Itera sobre la lista 'specs'
            specs.forEach { spec ->
                // Renderiza una molécula por cada item
                SpecItem(name = spec.name, value = spec.value)
            }
        }
    }
}