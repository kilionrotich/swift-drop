package com.kilion.swiftdrop.data.remote

import com.kilion.swiftdrop.data.model.Item
import com.kilion.swiftdrop.data.model.Order
import com.kilion.swiftdrop.data.model.ShopAnalytics
import com.kilion.swiftdrop.data.model.UserProfile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ShopService {
    @GET("shop/items")
    suspend fun getShopItems(): Response<List<Item>>

    @POST("shop/items")
    suspend fun addItem(@Body item: Item): Response<Item>

    @PUT("shop/items/{id}")
    suspend fun editItem(@Path("id") itemId: String, @Body item: Item): Response<Item>

    @DELETE("shop/items/{id}")
    suspend fun deleteItem(@Path("id") itemId: String): Response<Unit>

    @GET("shop/analytics")
    suspend fun getAnalytics(): Response<ShopAnalytics>

    @GET("shop/orders")
    suspend fun getOrders(): Response<List<Order>>

    @GET("shop/profile")
    suspend fun getShopProfile(): Response<UserProfile>
}