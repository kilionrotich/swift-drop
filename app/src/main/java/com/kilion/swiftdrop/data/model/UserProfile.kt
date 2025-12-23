package com.kilion.swiftdrop.data.model

data class UserProfile(
    val name: String,
    val phone: String,
    val role: String,
    val address: String? = null,
    val operatingHours: String? = null
)
