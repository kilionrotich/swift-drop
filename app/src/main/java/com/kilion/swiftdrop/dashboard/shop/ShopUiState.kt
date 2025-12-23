package com.kilion.swiftdrop.dashboard.shop

import com.kilion.swiftdrop.data.model.Item
import com.kilion.swiftdrop.data.model.Order
import com.kilion.swiftdrop.data.model.ShopAnalytics
import com.kilion.swiftdrop.data.model.UserProfile

sealed class ShopUiState {
    object Idle : ShopUiState()
    object Loading : ShopUiState()
    data class SuccessItems(val items: List<Item>) : ShopUiState()
    data class SuccessOrders(val orders: List<Order>) : ShopUiState()
    data class ShopProfileLoaded(val profile: UserProfile) : ShopUiState() // Renamed from ProfileLoaded
    data class AnalyticsLoaded(val analytics: ShopAnalytics) : ShopUiState()
    data class QRReady(val qrCodeUrl: String, val label: String) : ShopUiState()
    data class Error(val message: String) : ShopUiState()
}
