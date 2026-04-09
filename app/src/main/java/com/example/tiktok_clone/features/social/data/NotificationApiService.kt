package com.example.tiktok_clone.features.social.data

import com.example.tiktok_clone.features.social.data.model.NotificationActionType
import com.example.tiktok_clone.features.social.data.model.NotificationReceiptStatus
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationApiService {

    // GET notifications
    @GET("api/v1/social/notifications")
    suspend fun getNotifications(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): NotificationListResponse

    // GET latest notification
    @GET("api/v1/social/notifications/latest")
    suspend fun getLatestNotification(): LatestNotificationResponse

    // GET unread count
    @GET("api/v1/social/notifications/unread-count")
    suspend fun getUnreadCount(): UnreadCountResponse

    // POST mark 1 notification seen
    @POST("api/v1/social/notifications/{notification_id}/seen")
    suspend fun markSeen(
        @Path("notification_id") notificationId: Int,
    ): Response<MarkSeenResponse>

    // POST seen all notifications
    @POST("api/v1/social/notifications/seenAll")
    suspend fun seenAll(): Response<SeenAllResponse>



}

data class NotificationListResponse(
    @SerializedName("notifications") val notifications: List<NotificationDto>,
    @SerializedName("total") val total: Int,
)

data class UnreadCountResponse(
    @SerializedName("unreadCount") val unreadCount: Int,
)

data class MarkSeenResponse(
    @SerializedName("ok") val ok: Boolean,
)

data class SeenAllResponse(
    @SerializedName("unreadCount") val unreadCount: Int,
)

data class NotificationDto(
    @SerializedName("id") val id: Int,
    @SerializedName("fromUserId") val fromUserId: String,
    @SerializedName("postId") val postId: Int,
    @SerializedName("actionType") val actionType: String,
    @SerializedName("commentId") val commentId: Int? = null,
    @SerializedName("createdAt") val createdAt: Long,
    @SerializedName("receiptStatus") val receiptStatus: String,
)

data class LatestNotificationResponse(
    @SerializedName("notification") val notification: NotificationDto?,
)

// Map notification dto sang model
fun NotificationDto.toModel() = com.example.tiktok_clone.features.social.data.model.Notification(
    id = id,
    fromUserId = fromUserId,
    postId = postId,
    actionType = actionType.toActionType(),
    commentId = commentId,
    createdAt = createdAt,
    receiptStatus = receiptStatus.toReceiptStatus(),
)

private fun String.toActionType() = try {
    NotificationActionType.valueOf(uppercase())
} catch (_: Exception) {
    NotificationActionType.LIKE
}

private fun String.toReceiptStatus() = when (uppercase()) {
    "SEEN" -> NotificationReceiptStatus.SEEN
    else -> NotificationReceiptStatus.DELIVERED
}
