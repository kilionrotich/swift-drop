package com.kilion.swiftdrop.auth.login.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kilion.swiftdrop.auth.login.network.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun register(name: String, phone: String, email: String, password: String, role: String) {
        viewModelScope.launch {
            _uiState.value = RegisterUiState.Loading
            try {
                val registerRequest = RegisterRequest(name, phone, email, password, role)
                val response = authService.register(registerRequest)

                if (response.isSuccessful && response.body() != null) {
                    val registerResponse = response.body()!!
                    _uiState.value = RegisterUiState.Success(registerResponse.token)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    _uiState.value = RegisterUiState.Error("Registration failed: $errorBody")
                }
            } catch (e: Exception) {
                _uiState.value = RegisterUiState.Error("An exception occurred: ${e.message}")
            }
        }
    }
}