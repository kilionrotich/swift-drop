package com.kilion.swiftdrop.auth.login.network

import com.kilion.swiftdrop.data.model.Order
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RiderService {

    // Get all orders with 'Accepted' status, ready for pickup
    @GET("orders/accepted")
    suspend fun getAcceptedOrders(): Response<List<Order>>

    // Rider claims an order for pickup
    @POST("orders/{id}/pickup")
    suspend fun pickupOrder(@Path("id") orderId: String): Response<Order>

    // Get all orders assigned to this rider
    @GET("rider/orders/assigned")
    suspend fun getAssignedOrders(): Response<List<Order>>

    // Get all past orders for the rider
    @GET("riders/orders/history")
    suspend fun getDeliveryHistory(): Response<List<Order>>

    // Mark an order as delivered
    @POST("orders/{id}/deliver")
    suspend fun deliverOrder(@Path("id") orderId: String): Response<Order>

    // Validate order after QR scan (if you still need this)
    @POST("rider/orders/{id}/validate")
    suspend fun validateOrder(@Path("id") orderId: String): Response<Order>

    // Update rider’s current location
    @POST("rider/location/update")
    suspend fun updateLocation(@Body location: RiderLocationRequest): Response<Unit>

    // Rider accepts an order
    @POST("orders/{id}/accept")
    suspend fun acceptOrder(@Path("id") orderId: String): Response<Order>
}

data class RiderLocationRequest(
    val riderId: String,
    val latitude: Double,
    val longitude: Double
)