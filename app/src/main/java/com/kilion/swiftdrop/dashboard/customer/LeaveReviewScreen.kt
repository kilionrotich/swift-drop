package com.kilion.swiftdrop.dashboard.customer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kilion.swiftdrop.ui.components.SwiftDropTextField

@Composable
fun LeaveReviewScreen(navController: NavController, viewModel: CustomerViewModel = hiltViewModel(), orderId: String?) {
    var reviewText by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Leave a Review")
        Spacer(modifier = Modifier.height(16.dp))

        StarRatingInput(rating = rating, onRatingChange = { rating = it })

        Spacer(modifier = Modifier.height(16.dp))

        SwiftDropTextField(value = reviewText, onValueChange = { reviewText = it }, label = "Your review")
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (orderId != null) {
                    // For now, we'll just use the first item in the cart as the one being reviewed.
                    // A more robust solution would be to pass the specific item ID to this screen.
                    val itemId = viewModel.cart.value.firstOrNull()?.id
                    if (itemId != null) {
                        viewModel.submitReview(orderId, itemId, rating, reviewText)
                        navController.popBackStack()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = reviewText.isNotBlank() && rating > 0
        ) {
            Text("Submit Review")
        }
    }
}

@Composable
fun StarRatingInput(rating: Int, onRatingChange: (Int) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        (1..5).forEach { star ->
            IconButton(onClick = { onRatingChange(star) }) {
                Icon(
                    imageVector = if (star <= rating) Icons.Filled.Star else Icons.Outlined.StarBorder,
                    contentDescription = "Star $star",
                    tint = if (star <= rating) Color.Yellow else Color.Gray
                )
            }
        }
    }
}