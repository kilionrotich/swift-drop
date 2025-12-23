package com.kilion.swiftdrop.auth.login.network

import com.kilion.swiftdrop.auth.login.register.RegisterRequest
import com.kilion.swiftdrop.auth.login.register.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthService {
    @POST("auth/login/{role}")
    suspend fun login(@Path("role") role: String, @Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("auth/fcm-token")
    suspend fun saveFCMToken(@Body tokenRequest: FCMTokenRequest): Response<Unit>
}

data class FCMTokenRequest(val fcmToken: String)
