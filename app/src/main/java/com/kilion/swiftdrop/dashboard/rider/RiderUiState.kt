package com.kilion.swiftdrop.dashboard.rider

import com.kilion.swiftdrop.data.model.Order

sealed class RiderUiState {
    object Idle : RiderUiState()
    object Loading : RiderUiState()
    data class Success(val orders: List<Order>) : RiderUiState()
    data class HistorySuccess(val orders: List<Order>) : RiderUiState()
    data class Error(val message: String) : RiderUiState()
    data class OrderAccepted(val order: Order) : RiderUiState()
    data class Validated(val order: Order) : RiderUiState()
}