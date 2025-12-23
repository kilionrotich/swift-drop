package com.kilion.swiftdrop.auth.login.network

import com.kilion.swiftdrop.data.model.Analytics
import com.kilion.swiftdrop.data.model.Item
import com.kilion.swiftdrop.data.model.Order
import com.kilion.swiftdrop.data.model.ShopProfile
import retrofit2.Response
import retrofit2.http.*

interface ShopService {

    // ✅ Fetch all shop orders
    @GET("shop/orders")
    suspend fun getOrders(): Response<List<Order>>

    // ✅ Accept a specific order
    @POST("shop/orders/{id}/accept")
    suspend fun acceptOrder(@Path("id") orderId: String): Response<Unit>

    // ✅ Add a new item
    @POST("shop/items")
    suspend fun addItem(@Body item: Item): Response<Item>

    // ✅ Fetch all shop items
    @GET("shop/items")
    suspend fun getShopItems(): Response<List<Item>>

    // ✅ Update an existing item
    @PUT("shop/items/{id}")
    suspend fun updateItem(@Path("id") itemId: String, @Body item: Item): Response<Item>

    // ✅ Delete an item
    @DELETE("shop/items/{id}")
    suspend fun deleteItem(@Path("id") itemId: String): Response<Unit>

    // Profile Routes
    @GET("shop/profile")
    suspend fun getShopProfile(): Response<ShopProfile>

    @PUT("shop/profile")
    suspend fun updateShopProfile(@Body profile: ShopProfileRequestBody): Response<ShopProfile>

    // Analytics
    @GET("shop/analytics")
    suspend fun getAnalytics(): Response<Analytics>
}

data class ShopProfileRequestBody(
    val name: String,
    val address: String,
    val operatingHours: String
)