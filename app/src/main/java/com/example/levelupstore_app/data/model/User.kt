// Ruta: com/example/levelupstore_app/data/model/User.kt
@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)
package com.example.levelupstore_app.data.model

import kotlinx.serialization.Serializable

/**
 * Define la estructura de un usuario, coincidiendo con 'usuarios.json'.
 *
 * Los campos con '?' y '= null' o '= false' son opcionales.
 * Esto nos permite manejar tanto los usuarios completos de 'usuarios.json'
 * como los usuarios nuevos del registro, que solo tienen algunos campos.
 */
@Serializable
data class User(
    val email: String,
    val clave: String,
    val nombre: String,
    val edad: Int? = null, // Tu 'registro.html' pedía edad
    val apellidos: String? = null,
    val telefono: String? = null,
    val fechaNacimiento: String? = null,
    val genero: String? = null,
    val categoriaFavorita: String? = null,
    val plataformaPrincipal: String? = null,
    val gamerTag: String? = null,
    val bio: String? = null,
    val newsletter: Boolean? = false,
    val notificaciones: Boolean? = false,
    val isAdmin: Boolean = false
)