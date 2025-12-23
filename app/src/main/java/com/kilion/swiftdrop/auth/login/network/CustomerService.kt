package com.kilion.swiftdrop.auth.login.network

import com.kilion.swiftdrop.data.model.Item
import com.kilion.swiftdrop.data.model.Order
import com.kilion.swiftdrop.data.model.Review
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CustomerService {
    @GET("shop/items")
    suspend fun getAllItems(): Response<List<Item>>

    @POST("orders/place")
    suspend fun placeOrder(@Body orderRequest: OrderRequest): Response<Order>

    @GET("orders/my-orders")
    suspend fun getMyOrders(): Response<List<Order>>

    @POST("reviews")
    suspend fun submitReview(@Body reviewRequest: ReviewRequestBody): Response<Review>

    @GET("reviews/item/{itemId}")
    suspend fun getReviewsForItem(@Path("itemId") itemId: String): Response<List<Review>>
}

data class OrderRequest(
    val items: List<String>,
    val dropoffLocation: Location
)

data class Location(
    val address: String
)

data class ReviewRequestBody(
    val orderId: String,
    val itemId: String,
    val rating: Int,
    val comment: String
)