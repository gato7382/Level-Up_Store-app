package com.example.levelupstore_app.data.repository

import android.util.Log
import com.example.levelupstore_app.data.model.Product
import com.example.levelupstore_app.data.network.RetrofitClient
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ProductRepository {

    suspend fun getProducts(): List<Product> {
        return try {
            RetrofitClient.instance.getProducts()
        } catch (e: Exception) {
            Log.e("ProductRepo", "Error fetching products", e)
            emptyList()
        }
    }

    suspend fun getProductById(productId: Long): Product? {
        return try {
            RetrofitClient.instance.getProductById(productId)
        } catch (e: Exception) {
            Log.e("ProductRepo", "Error fetching product $productId", e)
            null
        }
    }

    suspend fun addProduct(product: Product, imageFile: File): Result<Product> {
        return try {
            val productJson = Gson().toJson(product)
            val productBody = productJson.toRequestBody("application/json".toMediaTypeOrNull())

            val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

            val createdProduct = RetrofitClient.instance.createProduct(productBody, imagePart)
            Result.success(createdProduct)
        } catch (e: Exception) {
            Log.e("ProductRepo", "Error creating product", e)
            Result.failure(e)
        }
    }

    suspend fun updateProduct(product: Product, imageFile: File?): Result<Product> {
        return try {
            val productJson = Gson().toJson(product)
            val productBody = productJson.toRequestBody("application/json".toMediaTypeOrNull())

            val imagePart = if (imageFile != null) {
                val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
            } else {
                null
            }

            // Llamada a la API que ahora acepta imagePart como nullable
            val updatedProduct = RetrofitClient.instance.updateProduct(product.id!!, productBody, imagePart)
            Result.success(updatedProduct)
        } catch (e: Exception) {
            Log.e("ProductRepo", "Error updating product", e)
            Result.failure(e)
        }
    }

    suspend fun deleteProduct(productId: Long): Result<Unit> {
        return try {
            RetrofitClient.instance.deleteProduct(productId)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ProductRepo", "Error deleting product", e)
            Result.failure(e)
        }
    }
}
