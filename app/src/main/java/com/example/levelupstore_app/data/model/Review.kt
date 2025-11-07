@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)
package com.example.levelupstore_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val name: String,
    val rating: Int,
    val text: String,
    val date: String
)