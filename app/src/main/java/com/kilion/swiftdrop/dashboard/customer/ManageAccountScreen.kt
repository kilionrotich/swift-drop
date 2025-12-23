package com.kilion.swiftdrop.dashboard.customer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageAccountScreen(navController: NavController, viewModel: SettingsViewModel = hiltViewModel()) {
    val userProfile by viewModel.userProfile.collectAsState()

    var username by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }

    LaunchedEffect(userProfile) {
        userProfile?.let {
            username = it.name
            contact = it.phone
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Manage Account") })
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = contact,
                onValueChange = { contact = it },
                label = { Text("Contact") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { /* TODO: Implement update logic */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("change_password") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Change Password")
            }
        }
    }
}
