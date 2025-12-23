package com.kilion.swiftdrop.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.kilion.swiftdrop.data.model.Address
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    private val gson = Gson()

    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit {
            it[KEY_AUTH_TOKEN] = token
        }
    }

    val authToken: Flow<String?>
        get() = context.dataStore.data.map {
            it[KEY_AUTH_TOKEN]
        }

    suspend fun clearAuthToken() {
        context.dataStore.edit {
            it.remove(KEY_AUTH_TOKEN) // Clear only the auth token
        }
    }

    suspend fun saveTheme(theme: String) {
        context.dataStore.edit {
            it[KEY_THEME] = theme
        }
    }

    val theme: Flow<String?>
        get() = context.dataStore.data.map {
            it[KEY_THEME]
        }

    suspend fun saveOrderStatusUpdates(enabled: Boolean) {
        context.dataStore.edit {
            it[KEY_ORDER_STATUS_UPDATES] = enabled
        }
    }

    val orderStatusUpdates: Flow<Boolean?>
        get() = context.dataStore.data.map {
            it[KEY_ORDER_STATUS_UPDATES]
        }

    suspend fun savePromotions(enabled: Boolean) {
        context.dataStore.edit {
            it[KEY_PROMOTIONS] = enabled
        }
    }

    val promotions: Flow<Boolean?>
        get() = context.dataStore.data.map {
            it[KEY_PROMOTIONS]
        }

    suspend fun saveAddresses(addresses: List<Address>) {
        val addressSet = addresses.map { gson.toJson(it) }.toSet()
        context.dataStore.edit {
            it[KEY_ADDRESSES] = addressSet
        }
    }

    val addresses: Flow<List<Address>>
        get() = context.dataStore.data.map { preferences ->
            preferences[KEY_ADDRESSES]?.map { gson.fromJson(it, Address::class.java) } ?: emptyList()
        }

    companion object {
        private val KEY_AUTH_TOKEN = stringPreferencesKey("auth_token")
        private val KEY_THEME = stringPreferencesKey("theme")
        private val KEY_ORDER_STATUS_UPDATES = booleanPreferencesKey("order_status_updates")
        private val KEY_PROMOTIONS = booleanPreferencesKey("promotions")
        private val KEY_ADDRESSES = stringSetPreferencesKey("addresses")
    }
}