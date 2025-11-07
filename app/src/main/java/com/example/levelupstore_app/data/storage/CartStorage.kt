@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)
package com.example.levelupstore_app.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.levelupstore_app.data.model.CartItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

class CartStorage(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("cart_prefs")
        val KEY_CART_ITEMS = stringPreferencesKey("cart_items_json")
    }

    val cartItemsFlow: Flow<List<CartItem>> = context.dataStore.data.map { preferences ->
        preferences[KEY_CART_ITEMS]?.let { jsonString ->
            try {
                Json.decodeFromString<List<CartItem>>(jsonString)
            } catch (e: Exception) {
                emptyList()
            }
        } ?: emptyList()
    }

    suspend fun saveCartItems(items: List<CartItem>) {
        context.dataStore.edit { preferences ->
            preferences[KEY_CART_ITEMS] = Json.encodeToString(items)
        }
    }
}