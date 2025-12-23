package com.kilion.swiftdrop.auth.login.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// The DataStore instance is created as an extension on Context.
private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

/**
 * Manages the storage and retrieval of the authentication token using Jetpack DataStore.
 * This class is marked as a Singleton to ensure there's only one instance throughout the app.
 *
 * @param context The application context, provided by Hilt.
 */
@Singleton // ✅ Ensures only one instance of TokenManager exists.
class TokenManager @Inject constructor(
    // ✅ Use @ApplicationContext to tell Hilt to provide the application's context here.
    @ApplicationContext private val context: Context
) {

    companion object {
        // Defines the key for storing the token in DataStore.
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
    }

    /**
     * Saves the authentication token to DataStore.
     */
    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
        }
    }

    /**
     * Retrieves the authentication token from DataStore as a Flow.
     * Returns a Flow that emits the token string, or null if it doesn't exist.
     */
    fun getToken(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[TOKEN_KEY]
        }
    }

    /**
     * Removes the authentication token from DataStore.
     */
    suspend fun clearToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
        }
    }
}
