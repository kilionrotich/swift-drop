package com.kilion.swiftdrop.dashboard.customer

import com.kilion.swiftdrop.data.model.Item
import com.kilion.swiftdrop.data.model.Order
import com.kilion.swiftdrop.data.model.Review

sealed class CustomerUiState {
    object Idle : CustomerUiState()
    object Loading : CustomerUiState()
    data class Success(val items: List<Item>) : CustomerUiState()
    data class OrderPlaced(val orderId: String) : CustomerUiState()
    data class OrdersLoaded(val orders: List<Order>) : CustomerUiState()
    object ReviewSubmitted : CustomerUiState()
    data class ReviewsLoaded(val reviews: List<Review>) : CustomerUiState()
    data class Error(val message: String) : CustomerUiState()
}
