package com.kilion.swiftdrop.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

/**
 * Represents a single message in a chat conversation.
 * The empty default values are required by Firestore for data serialization.
 */
data class ChatMessage(
    val senderId: String = "",
    val text: String = "",
    @ServerTimestamp val timestamp: Date? = null
)
