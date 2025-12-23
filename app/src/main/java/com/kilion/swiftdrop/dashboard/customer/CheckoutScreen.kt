package com.kilion.swiftdrop.dashboard.customer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kilion.swiftdrop.data.model.Item
import com.kilion.swiftdrop.ui.components.SwiftDropTextField

@Composable
fun CheckoutScreen(navController: NavController, viewModel: CustomerViewModel = hiltViewModel()) {
    val cart by viewModel.cart.collectAsState()
    var dropoffAddress by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Checkout", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (cart.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Your cart is empty.")
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cart) { item ->
                    CartItemRow(item = item)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            SwiftDropTextField(value = dropoffAddress, onValueChange = { dropoffAddress = it }, label = "Drop-off Address")

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.placeOrder(dropoffAddress) },
                modifier = Modifier.fillMaxWidth(),
                enabled = dropoffAddress.isNotBlank()
            ) {
                Text("Confirm Order")
            }
        }
    }

    val uiState by viewModel.uiState.collectAsState()
    if (uiState is CustomerUiState.OrderPlaced) {
        // You can show a confirmation dialog or navigate to an order tracking screen
        // For now, we'll just navigate back to the home screen
        LaunchedEffect(Unit) {
            navController.navigate("customerHome") {
                popUpTo("checkout") { inclusive = true }
            }
        }
    }
}

@Composable
fun CartItemRow(item: Item) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = item.name ?: "Unnamed Item", modifier = Modifier.weight(1f))
            Text(text = "$${item.price}")
        }
    }
}
