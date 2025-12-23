package com.kilion.swiftdrop.dashboard.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.jwt.JWT
import com.kilion.swiftdrop.data.local.UserPreferences
import com.kilion.swiftdrop.data.model.Address
import com.kilion.swiftdrop.data.model.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {

    val theme: StateFlow<String?> = userPreferences.theme
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val orderStatusUpdates: StateFlow<Boolean?> = userPreferences.orderStatusUpdates
        .stateIn(viewModelScope, SharingStarted.Lazily, true) // Default to true

    val promotions: StateFlow<Boolean?> = userPreferences.promotions
        .stateIn(viewModelScope, SharingStarted.Lazily, true) // Default to true

    val addresses: StateFlow<List<Address>> = userPreferences.addresses
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val userProfile: StateFlow<UserProfile?> = userPreferences.authToken.map { token ->
        token?.let {
            val jwt = JWT(it)
            val name = jwt.getClaim("name").asString() ?: "N/A"
            val phone = jwt.getClaim("phone").asString() ?: "N/A"
            val role = jwt.getClaim("role").asString() ?: "N/A"
            UserProfile(name, phone, role)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun saveTheme(theme: String) {
        viewModelScope.launch {
            userPreferences.saveTheme(theme)
        }
    }

    fun saveOrderStatusUpdates(enabled: Boolean) {
        viewModelScope.launch {
            userPreferences.saveOrderStatusUpdates(enabled)
        }
    }

    fun savePromotions(enabled: Boolean) {
        viewModelScope.launch {
            userPreferences.savePromotions(enabled)
        }
    }

    fun addAddress(street: String, city: String, state: String, zipCode: String) {
        viewModelScope.launch {
            val currentAddresses = userPreferences.addresses.first().toMutableList()
            val newAddress = Address(UUID.randomUUID().toString(), street, city, state, zipCode)
            currentAddresses.add(newAddress)
            userPreferences.saveAddresses(currentAddresses)
        }
    }

    fun updateAddress(address: Address) {
        viewModelScope.launch {
            val currentAddresses = userPreferences.addresses.first().toMutableList()
            val index = currentAddresses.indexOfFirst { it.id == address.id }
            if (index != -1) {
                currentAddresses[index] = address
                userPreferences.saveAddresses(currentAddresses)
            }
        }
    }

    fun deleteAddress(address: Address) {
        viewModelScope.launch {
            val currentAddresses = userPreferences.addresses.first().toMutableList()
            currentAddresses.remove(address)
            userPreferences.saveAddresses(currentAddresses)
        }
    }

    fun setDefaultAddress(address: Address) {
        viewModelScope.launch {
            val currentAddresses = userPreferences.addresses.first().map {
                it.copy(isDefault = it.id == address.id)
            }
            userPreferences.saveAddresses(currentAddresses)
        }
    }
}