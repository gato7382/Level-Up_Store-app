package com.example.levelupstore_app.data.network

import com.example.levelupstore_app.data.model.Order
import com.example.levelupstore_app.data.model.Product
import com.example.levelupstore_app.data.model.Review
import com.example.levelupstore_app.data.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // --- PRODUCTOS ---
    @GET("/products")
    suspend fun getProducts(): List<Product>

    @GET("/products/{id}")
    suspend fun getProductById(@Path("id") id: String): Product

    @POST("/products")
    suspend fun createProduct(@Body product: Product): Product

    // Actualizar producto existente
    @retrofit2.http.PUT("products/{id}")
    suspend fun updateProduct(@retrofit2.http.Path("id") id: String, @retrofit2.http.Body product: Product): Product

    // Eliminar producto
    @retrofit2.http.DELETE("products/{id}")
    suspend fun deleteProduct(@retrofit2.http.Path("id") id: String)

    // --- USUARIOS ---
    @POST("/auth/login")
    suspend fun login(@Body credentials: Map<String, String>): User

    @POST("/auth/register") // (Si tu backend lo soporta)
    suspend fun register(@Body user: User): User

    // --- PEDIDOS (ORDERS) ---
    // 1. Enviar un pedido nuevo (Checkout)
    @POST("/orders")
    suspend fun createOrder(@Body order: Order): Order

    // 2. Obtener pedidos de un usuario (Filtrando por email)
    // Ejemplo: GET /orders?userEmail=juan@gmail.com
    @GET("/orders")
    suspend fun getOrdersByUser(@Query("userEmail") email: String): List<Order>

    // --- RESEÑAS (REVIEWS) ---
    // 1. Obtener reseñas de un producto
    // Ejemplo: GET /products/ps5/reviews (Depende de tu backend, o GET /reviews?productId=ps5)
    // Asumiremos que tu backend permite filtrar reseñas por producto:
    @GET("/reviews")
    suspend fun getReviewsByProduct(@Query("productId") productId: String): List<Review>

    // 2. Crear una reseña
    @POST("/reviews")
    suspend fun createReview(@Body review: Review): Review
}