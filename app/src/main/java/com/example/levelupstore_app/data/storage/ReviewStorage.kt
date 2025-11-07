@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)
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

class ReviewStorage(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("review_prefs")
        val KEY_REVIEWS_MAP = stringPreferencesKey("reviews_map_json")
    }

    val reviewsMapFlow: Flow<Map<String, List<Review>>> = context.dataStore.data.map { preferences ->
        preferences[KEY_REVIEWS_MAP]?.let { jsonString ->
            try {
                Json.decodeFromString<Map<String, List<Review>>>(jsonString)
            } catch (e: Exception) {
                emptyMap()
            }
        } ?: emptyMap()
    }

    suspend fun saveReviewsMap(reviewsMap: Map<String, List<Review>>) {
        context.dataStore.edit { preferences ->
            preferences[KEY_REVIEWS_MAP] = Json.encodeToString(reviewsMap)
        }
    }
}