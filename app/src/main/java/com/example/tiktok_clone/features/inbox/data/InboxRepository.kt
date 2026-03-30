package com.example.tiktok_clone.features.inbox.data

import com.example.tiktok_clone.features.inbox.data.model.Message
import com.example.tiktok_clone.features.inbox.data.model.MessageStatus
import com.example.tiktok_clone.features.inbox.data.model.MessageType
import com.example.tiktok_clone.features.social.data.FollowUserResponse
import java.io.File
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class InboxRepository(
    private val api: InboxApiService,
) {
    data class CachedMessages(
        val chatId: Int,
        /**
         * Dữ liệu raw theo đúng format backend trả về.
         * UI sẽ tự reverse để hiển thị theo thứ tự mong muốn.
         */
        val rawMessages: List<Message>,
    )

    private val cacheLock = Any()
    private val chatIdByOtherUid: MutableMap<String, Int> = mutableMapOf()
    private val messagesCacheByChatId: MutableMap<Int, List<Message>> = mutableMapOf()

    fun putChatIdMapping(chatWithId: String, chatId: Int) {
        synchronized(cacheLock) {
            chatIdByOtherUid[chatWithId] = chatId
        }
    }

    fun getCachedMessages(chatWithId: String): CachedMessages? {
        val resolvedChatId = chatWithId.toIntOrNull() ?: synchronized(cacheLock) {
            chatIdByOtherUid[chatWithId]
        } ?: return null

        return synchronized(cacheLock) {
            messagesCacheByChatId[resolvedChatId]?.let { raw ->
                CachedMessages(chatId = resolvedChatId, rawMessages = raw)
            }
        }
    }

    /**
     * Trả về danh sách contact theo "shape" giống getFollowers/getFollowing (Social).
     * Dễ reuse UI hiện có đang consume List<FollowUserResponse>.
     */
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

    suspend fun getChats(limit: Int? = null, offset: Int? = null): ChatsResponse {
        val res = api.getChats(limit = limit, offset = offset)
        synchronized(cacheLock) {
            res.chats.forEach { chat ->
                chatIdByOtherUid[chat.otherUserId] = chat.chatId
            }
        }
        return res
    }

    suspend fun getMessages(chatId: Int, limit: Int? = null, offset: Int? = null): List<Message> {
        val res = api.getMessages(chatId = chatId, limit = limit, offset = offset)
        val mapped = res.messages.map { it.toMessage() }
        synchronized(cacheLock) {
            messagesCacheByChatId[chatId] = mapped
        }
        return mapped
    }

    data class MessagesPage(
        val messages: List<Message>,
        val total: Int,
    )

    suspend fun getMessagesPage(
        chatId: Int,
        limit: Int? = null,
        offset: Int? = null,
    ): MessagesPage {
        val res = api.getMessages(chatId = chatId, limit = limit, offset = offset)
        val mapped = res.messages.map { it.toMessage() }
        synchronized(cacheLock) {
            messagesCacheByChatId[chatId] = mapped
        }
        return MessagesPage(messages = mapped, total = res.total)
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

    suspend fun sendMediaMessage(
        otherUid: String,
        imageUri: String,
        type: String,
        content: String? = null,
    ): Message {
        val body = SendMessageRequest(
            content = content,
            imageUri = imageUri,
            type = type.uppercase(),
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
        mimeType: String? = null,
    ): Message {
        val normalizedType = type.uppercase()

        val mediaTypeString = mimeType
            ?: when (normalizedType) {
                "VIDEO" -> "video/mp4"
                else -> "image/jpeg"
            }
        val mediaType = mediaTypeString.toMediaType()

        val requestBody = file.asRequestBody(mediaType)
        val filePart = MultipartBody.Part.createFormData(
            name = "file",
            filename = file.name,
            body = requestBody,
        )

        val typeBody = normalizedType.toRequestBody("text/plain".toMediaType())
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
    type = type?.toMessageType() ?: MessageType.TEXT,
    status = status?.toMessageStatus() ?: MessageStatus.SENT,
    imageUri = imageUri,
    receiptStatus = receiptStatus?.toMessageStatus(),
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

