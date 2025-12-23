package com.kilion.swiftdrop.data.repository

import com.kilion.swiftdrop.auth.login.network.AuthService
import com.kilion.swiftdrop.auth.login.network.FCMTokenRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val authService: AuthService
) {
    suspend fun saveFCMToken(token: String) {
        val request = FCMTokenRequest(fcmToken = token)
        authService.saveFCMToken(request)
    }
}