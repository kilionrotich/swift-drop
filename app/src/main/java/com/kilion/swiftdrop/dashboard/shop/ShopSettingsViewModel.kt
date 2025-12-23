package com.kilion.swiftdrop.dashboard.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.jwt.JWT
import com.kilion.swiftdrop.data.local.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopSettingsViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {

    val username: StateFlow<String?> = userPreferences.authToken.map { token ->
        token?.let { 
            val jwt = JWT(it)
            jwt.getClaim("name").asString()
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun updateUsername(newUsername: String) {
        viewModelScope.launch {
            // TODO: Implement username update logic
        }
    }
}