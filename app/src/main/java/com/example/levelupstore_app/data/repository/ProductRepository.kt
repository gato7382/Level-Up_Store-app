package com.example.levelupstore_app.data.repository

import android.content.Context
import android.util.Log
import com.example.levelupstore_app.data.model.Product
import com.example.levelupstore_app.data.network.RetrofitClient

class ProductRepository(private val context: Context) {

    /**
     * Obtiene la lista de productos desde el servidor (Backend).
     */
    suspend fun getProducts(): List<Product> {
        return try {
            // Llamada a la red
            val response = RetrofitClient.instance.getProducts()
            Log.d("API_TEST", "Éxito: ${response.size} productos cargados")
            response
        } catch (e: Exception) {
            Log.e("API_TEST", "Error al conectar: ${e.message}")
            e.printStackTrace()
            emptyList() // Retorna lista vacía si falla (modo offline básico)
        }
    }

    /**
     * Obtiene un producto específico por ID desde el servidor.
     */
    suspend fun getProductById(productId: String): Product? {
        return try {
            RetrofitClient.instance.getProductById(productId)
        } catch (e: Exception) {
            Log.e("API_TEST", "Error al buscar producto $productId: ${e.message}")
            null
        }
    }

    /**
     * Sube un nuevo producto al servidor (Para AdminScreen).
     */
    suspend fun addProduct(product: Product): Boolean {
        return try {
            RetrofitClient.instance.createProduct(product)
            true
        } catch (e: Exception) {
            Log.e("API_TEST", "Error al crear producto: ${e.message}")
            false
        }
    }

    suspend fun updateProduct(product: Product): Boolean {
        return try {
            RetrofitClient.instance.updateProduct(product.id, product)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteProduct(productId: String): Boolean {
        return try {
            RetrofitClient.instance.deleteProduct(productId)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}