package com.kilion.swiftdrop.dashboard.shop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ShopAnalyticsScreen(viewModel: ShopViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAnalytics()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Analytics", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        when (val state = uiState) {
            is ShopUiState.Loading -> {
                CircularProgressIndicator()
            }
            is ShopUiState.AnalyticsLoaded -> {
                val analytics = state.analytics
                Column {
                    AnalyticsCard(title = "Total Revenue", value = "$${analytics.totalRevenue}")
                    Spacer(modifier = Modifier.height(8.dp))
                    AnalyticsCard(title = "Total Orders", value = "${analytics.totalOrders}")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Top Selling Items", style = MaterialTheme.typography.titleMedium)
                    Column {
                        analytics.topSellingItems.forEach { item ->
                            Text("${item.name}: ${item.salesCount} sales")
                        }
                    }
                }
            }
            is ShopUiState.Error -> {
                Text(state.message, color = MaterialTheme.colorScheme.error)
            }
            else -> {
                // Handle other states if necessary
            }
        }
    }
}

@Composable
fun AnalyticsCard(title: String, value: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}