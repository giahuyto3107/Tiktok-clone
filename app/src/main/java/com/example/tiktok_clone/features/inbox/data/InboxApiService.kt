package com.example.tiktok_clone.features.inbox.data

import com.example.tiktok_clone.features.inbox.data.model.MessageStatus
import com.example.tiktok_clone.features.inbox.data.model.MessageType
import com.example.tiktok_clone.features.social.data.FollowUserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface InboxApiService {

    @GET("api/v1/inbox/contacts")
    suspend fun getContacts(
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null,
    ): ContactsResponse

    @GET("api/v1/inbox/chats")
    suspend fun getChats(
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null,
    ): ChatsResponse

    @GET("api/v1/inbox/chats/{chatId}/messages")
    suspend fun getMessages(
        @Path("chatId") chatId: Int,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null,
    ): MessagesResponse

    @POST("api/v1/inbox/chats/{otherUid}/messages")
    suspend fun sendMessage(
        @Path("otherUid") otherUid: String,
        @Body body: SendMessageRequest,
    ): MessageDto

    @Multipart
    @POST("api/v1/inbox/chats/{otherUid}/messages/upload")
    suspend fun uploadMessage(
        @Path("otherUid") otherUid: String,
        @Part file: MultipartBody.Part,
        @Part("type") type: RequestBody?,
        @Part("content") content: RequestBody?,
    ): MessageDto
}

// region Response DTOs (đúng format JSON backend)

data class ChatsResponse(
    val chats: List<ChatResponse>,
    val total: Int,
)

data class ContactsResponse(
    val users: List<FollowUserResponse>,
    val total: Int,
)

data class ChatResponse(
    val chatId: Int,
    val otherUserId: String,
    val lastMessage: MessageDto? = null,
    val unreadCount: Int = 0,
)

/** Message trả về từ API (id là số) */
data class MessageDto(
    val id: Int,
    val content: String?,
    val senderId: String,
    val timestamp: Long,
    val type: String?= MessageType.TEXT.toString(),
    val status: String? = MessageStatus.SENT.toString(),
    val imageUri: String? = null,
    val receiptStatus: String? = null,
)

data class MessagesResponse(
    val messages: List<MessageDto>,
    val total: Int,
)

// endregion

// region Request DTOs

/** Body POST gửi tin TEXT – spec: content, imageUri, type */
data class SendMessageRequest(
    val content: String? = null,
    val imageUri: String? = null,
    val type: String = "TEXT",
)

// endregion
