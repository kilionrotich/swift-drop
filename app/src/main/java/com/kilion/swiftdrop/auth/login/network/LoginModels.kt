package com.kilion.swiftdrop.auth.login.network

import com.google.gson.annotations.SerializedName

// Request payload sent to backend
data class LoginRequest(
    val phone: String,
    val password: String
)

// Response payload returned by backend
data class LoginResponse(
    @SerializedName("token")
    val token: String
)
