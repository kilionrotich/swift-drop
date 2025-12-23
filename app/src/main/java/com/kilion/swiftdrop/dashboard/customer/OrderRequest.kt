package com.kilion.swiftdrop.dashboard.customer

data class OrderRequest(
    val itemIds: List<String>,
    val deliveryAddress: String
)