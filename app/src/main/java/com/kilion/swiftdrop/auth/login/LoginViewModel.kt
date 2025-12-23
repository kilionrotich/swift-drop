package com.kilion.swiftdrop.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kilion.swiftdrop.auth.login.network.AuthService
import com.kilion.swiftdrop.auth.login.network.LoginRequest
import com.kilion.swiftdrop.data.local.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(phone: String, password: String) {
        login(phone, password, "rider")
    }

    fun login(phone: String, password: String, role: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                val loginRequest = LoginRequest(phone, password)
                val response = authService.login(role, loginRequest)

                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    userPreferences.saveAuthToken(loginResponse.token)
                    _uiState.value = LoginUiState.Success(loginResponse.token)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    _uiState.value = LoginUiState.Error("Login failed: $errorBody")
                }
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error("An exception occurred: ${e.message}")
            }
        }
    }
}