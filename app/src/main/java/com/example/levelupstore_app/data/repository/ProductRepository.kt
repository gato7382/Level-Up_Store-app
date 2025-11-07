// Ruta: com/example/levelupstore_app/data/repository/ProductRepository.kt
package com.example.levelupstore_app.data.repository

import android.content.Context
import com.example.levelupstore_app.data.model.Product
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import java.io.IOException

/**
 * Este repositorio es el único que sabe cómo obtener la lista de productos.
 * En este caso, sabe leer el archivo 'products.json' de la carpeta 'assets'.
 *
 * @param context El "Contexto" de Android, necesario para acceder a la carpeta 'assets'.
 */
class ProductRepository(private val context: Context) {

    // Creamos una instancia de Json para poder "decodificar" (parsear) el texto
    private val json = Json {
        ignoreUnknownKeys = true // Ignora campos en el JSON que no estén en nuestra data class
    }

    /**
     * Carga y decodifica la lista de productos desde 'products.json'.
     *
     * @return Una lista de objetos 'Product'.
     */
    fun getProducts(): List<Product> {
        return try {
            // 1. Pedirle al 'context' de Android que abra el archivo de 'assets'
            val jsonString = context.assets.open("products.json")
                .bufferedReader() // Lo abre como un lector de texto
                .use { it.readText() } // Lee todo el contenido del archivo en un String

            // 2. Usar la librería de serialización para convertir el String en una Lista de Productos
            json.decodeFromString<List<Product>>(jsonString)

        } catch (e: IOException) {
            // Si el archivo no se encuentra o no se puede leer, imprime el error
            e.printStackTrace()
            // Y devuelve una lista vacía para que la app no se rompa
            emptyList()
        } catch (e: Exception) {
            // Captura cualquier otro error (ej. JSON mal formado)
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Busca un solo producto por su ID.
     *
     * @param productId El ID del producto a buscar (ej: "ps5")
     * @return Un objeto 'Product' si lo encuentra, o 'null' si no.
     */
    fun getProductById(productId: String): Product? {
        // Llama a nuestra otra función para obtener todos los productos
        // y luego usa la función de Kotlin 'find' para buscar el que coincida.
        return getProducts().find { it.id == productId }
    }
}