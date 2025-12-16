package com.example.levelupstore_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long? = null,
    val email: String,
    val nombre: String,
    val apellidos: String? = null,
    val telefono: String? = null,
    val fechaNacimiento: String? = null,
    val genero: String? = null,
    val categoriaFavorita: String? = null,
    val plataformaPrincipal: String? = null,
    val gamerTag: String? = null,
    val bio: String? = null,
    val newsletter: Boolean? = null,
    val notificaciones: Boolean? = null,
    val admin: Boolean = false,
    val clave: String? = null
)
