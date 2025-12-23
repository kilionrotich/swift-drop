package com.kilion.swiftdrop.dashboard.rider

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RiderOrdersScreen(viewModel: RiderViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState(initial = RiderUiState.Idle)

    LaunchedEffect(Unit) {
        viewModel.getAssignedOrders()
    }

    when (uiState) {
        is RiderUiState.Loading -> CircularProgressIndicator()

        is RiderUiState.Error -> Text((uiState as RiderUiState.Error).message)

        is RiderUiState.Success -> {
            val orders = (uiState as RiderUiState.Success).orders
            LazyColumn {
                items(orders) { order ->
                    Card(modifier = Modifier.padding(8.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Order ID: ${order.id}")
                            Text("Pickup: ${order.pickupLocation}")
                            Text("Drop-off: ${order.dropoffLocation}")
                            Button(onClick = { viewModel.acceptOrder(order.id) }) {
                                Text("Accept Order")
                            }
                        }
                    }
                }
            }
        }

        is RiderUiState.OrderAccepted -> {
            val order = (uiState as RiderUiState.OrderAccepted).order
            Column(modifier = Modifier.padding(16.dp)) {
                Text("✅ You accepted order ${order.id}")
                Text("Pickup: ${order.pickupLocation}")
                Text("Drop-off: ${order.dropoffLocation}")
            }
        }

        is RiderUiState.Validated -> {
            val order = (uiState as RiderUiState.Validated).order
            Column(modifier = Modifier.padding(16.dp)) {
                Text("✅ Order ${order.id} validated successfully.")
                Text("Pickup: ${order.pickupLocation}")
                Text("Drop-off: ${order.dropoffLocation}")
            }
        }

        is RiderUiState.HistorySuccess -> {
            // Do nothing
        }

        is RiderUiState.Idle -> Text("No available orders.")
    }
}