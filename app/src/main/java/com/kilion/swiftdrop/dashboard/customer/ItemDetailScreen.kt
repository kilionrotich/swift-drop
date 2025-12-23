package com.kilion.swiftdrop.dashboard.customer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kilion.swiftdrop.data.model.Review

@Composable
fun ItemDetailScreen(viewModel: CustomerViewModel = hiltViewModel(), itemId: String?) {
    val reviews by viewModel.reviews.collectAsState()
    // You would also fetch the item details here, but for now we'll focus on reviews

    LaunchedEffect(itemId) {
        if (itemId != null) {
            viewModel.loadReviewsForItem(itemId)
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Reviews", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // TODO: Display average rating here

        LazyColumn {
            items(reviews) { review ->
                ReviewCard(review = review)
            }
        }
    }
}

@Composable
fun ReviewCard(review: Review) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = review.user, style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.weight(1f))
                Row {
                    (1..5).forEach { star ->
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = if (star <= review.rating) Color.Yellow else Color.Gray
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = review.comment)
        }
    }
}
