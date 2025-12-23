package com.kilion.swiftdrop.dashboard.shop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kilion.swiftdrop.data.model.Order

@Composable
fun ShopOrderHistoryScreen(viewModel: ShopViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getOrders()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Order History", style = MaterialTheme.typography.headlineMedium)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is ShopUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is ShopUiState.SuccessOrders -> {
                    if (state.orders.isEmpty()) {
                        Text(
                            text = "You have no past orders.",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        // Display only completed or delivered orders
                        val completedOrders = state.orders.filter { it.status.equals("Delivered", ignoreCase = true) || it.status.equals("Completed", ignoreCase = true) }
                        if(completedOrders.isEmpty()){
                             Text(
                                text = "You have no completed orders.",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(16.dp)
                            )
                        } else {
                            OrderHistoryList(orders = completedOrders)
                        }
                    }
                }
                is ShopUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun OrderHistoryList(orders: List<Order>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(orders, key = { it.id }) { order ->
            OrderHistoryCard(order = order)
        }
    }
}

@Composable
fun OrderHistoryCard(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Order ID: ${order.id}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Status: ${order.status}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}