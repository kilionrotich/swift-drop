package com.kilion.swiftdrop.data.model

data class ShopAnalytics(
    val totalRevenue: Double,
    val totalOrders: Int,
    val topSellingItems: List<Item>
)