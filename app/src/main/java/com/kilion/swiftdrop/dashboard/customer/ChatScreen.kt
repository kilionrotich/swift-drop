package com.kilion.swiftdrop.dashboard.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kilion.swiftdrop.data.model.ChatMessage

val LacePink = Color(0xFFF5E6E8) // A soft, pinkish-lace color

@Composable
fun ChatScreen(
    conversationId: String,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsState()
    var text by remember { mutableStateOf("") }

    // This would be the ID of the currently logged-in user.
    // For now, we'll hardcode it. In a real app, you'd get this from a user profile.
    val currentUserId = "my_user_id"

    LaunchedEffect(conversationId) {
        viewModel.loadMessages(conversationId)
    }

    Column(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { message ->
                ChatMessageBubble(message, currentUserId)
            }
        }

        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message...") }
            )
            IconButton(onClick = {
                if (text.isNotBlank()) {
                    viewModel.sendMessage(conversationId, text, currentUserId)
                    text = ""
                }
            }) {
                Icon(Icons.Default.Send, contentDescription = "Send")
            }
        }
    }
}

@Composable
fun ChatMessageBubble(message: ChatMessage, currentUserId: String) {
    val isMyMessage = message.senderId == currentUserId
    val alignment = if (isMyMessage) Alignment.CenterEnd else Alignment.CenterStart
    val backgroundColor = if (isMyMessage) MaterialTheme.colorScheme.primaryContainer else LacePink

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = alignment) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(backgroundColor)
                .padding(12.dp)
        ) {
            Text(text = message.text)
        }
    }
}