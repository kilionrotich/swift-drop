package com.kilion.swiftdrop.data.repository

import com.kilion.swiftdrop.auth.login.network.RiderService
import com.kilion.swiftdrop.data.model.Order
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RiderRepository @Inject constructor(
    private val riderService: RiderService
) {
    suspend fun getAcceptedOrders(): List<Order> {
        val response = riderService.getAcceptedOrders()
        if (response.isSuccessful) {
            return response.body()?.filterNotNull() ?: emptyList()
        } else {
            throw Exception("Failed to fetch accepted orders: ${response.code()}")
        }
    }

    suspend fun pickupOrder(orderId: String): Order {
        val response = riderService.pickupOrder(orderId)
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Failed to pick up order: ${response.code()}")
        }
    }

    suspend fun acceptOrder(orderId: String): Order {
        val response = riderService.acceptOrder(orderId)
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Failed to accept order: ${response.code()}")
        }
    }
}