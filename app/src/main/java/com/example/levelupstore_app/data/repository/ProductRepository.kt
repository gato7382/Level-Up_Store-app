package com.example.levelupstore_app.data.repository

import android.content.Context
import com.example.levelupstore_app.data.model.Product
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import java.io.IOException

class ProductRepository(private val context: Context) {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    fun getProducts(): List<Product> {
        return try {
            val jsonString = context.assets.open("products.json")
                .bufferedReader()
                .use { it.readText() }

            json.decodeFromString<List<Product>>(jsonString)

        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun getProductById(productId: String): Product? {
        return getProducts().find { it.id == productId }
    }
}