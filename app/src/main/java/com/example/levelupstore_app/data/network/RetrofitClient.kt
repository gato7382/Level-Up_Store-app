package com.example.levelupstore_app.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://44.213.211.202:9090"
    
    private var authToken: String? = null

    fun setAuthToken(token: String?) {
        authToken = token
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor { authToken })
        .build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
