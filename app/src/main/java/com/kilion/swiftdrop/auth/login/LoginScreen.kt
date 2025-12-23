package com.kilion.swiftdrop.auth.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.auth0.android.jwt.JWT
import com.kilion.swiftdrop.ui.components.PrimaryButton
import com.kilion.swiftdrop.ui.components.SwiftDropTextField
import com.kilion.swiftdrop.utils.ValidationUtils

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = hiltViewModel()) {
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
        ) {

            Text(text = "Login", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(24.dp))

            SwiftDropTextField(
                value = phone,
                onValueChange = { phone = it },
                label = "Phone"
            )

            Spacer(modifier = Modifier.height(16.dp))

            SwiftDropTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                isPassword = true,
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(
                            imageVector = if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { navController.navigate("forgot_password") }) {
                Text("Forgot Password?")
            }

            Spacer(modifier = Modifier.height(16.dp))

            PrimaryButton(
                text = "Login",
                onClick = {
                    errorMessage = ""
                    when {
                        !ValidationUtils.isNotEmpty(phone, password) ->
                            errorMessage = "All fields are required"
                        !ValidationUtils.isValidPassword(password) ->
                            errorMessage = "Password must be at least 6 characters"
                        else -> {
                            viewModel.login(phone, password)
                        }
                    }
                }
            )

            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }


            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate("register") }) {
                Text("Don't have an account? Register")
            }

            val currentUiState = uiState
            if (currentUiState is LoginUiState.Error) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = currentUiState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        when (val currentUiState = uiState) {
            is LoginUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is LoginUiState.Success -> {
                // This LaunchedEffect will trigger navigation when the state becomes Success
                LaunchedEffect(currentUiState) {
                    val jwt = JWT(currentUiState.token)
                    val role = jwt.getClaim("role").asString()
                    
                    val destination = when (role) {
                        "Rider" -> "rider_graph"
                        "Shop" -> "shop_graph"
                        "Customer" -> "customer_graph" // Corrected to use the graph route
                        else -> "login" // Fallback
                    }

                    if (destination != "login") {
                        navController.navigate(destination) {
                            // This clears the entire back stack to prevent going back to login
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    }
                }
            }
            else -> {}
        }
    }
}