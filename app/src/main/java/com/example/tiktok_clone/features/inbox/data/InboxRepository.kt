package com.example.tiktok_clone.features.inbox.data

import com.example.tiktok_clone.features.inbox.data.model.Message
import com.example.tiktok_clone.features.inbox.data.model.MessageStatus
import com.example.tiktok_clone.features.inbox.data.model.MessageType
import java.io.File
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class InboxRepository(
    private val api: InboxApiService,
) {

    suspend fun getChats(limit: Int? = null, offset: Int? = null): ChatsResponse {
        return api.getChats(limit = limit, offset = offset)
    }

    suspend fun getMessages(chatId: Int, limit: Int? = null, offset: Int? = null): List<Message> {
        val res = api.getMessages(chatId = chatId, limit = limit, offset = offset)
        return res.messages.map { it.toMessage() }
    }

    /**
     * Gửi tin nhắn TEXT (không upload file).
     */
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

    /**
     * Gửi tin nhắn kèm file (IMAGE / VIDEO) bằng endpoint multipart.
     * File: ảnh hoặc video đã có sẵn dưới dạng File.
     */
    suspend fun sendMessageWithFile(
        otherUid: String,
        file: File,
        type: String,
        content: String? = null,
    ): Message {
        val mediaType = when (type.uppercase()) {
            "VIDEO" -> "video/*"
            else -> "image/*"
        }.toMediaType()

        val requestBody = file.asRequestBody(mediaType)
        val filePart = MultipartBody.Part.createFormData(
            name = "file",
            filename = file.name,
            body = requestBody,
        )

        val typeBody = type.toRequestBody("text/plain".toMediaType())
        val contentBody = content?.toRequestBody("text/plain".toMediaType())

        val dto = api.uploadMessage(
            otherUid = otherUid,
            file = filePart,
            type = typeBody,
            content = contentBody,
        )
        return dto.toMessage()
    }

    /** Dùng khi cần hiển thị lastMessage từ ChatResponse (MessageDto → Message). */
    fun mapToMessage(dto: MessageDto): Message = dto.toMessage()
}

private fun MessageDto.toMessage(): Message = Message(
    id = id.toString(),
    content = content ?: "",
    senderId = senderId,
    timestamp = timestamp,
    type = type.toMessageType(),
    status = status.toMessageStatus(),
    imageUri = imageUri,
)

private fun String.toMessageType(): MessageType = when (uppercase()) {
    "IMAGE" -> MessageType.IMAGE
    "VIDEO" -> MessageType.VIDEO
    else -> MessageType.TEXT
}

private fun String.toMessageStatus(): MessageStatus = when (uppercase()) {
    "NEW" -> MessageStatus.NEW
    "SENT" -> MessageStatus.SENT
    "DELIVERED" -> MessageStatus.DELIVERED
    "SEEN" -> MessageStatus.SEEN
    else -> MessageStatus.SENT
}
