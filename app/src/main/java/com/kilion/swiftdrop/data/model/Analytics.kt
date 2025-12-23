package com.kilion.swiftdrop.data.model

data class Analytics(
    val totalRevenue: Double,
    val totalOrders: Int,
    val topSellingItems: List<TopSellingItem>
)

data class TopSellingItem(
    val name: String,
    val count: Int
)
