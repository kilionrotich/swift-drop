package com.kilion.swiftdrop.data.repository

import com.kilion.swiftdrop.auth.login.network.CustomerService
import com.kilion.swiftdrop.auth.login.network.Location
import com.kilion.swiftdrop.auth.login.network.OrderRequest
import com.kilion.swiftdrop.auth.login.network.ReviewRequestBody
import com.kilion.swiftdrop.data.model.Item
import com.kilion.swiftdrop.data.model.Order
import com.kilion.swiftdrop.data.model.Review
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerRepository @Inject constructor(
    private val customerService: CustomerService
) {
    suspend fun getAllItems(): List<Item> {
        val response = customerService.getAllItems()
        if (response.isSuccessful) {
            return response.body()?.filterNotNull() ?: emptyList()
        } else {
            throw Exception("Failed to fetch items: ${response.code()}")
        }
    }

    suspend fun placeOrder(items: List<Item>, dropoffAddress: String): Order {
        val itemIds = items.map { it.id }
        val orderRequest = OrderRequest(
            items = itemIds,
            dropoffLocation = Location(address = dropoffAddress)
        )
        val response = customerService.placeOrder(orderRequest)
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Failed to place order: ${response.code()}")
        }
    }

    suspend fun getMyOrders(): List<Order> {
        val response = customerService.getMyOrders()
        if (response.isSuccessful) {
            return response.body()?.filterNotNull() ?: emptyList()
        } else {
            throw Exception("Failed to fetch orders: ${response.code()}")
        }
    }

    suspend fun submitReview(orderId: String, itemId: String, rating: Int, comment: String): Review {
        val requestBody = ReviewRequestBody(orderId, itemId, rating, comment)
        val response = customerService.submitReview(requestBody)
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Failed to submit review: ${response.code()}")
        }
    }

    suspend fun getReviewsForItem(itemId: String): List<Review> {
        val response = customerService.getReviewsForItem(itemId)
        if (response.isSuccessful) {
            return response.body()?.filterNotNull() ?: emptyList()
        } else {
            throw Exception("Failed to fetch reviews: ${response.code()}")
        }
    }
}