package com.kilion.swiftdrop.dashboard.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kilion.swiftdrop.data.model.Order
import com.kilion.swiftdrop.ui.theme.LightBlue

@Composable
fun OrderHistoryScreen(uiState: CustomerUiState, onGetOrders: () -> Unit, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        onGetOrders()
    }

    Box(
        modifier = modifier.fillMaxSize().background(LightBlue),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is CustomerUiState.Loading -> {
                CircularProgressIndicator()
            }
            is CustomerUiState.OrdersLoaded -> {
                if (uiState.orders.isEmpty()) {
                    Text(
                        text = "You have no past orders.",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    OrderList(orders = uiState.orders)
                }
            }
            is CustomerUiState.Error -> {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else -> {}
        }
    }
}

@Composable
fun OrderList(orders: List<Order>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(orders, key = { it.id }) { order ->
            OrderHistoryCard(order = order)
        }
    }
}

@Composable
fun OrderHistoryCard(order: Order, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Order #${order.id.take(6)}", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Status: ${order.status}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Total: $${order.total}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            order.items.forEach { item ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = item.name ?: "Unnamed Item", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
