package com.example.tiktok_clone.features.inbox.model

// Message.kt - Data model
data class Message(
    val id: String,
    val content: String,
    val senderId: String,
    val timestamp: Long,
    val type: MessageType,
    val status: MessageStatus = MessageStatus.SENT,
    val imageUri: String? = null,
)

enum class MessageType { TEXT, IMAGE, VIDEO }
enum class MessageStatus { SENDING, SENT, DELIVERED, SEEN, NEW }
