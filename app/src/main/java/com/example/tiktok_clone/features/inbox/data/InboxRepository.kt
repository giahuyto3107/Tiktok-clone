package com.example.tiktok_clone.features.inbox.data

import com.example.tiktok_clone.features.inbox.data.model.Message
import com.example.tiktok_clone.features.inbox.data.model.MessageStatus
import com.example.tiktok_clone.features.inbox.data.model.MessageType
import com.example.tiktok_clone.features.social.data.FollowUserResponse

class InboxRepository(
    private val api: InboxApiService,
) {

    // Lay danh sach contact (friends/chat)
    suspend fun getContacts(
        limit: Int? = 50,
        offset: Int? = 0,
    ): List<FollowUserResponse> {
        val res = api.getContacts(limit = limit, offset = offset)
        return res.users.map { u ->
            FollowUserResponse(
                uid = u.uid,
                username = u.username,
                avatarUrl = u.avatarUrl,
            )
        }
    }

    // Lay danh sach chat cua user
    suspend fun getChats(limit: Int? = null, offset: Int? = null): ChatsResponse =
        api.getChats(limit = limit, offset = offset)

    // Lay danh sach tin nhan theo chatId
    suspend fun getMessages(
        chatId: Int,
        limit: Int? = null,
    ): List<Message> {
        val res = api.getMessages(chatId = chatId, limit = limit, offset = 0)
        return res.messages.map { it.toMessage() }
    }

    // Gui tin nhan text cho user khac
    suspend fun sendMessage(
        otherUid: String,
        content: String? = null,
    ): Message {
        val body = SendMessageRequest(
            content = content,
            imageUri = null,
            type = "TEXT",
        )
        return api.sendMessage(otherUid, body).toMessage()
    }

    // Map dto sang model Message
    fun mapToMessage(dto: MessageDto): Message = dto.toMessage()
}

// Convert MessageDto sang Message
private fun MessageDto.toMessage(): Message = Message(
    id = id.toString(),
    content = content ?: "",
    senderId = senderId,
    timestamp = timestamp,
    type = type?.toMessageType() ?: MessageType.TEXT,
    status = status?.toMessageStatus() ?: MessageStatus.SENT,
    imageUri = imageUri,
    receiptStatus = receiptStatus?.toMessageStatus(),
)

// Convert string type sang enum MessageType
private fun String.toMessageType(): MessageType = when (uppercase()) {
    "IMAGE" -> MessageType.IMAGE
    "VIDEO" -> MessageType.VIDEO
    else -> MessageType.TEXT
}

// Convert string status sang enum MessageStatus
private fun String.toMessageStatus(): MessageStatus = when (uppercase()) {
    "NEW" -> MessageStatus.NEW
    "SENT" -> MessageStatus.SENT
    "DELIVERED" -> MessageStatus.DELIVERED
    "SEEN" -> MessageStatus.SEEN
    else -> MessageStatus.SENT
}
