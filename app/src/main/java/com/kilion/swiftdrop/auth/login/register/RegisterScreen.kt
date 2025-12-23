package com.kilion.swiftdrop.auth.login.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.auth0.android.jwt.JWT
import com.kilion.swiftdrop.ui.components.PrimaryButton
import com.kilion.swiftdrop.ui.components.SwiftDropTextField
import com.kilion.swiftdrop.utils.ValidationUtils

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("Customer") } // Default role
    var termsAccepted by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Register", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(24.dp))

            SwiftDropTextField(
                value = name,
                onValueChange = { name = it },
                label = "Full Name"
            )
            Spacer(modifier = Modifier.height(16.dp))

            SwiftDropTextField(
                value = phone,
                onValueChange = { phone = it },
                label = "Phone Number"
            )
            Spacer(modifier = Modifier.height(16.dp))

            SwiftDropTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email Address"
            )
            Spacer(modifier = Modifier.height(16.dp))

            SwiftDropTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                isPassword = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            SwiftDropTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirm Password",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Role selection
            Text("Register as:", style = MaterialTheme.typography.bodyLarge)
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = role == "Customer",
                    onClick = { role = "Customer" }
                )
                Text("Customer")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = role == "Rider",
                    onClick = { role = "Rider" }
                )
                Text("Rider")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = role == "Shop",
                    onClick = { role = "Shop" }
                )
                Text("Shop")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = termsAccepted, onCheckedChange = { termsAccepted = it })
                Text("I agree to the Terms and Conditions")
            }

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(text = "Register") {
                errorMessage = ""
                when {
                    !ValidationUtils.isNotEmpty(name, phone, email, password, confirmPassword) ->
                        errorMessage = "All fields are required"

                    !ValidationUtils.isValidPhone(phone) ->
                        errorMessage = "Phone must start with 07 and be 10 digits"

                    !ValidationUtils.isValidEmail(email) ->
                        errorMessage = "Please enter a valid email address"

                    !ValidationUtils.isValidPassword(password) ->
                        errorMessage = "Password must be at least 6 characters"

                    password != confirmPassword ->
                        errorMessage = "Passwords do not match"

                    !termsAccepted ->
                        errorMessage = "You must accept the Terms and Conditions"

                    else -> {
                        viewModel.register(name, phone, email, password, role)
                    }
                }
            }

            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate("login") }) {
                Text("Already have an account? Login")
            }

            val currentUiState = uiState
            if (currentUiState is RegisterUiState.Error) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = currentUiState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        val currentUiState = uiState
        if (currentUiState is RegisterUiState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        if (currentUiState is RegisterUiState.Success) {
            val jwt = JWT(currentUiState.token)
            val userRole = jwt.getClaim("role").asString()

            LaunchedEffect(Unit) {
                when (userRole) {
                    "Rider" -> navController.navigate("rider_graph")
                    "Shop" -> navController.navigate("shop_graph")
                    "Customer" -> navController.navigate("customerHome")
                    else -> navController.navigate("login") // Default fallback
                }
            }
        }
    }
}