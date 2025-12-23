package com.kilion.swiftdrop.auth.login.register

// Request body for registration
data class RegisterRequest(
    val name: String,
    val phone: String,
    val email: String,
    val password: String,
    val role: String
)

// Response body for registration
data class RegisterResponse(
    val token: String
)

// UI State for the registration screen
sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    data class Success(val token: String) : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}
