package com.kilion.swiftdrop.dashboard.rider

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.jwt.JWT
import com.kilion.swiftdrop.auth.login.network.RiderService
import com.kilion.swiftdrop.data.local.UserPreferences
import com.kilion.swiftdrop.data.model.UserProfile
import com.kilion.swiftdrop.data.repository.RiderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RiderViewModel @Inject constructor(
    private val riderRepository: RiderRepository,
    private val riderService: RiderService,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow<RiderUiState>(RiderUiState.Idle)
    val uiState: StateFlow<RiderUiState> = _uiState.asStateFlow()

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    fun loadUserProfile() {
        viewModelScope.launch {
            userPreferences.authToken.firstOrNull()?.let {
                val jwt = JWT(it)
                val name = jwt.getClaim("name").asString() ?: "N/A"
                val phone = jwt.getClaim("phone").asString() ?: "N/A"
                val role = jwt.getClaim("role").asString() ?: "N/A"
                _userProfile.value = UserProfile(name, phone, role)
            }
        }
    }

    fun getDeliveryHistory() {
        viewModelScope.launch {
            _uiState.value = RiderUiState.Loading
            try {
                val response = riderService.getDeliveryHistory()
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = RiderUiState.HistorySuccess(response.body()!!.filterNotNull())
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    _uiState.value = RiderUiState.Error("Failed to load delivery history: $errorBody")
                    Log.e("RiderViewModel", "Failed to load delivery history: ${response.code()} - $errorBody")
                }
            } catch (e: Exception) {
                _uiState.value = RiderUiState.Error("Error fetching delivery history: ${e.message}")
                Log.e("RiderViewModel", "Error fetching delivery history", e)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPreferences.clearAuthToken()
        }
    }

    fun loadAcceptedOrders() {
        viewModelScope.launch {
            _uiState.value = RiderUiState.Loading
            try {
                val orders = riderRepository.getAcceptedOrders()
                _uiState.value = RiderUiState.Success(orders)
            } catch (e: Exception) {
                val errorMessage = "Failed to load accepted orders: ${e.message}"
                _uiState.value = RiderUiState.Error(errorMessage)
                Log.e("RiderViewModel", errorMessage, e)
            }
        }
    }

    fun pickupOrder(orderId: String) {
        viewModelScope.launch {
            _uiState.value = RiderUiState.Loading
            try {
                riderRepository.pickupOrder(orderId)
                loadAcceptedOrders()
            } catch (e: Exception) {
                val errorMessage = "Failed to pick up order: ${e.message}"
                _uiState.value = RiderUiState.Error(errorMessage)
                Log.e("RiderViewModel", errorMessage, e)
            }
        }
    }

    fun getAssignedOrders() {
        viewModelScope.launch {
            _uiState.value = RiderUiState.Loading
            try {
                val response = riderService.getAssignedOrders()
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = RiderUiState.Success(response.body()!!.filterNotNull())
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    _uiState.value = RiderUiState.Error("Failed to load assigned orders: $errorBody")
                    Log.e("RiderViewModel", "Failed to load assigned orders: ${response.code()} - $errorBody")
                }
            } catch (e: Exception) {
                _uiState.value = RiderUiState.Error("Error fetching orders: ${e.message}")
                Log.e("RiderViewModel", "Error fetching orders", e)
            }
        }
    }

    fun deliverOrder(orderId: String) {
        viewModelScope.launch {
            try {
                val response = riderService.deliverOrder(orderId)
                if (response.isSuccessful) {
                    getAssignedOrders()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    _uiState.value = RiderUiState.Error("Failed to deliver order: $errorBody")
                    Log.e("RiderViewModel", "Failed to deliver order: ${response.code()} - $errorBody")
                }
            } catch (e: Exception) {
                _uiState.value = RiderUiState.Error("Error delivering order: ${e.message}")
                Log.e("RiderViewModel", "Error delivering order", e)
            }
        }
    }

    fun validateOrder(orderId: String) {
        viewModelScope.launch {
            _uiState.value = RiderUiState.Loading
            try {
                val response = riderService.validateOrder(orderId)
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = RiderUiState.Validated(response.body()!!)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    _uiState.value = RiderUiState.Error("Order validation failed: $errorBody")
                    Log.e("RiderViewModel", "Order validation failed: ${response.code()} - $errorBody")
                }
            } catch (e: Exception) {
                _uiState.value = RiderUiState.Error("Error: ${e.message}")
                Log.e("RiderViewModel", "Error validating order", e)
            }
        }
    }

    fun acceptOrder(orderId: String) {
        viewModelScope.launch {
            _uiState.value = RiderUiState.Loading
            try {
                val order = riderRepository.acceptOrder(orderId)
                _uiState.value = RiderUiState.OrderAccepted(order)
            } catch (e: Exception) {
                val errorMessage = "Failed to accept order: ${e.message}"
                _uiState.value = RiderUiState.Error(errorMessage)
                Log.e("RiderViewModel", errorMessage, e)
            }
        }
    }
}