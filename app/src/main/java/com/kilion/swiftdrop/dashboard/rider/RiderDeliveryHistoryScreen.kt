package com.kilion.swiftdrop.dashboard.rider

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kilion.swiftdrop.R

@Composable
fun RiderDeliveryHistoryScreen(
    navController: NavController,
    viewModel: RiderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getDeliveryHistory()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is RiderUiState.Loading -> {
                // The Lottie animation is commented out until you add the file.
                // To enable it, add 'rider_animation.json' to your 'res/raw' folder
                // and uncomment the code below.
                /*
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.rider_animation))
                val progress by animateLottieCompositionAsState(
                    composition,
                    iterations = LottieConstants.IterateForever
                )
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(200.dp)
                )
                */
                
                // Using the default circular progress indicator for now.
                CircularProgressIndicator()
            }
            is RiderUiState.Error -> Text(state.message)
            is RiderUiState.HistorySuccess -> {
                val orders = state.orders
                if (orders.isEmpty()) {
                    Text("No delivery history.")
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(orders) { order ->
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Order ID: ${order.id}", style = MaterialTheme.typography.titleMedium)
                                    Text("Status: ${order.status}", style = MaterialTheme.typography.bodyMedium)
                                    Text("Pickup: ${order.pickupLocation}", style = MaterialTheme.typography.bodyMedium)
                                    Text("Drop-off: ${order.dropoffLocation}", style = MaterialTheme.typography.bodyMedium)
                                    Text("Earnings: KES ${order.total}", style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }
            }
            else -> Text("No delivery history.")
        }
    }
}
