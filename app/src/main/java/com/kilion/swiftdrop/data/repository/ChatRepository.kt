package com.kilion.swiftdrop.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kilion.swiftdrop.data.model.ChatMessage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    /**
     * Sends a chat message to a specific conversation in Firestore.
     */
    fun sendMessage(conversationId: String, message: ChatMessage) {
        firestore.collection("chats").document(conversationId)
            .collection("messages").add(message)
    }

    /**
     * Listens for real-time updates to a chat conversation and returns a Flow of messages.
     */
    fun getMessages(conversationId: String): Flow<List<ChatMessage>> = callbackFlow {
        val subscription = firestore.collection("chats").document(conversationId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error) // Close the flow on error
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val messages = snapshot.toObjects(ChatMessage::class.java)
                    trySend(messages) // Send the latest list of messages
                }
            }

        // This is called when the Flow is closed or cancelled
        awaitClose { subscription.remove() }
    }
}