package com.kilion.swiftdrop.data.model

data class OrderRequest(
    val itemIds: List<String>,
    val deliveryAddress: String
)