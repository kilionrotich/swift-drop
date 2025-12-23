package com.kilion.swiftdrop.dashboard.customer

import com.kilion.swiftdrop.auth.login.network.OrderRequest
import com.kilion.swiftdrop.auth.login.network.ReviewRequestBody
import com.kilion.swiftdrop.data.model.Item
import com.kilion.swiftdrop.data.model.Order
import com.kilion.swiftdrop.data.model.Review
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CustomerService {
    @GET("items")
    suspend fun getAllItems(): Response<List<Item>>

    @POST("orders")
    suspend fun placeOrder(@Body orderRequest: OrderRequest): Response<Order>

    @GET("orders")
    suspend fun getMyOrders(): Response<List<Order>>

    @POST("reviews")
    suspend fun submitReview(@Body reviewRequest: ReviewRequestBody): Response<Review>

    @GET("items/{id}/reviews")
    suspend fun getReviewsForItem(@Path("id") itemId: String): Response<List<Review>>
}
