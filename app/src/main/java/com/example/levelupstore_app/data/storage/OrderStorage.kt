@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)
package com.example.levelupstore_app.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.levelupstore_app.data.model.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

class OrderStorage(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("order_prefs")
        val KEY_ORDERS_MAP = stringPreferencesKey("orders_map_json")
    }

    val ordersMapFlow: Flow<Map<String, List<Order>>> = context.dataStore.data.map { preferences ->
        preferences[KEY_ORDERS_MAP]?.let { jsonString ->
            try {
                Json.decodeFromString<Map<String, List<Order>>>(jsonString)
            } catch (e: Exception) {
                emptyMap()
            }
        } ?: emptyMap()
    }

    suspend fun saveOrdersMap(ordersMap: Map<String, List<Order>>) {
        context.dataStore.edit { preferences ->
            preferences[KEY_ORDERS_MAP] = Json.encodeToString(ordersMap)
        }
    }
}