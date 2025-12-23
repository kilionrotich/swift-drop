package com.kilion.swiftdrop.dashboard.customer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kilion.swiftdrop.data.model.Order

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerOrdersScreen(navController: NavController, viewModel: CustomerViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMyOrders()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("My Orders") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when (val state = uiState) {
                is CustomerUiState.Loading -> CircularProgressIndicator()

                is CustomerUiState.Error -> Text(
                    state.message,
                    color = MaterialTheme.colorScheme.error
                )

                is CustomerUiState.OrdersLoaded -> {
                    val orders = state.orders
                    if (orders.isEmpty()) {
                        Text("You have no past orders.")
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(orders) { order ->
                                CustomerOrderCard(order)
                            }
                        }
                    }
                }

                is CustomerUiState.Idle -> Text("No active orders.")
                // Add other exhaustive cases
                is CustomerUiState.Success -> {
                    // This screen is for orders, not general item success.
                    // You might want to show a message or just an idle state.
                     Text("No active orders.")
                }
                is CustomerUiState.OrderPlaced -> {
                    // After placing an order, you might want to refresh the orders list.
                    LaunchedEffect(state.orderId) {
                        viewModel.loadMyOrders()
                    }
                    Text("Order placed successfully! Refreshing...")
                }
                is CustomerUiState.ReviewSubmitted -> {
                    // Can show a temporary message.
                    Text("Review submitted.")
                }
                 is CustomerUiState.ReviewsLoaded -> {
                    // Not relevant to this screen
                 }
            }
        }
    }
}

@Composable
fun CustomerOrderCard(order: Order) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Order ID: ${order.id}")
            Text("Status: ${order.status}")
            Text("Drop-off: ${order.dropoffLocation}")
        }
    }
}

@Composable
fun CustomerQrDisplay(qrUrl: String) {
    Text("Show this QR to a rider at delivery")
    Spacer(Modifier.height(12.dp))
    AsyncImage(
        model = qrUrl,
        contentDescription = "Customer QR",
        modifier = Modifier.size(200.dp)
    )
}
