package com.kilion.swiftdrop.dashboard.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kilion.swiftdrop.data.model.Item
import com.kilion.swiftdrop.data.model.Order
import com.kilion.swiftdrop.data.model.UserProfile
import com.kilion.swiftdrop.data.repository.ShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val shopRepository: ShopRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ShopUiState>(ShopUiState.Idle)
    val uiState: StateFlow<ShopUiState> = _uiState

    fun loadItems() {
        viewModelScope.launch {
            _uiState.value = ShopUiState.Loading
            try {
                _uiState.value = ShopUiState.SuccessItems(shopRepository.getShopItems())
            } catch (e: Exception) {
                _uiState.value = ShopUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun loadAnalytics() {
        viewModelScope.launch {
            _uiState.value = ShopUiState.Loading
            try {
                _uiState.value = ShopUiState.AnalyticsLoaded(shopRepository.getAnalytics())
            } catch (e: Exception) {
                _uiState.value = ShopUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun addItem(item: Item) {
        viewModelScope.launch {
            _uiState.value = ShopUiState.Loading
            try {
                val newItem = shopRepository.addItem(item)
                val currentItems = (_uiState.value as? ShopUiState.SuccessItems)?.items ?: emptyList()
                _uiState.value = ShopUiState.SuccessItems(currentItems + newItem)
            } catch (e: Exception) {
                _uiState.value = ShopUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun editItem(item: Item) {
        viewModelScope.launch {
            _uiState.value = ShopUiState.Loading
            try {
                val updatedItem = shopRepository.editItem(item)
                val currentItems = (_uiState.value as? ShopUiState.SuccessItems)?.items?.map {
                    if (it.id == updatedItem.id) updatedItem else it
                } ?: emptyList()
                _uiState.value = ShopUiState.SuccessItems(currentItems)
            } catch (e: Exception) {
                _uiState.value = ShopUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun deleteItem(itemId: String) {
        viewModelScope.launch {
            // Optimistically update the UI
            val currentItems = (_uiState.value as? ShopUiState.SuccessItems)?.items ?: return@launch
            _uiState.value = ShopUiState.SuccessItems(currentItems.filter { it.id != itemId })

            try {
                shopRepository.deleteItem(itemId)
            } catch (e: Exception) {
                // Revert if the deletion failed
                _uiState.value = ShopUiState.Error("Failed to delete item: ${e.message}")
                _uiState.value = ShopUiState.SuccessItems(currentItems) // Revert to original list
            }
        }
    }

    fun getOrders() {
        viewModelScope.launch {
            _uiState.value = ShopUiState.Loading
            try {
                _uiState.value = ShopUiState.SuccessOrders(shopRepository.getOrders())
            } catch (e: Exception) {
                _uiState.value = ShopUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun loadShopProfile() {
        viewModelScope.launch {
            _uiState.value = ShopUiState.Loading
            try {
                _uiState.value = ShopUiState.ShopProfileLoaded(shopRepository.getShopProfile())
            } catch (e: Exception) {
                _uiState.value = ShopUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}