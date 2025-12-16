package com.example.levelupstore_app.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val id: Long? = null,
    val name: String? = null,
    val rating: Int,
    val text: String? = null,
    val date: String? = null,
    val user: User? = null
)
