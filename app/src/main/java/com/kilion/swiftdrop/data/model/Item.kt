package com.kilion.swiftdrop.data.model

import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("_id")
    val id: String,
    val name: String?,
    @SerializedName("value")
    val price: Double = 0.0,
    val description: String?,
    val imageUrl: String? = null,
    val salesCount: Int = 0
)