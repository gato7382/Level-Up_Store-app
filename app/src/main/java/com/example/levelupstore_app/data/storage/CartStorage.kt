@file:OptIn(kotlinx.serialization.InternalSerializationApi::class) // Tu Opt-In
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

/**
 * Gestiona el almacenamiento persistente del carrito de compras.
 * (Nuestro "localStorage" para 'levelUpGamerCart')
 */
class CartStorage(private val context: Context) {

    // Archivo "cart_prefs"
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("cart_prefs")
        val KEY_CART_ITEMS = stringPreferencesKey("cart_items_json")
    }

    /** Emite la lista de items cada vez que cambia. */
    val cartItemsFlow: Flow<List<CartItem>> = context.dataStore.data.map { preferences ->
        preferences[KEY_CART_ITEMS]?.let { jsonString ->
            try {
                // Forma moderna: Pasa el tipo directamente
                Json.decodeFromString<List<CartItem>>(jsonString)
            } catch (e: Exception) {
                emptyList()
            }
        } ?: emptyList()
    }

    /** Guarda la lista completa de items */
    suspend fun saveCartItems(items: List<CartItem>) {
        context.dataStore.edit { preferences ->
            // Forma moderna: Pasa el objeto directamente
            preferences[KEY_CART_ITEMS] = Json.encodeToString(items)
        }
    }
}