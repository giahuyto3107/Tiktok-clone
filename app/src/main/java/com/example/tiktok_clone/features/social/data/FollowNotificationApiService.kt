package com.example.tiktok_clone.features.social.data

import com.example.tiktok_clone.features.social.data.model.FollowNotification
import com.example.tiktok_clone.features.social.data.model.FollowNotificationReceiptStatus
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FollowNotificationApiService {

    // GET follow notifications
    @GET("api/v1/social/follow/notifications")
    suspend fun getFollowNotifications(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): FollowNotificationListResponse

    // GET unread count
    @GET("api/v1/social/follow/notifications/unread-count")
    suspend fun getUnreadCount(): UnreadCountResponse

    // GET latest follow notification
    @GET("api/v1/social/follow/notifications/latest")
    suspend fun getLatestNotification(): FollowLatestNotificationResponse

    // POST seen all follow notifications
    @POST("api/v1/social/follow/notifications/seen-all")
    suspend fun seenAll(): Response<SeenAllResponse>

    // POST seen all follow notifications (camelCase)
    @POST("api/v1/social/follow/notifications/seenAll")
    suspend fun seenAllCamel(): Response<SeenAllResponse>
}

data class FollowLatestNotificationResponse(
    @SerializedName("notification") val notification: FollowNotificationDto?,
)

data class FollowNotificationListResponse(
    @SerializedName("notifications") val notifications: List<FollowNotificationDto>,
    @SerializedName("total") val total: Int,
)


data class FollowNotificationDto(
    @SerializedName("id") val id: Int,
    @SerializedName("followerId") val followerId: String,
    @SerializedName("createdAt") val createdAt: Long,
    @SerializedName("receiptStatus") val receiptStatus: String,
)

// Map follow notification dto sang model
fun FollowNotificationDto.toModel(): FollowNotification {
    return FollowNotification(
        id = id,
        followerId = followerId,
        createdAt = createdAt,
        receiptStatus = receiptStatus.toReceiptStatus(),
    )
}

private fun String.toReceiptStatus(): FollowNotificationReceiptStatus = when (uppercase()) {
    "SEEN" -> FollowNotificationReceiptStatus.SEEN
    "DELIVERED" -> FollowNotificationReceiptStatus.DELIVERED
    else -> FollowNotificationReceiptStatus.DELIVERED
}