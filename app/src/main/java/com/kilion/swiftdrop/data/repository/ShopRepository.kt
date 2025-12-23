package com.kilion.swiftdrop.data.repository

import com.kilion.swiftdrop.data.model.Item
import com.kilion.swiftdrop.data.model.Order
import com.kilion.swiftdrop.data.model.ShopAnalytics
import com.kilion.swiftdrop.data.model.UserProfile
import com.kilion.swiftdrop.data.remote.ShopService
import javax.inject.Inject

class ShopRepository @Inject constructor(
    private val shopService: ShopService
) {
    suspend fun getShopItems(): List<Item> {
        return shopService.getShopItems().body() ?: emptyList()
    }

    suspend fun addItem(item: Item): Item {
        val response = shopService.addItem(item)
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Failed to add item: ${response.code()}")
        }
    }

    suspend fun editItem(item: Item): Item {
        val response = shopService.editItem(item.id, item)
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Failed to edit item: ${response.code()}")
        }
    }

    suspend fun deleteItem(itemId: String) {
        shopService.deleteItem(itemId)
    }

    suspend fun getAnalytics(): ShopAnalytics {
        val response = shopService.getAnalytics()
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Failed to fetch analytics: ${response.code()}")
        }
    }

    suspend fun getOrders(): List<Order> {
        val response = shopService.getOrders()
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Failed to fetch orders: ${response.code()}")
        }
    }

    suspend fun getShopProfile(): UserProfile {
        val response = shopService.getShopProfile()
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Failed to fetch shop profile: ${response.code()}")
        }
    }
}