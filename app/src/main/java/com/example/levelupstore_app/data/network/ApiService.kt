package com.example.levelupstore_app.data.network

import com.example.levelupstore_app.data.model.LoginResponse
import com.example.levelupstore_app.data.model.Order
import com.example.levelupstore_app.data.model.Product
import com.example.levelupstore_app.data.model.Review
import com.example.levelupstore_app.data.model.ReviewRequest
import com.example.levelupstore_app.data.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // --- AUTH ---
    @POST("/auth/login")
    suspend fun login(@Body credentials: Map<String, String>): LoginResponse

    @POST("/auth/register")
    suspend fun register(@Body user: User): Map<String, String>

    @GET("/auth/profile")
    suspend fun getProfile(): User

    @PUT("/auth/profile")
    suspend fun updateProfile(@Body user: User): User

    // --- PRODUCTS ---
    @GET("/api/products")
    suspend fun getProducts(): List<Product>

    @GET("/api/products/{id}")
    suspend fun getProductById(@Path("id") id: Long): Product

    @Multipart
    @POST("/api/products")
    suspend fun createProduct(
        @Part("product") product: RequestBody,
        @Part image: MultipartBody.Part
    ): Product

    @Multipart
    @PUT("/api/products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Long,
        @Part("product") product: RequestBody,
        @Part image: MultipartBody.Part? = null // <-- ¡AHORA ES NULLABLE!
    ): Product

    @DELETE("/api/products/{id}")
    suspend fun deleteProduct(@Path("id") id: Long): Response<Unit>

    // --- REVIEWS ---
    @GET("/reviews")
    suspend fun getReviewsByProduct(@Query("productoId") productId: Long): List<Review>

    @POST("/reviews")
    suspend fun createReview(
        @Query("productoId") productId: Long,
        @Body review: ReviewRequest
    ): Review

    // --- ORDERS ---
    @GET("/orders")
    suspend fun getOrders(): List<Order>

    @POST("/orders")
    suspend fun createOrder(@Body order: Order): Order
}
