package com.kilion.swiftdrop.dashboard.rider

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RiderDashboardScreen(viewModel: RiderViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAcceptedOrders()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("🚴 Rider Dashboard", modifier = Modifier.padding(bottom = 16.dp))

        when (val state = uiState) {
            is RiderUiState.Success -> {
                val orders = state.orders

                if (orders.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No orders available for pickup.")
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(orders) { order ->
                            Card(modifier = Modifier.padding(8.dp)) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Order ID: ${order.id}")
                                    Text("Pickup: ${order.pickupLocation}")
                                    Text("Drop-off: ${order.dropoffLocation}")
                                    Button(onClick = { viewModel.pickupOrder(order.id) }) {
                                        Text("Pick Up")
                                    }
                                }
                            }
                        }
                    }
                }
            }
            is RiderUiState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }
            is RiderUiState.Error -> Text(state.message)
            else -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No orders available.")
                }
            }
        }
    }
}