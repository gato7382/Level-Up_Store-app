@file:OptIn(kotlinx.serialization.InternalSerializationApi::class) // Tu Opt-In
package com.example.levelupstore_app.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.levelupstore_app.data.model.Review
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

/**
 * Gestiona el almacenamiento persistente de las reseñas.
 * Guardaremos un "Mapa" donde la clave es el ID del producto (ej: "ps5")
 * y el valor es la Lista de Reseñas de ESE producto.
 */
class ReviewStorage(private val context: Context) {

    // Archivo "review_prefs"
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("review_prefs")
        val KEY_REVIEWS_MAP = stringPreferencesKey("reviews_map_json")
    }

    /** Emite el Mapa completo de reseñas cada vez que cambia. */
    val reviewsMapFlow: Flow<Map<String, List<Review>>> = context.dataStore.data.map { preferences ->
        preferences[KEY_REVIEWS_MAP]?.let { jsonString ->
            try {
                // Forma moderna: Pasa el tipo directamente
                Json.decodeFromString<Map<String, List<Review>>>(jsonString)
            } catch (e: Exception) {
                emptyMap()
            }
        } ?: emptyMap()
    }

    /** Guarda el Mapa completo de reseñas. */
    suspend fun saveReviewsMap(reviewsMap: Map<String, List<Review>>) {
        context.dataStore.edit { preferences ->
            // Forma moderna: Pasa el objeto directamente
            preferences[KEY_REVIEWS_MAP] = Json.encodeToString(reviewsMap)
        }
    }
}