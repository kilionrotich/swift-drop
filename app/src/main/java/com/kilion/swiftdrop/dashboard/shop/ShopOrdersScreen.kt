package com.kilion.swiftdrop.dashboard.shop

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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

@Composable
fun ShopOrdersScreen(
    uiState: ShopUiState,
    onAcceptOrder: (String) -> Unit,
    onGetOrders: () -> Unit
) {
    LaunchedEffect(Unit) {
        Log.d("ShopOrdersScreen", "Screen composed, calling onGetOrders()")
        onGetOrders()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is ShopUiState.Loading -> {
                CircularProgressIndicator()
            }

            is ShopUiState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }

            is ShopUiState.SuccessOrders -> {
                if (state.orders.isEmpty()) {
                    Text(
                        text = "You have no incoming orders.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    OrdersList(
                        orders = state.orders,
                        onAcceptOrder = onAcceptOrder
                    )
                }
            }

            is ShopUiState.QRReady -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(state.label, style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(16.dp))
                    Text("QR Code URL: ${state.qrCodeUrl}")
                }
            }

            is ShopUiState.Idle, is ShopUiState.SuccessItems, is ShopUiState.ShopProfileLoaded, is ShopUiState.AnalyticsLoaded -> {
                LaunchedEffect(Unit) {
                    onGetOrders()
                }
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun OrdersList(orders: List<Order>, onAcceptOrder: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(items = orders, key = { it.id }) { order ->
            OrderCard(
                order = order,
                onAccept = { onAcceptOrder(order.id) }
            )
        }
    }
}

@Composable
private fun OrderCard(order: Order, onAccept: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Order ID: ${order.id}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Status: ${order.status}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Items: ${order.items.joinToString()}",
                style = MaterialTheme.typography.bodySmall
            )
            if (order.status.equals("pending", ignoreCase = true)) {
                Button(
                    onClick = onAccept,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Accept Order")
                }
            }
        }
    }
}