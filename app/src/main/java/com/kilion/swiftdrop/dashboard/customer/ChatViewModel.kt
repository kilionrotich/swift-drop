package com.kilion.swiftdrop.dashboard.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kilion.swiftdrop.data.model.ChatMessage
import com.kilion.swiftdrop.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    fun loadMessages(conversationId: String) {
        viewModelScope.launch {
            chatRepository.getMessages(conversationId).collect {
                _messages.value = it
            }
        }
    }

    fun sendMessage(conversationId: String, text: String, senderId: String) {
        val message = ChatMessage(senderId = senderId, text = text)
        chatRepository.sendMessage(conversationId, message)
    }
}