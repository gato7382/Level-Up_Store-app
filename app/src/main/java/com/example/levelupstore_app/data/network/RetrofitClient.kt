package com.example.levelupstore_app.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // --- CONFIGURACIÓN DE URL ---
    // Para el emulador de Android, 'localhost' de tu PC es '10.0.2.2'.
    // Si usas un dispositivo físico, necesitas la IP local de tu PC (ej. 192.168.1.X).
    // Asumo que tu backend corre en el puerto 3000 (típico de Node/JsonServer).
    private const val BASE_URL = "http://10.0.2.2:3000/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // <-- Usamos Gson como pediste
            .build()
            .create(ApiService::class.java)
    }
}