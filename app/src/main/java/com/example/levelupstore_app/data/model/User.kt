@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)
package com.example.levelupstore_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String,
    val clave: String,
    val nombre: String,
    val edad: Int? = null,
    val apellidos: String? = null,
    val telefono: String? = null,
    val fechaNacimiento: String? = null,
    val genero: String? = null,
    val categoriaFavorita: String? = null,
    val plataformaPrincipal: String? = null,
    val gamerTag: String? = null,
    val bio: String? = null,
    val newsletter: Boolean? = false,
    val notificaciones: Boolean? = false
)