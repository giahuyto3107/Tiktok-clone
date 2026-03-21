package com.example.tiktok_clone.features.social.data.model

data class FollowNotification(
    val id: Int,
    val followerId: String,
    val createdAt: Long,
    val receiptStatus: FollowNotificationReceiptStatus,
)
enum class FollowNotificationReceiptStatus {
    DELIVERED, SEEN
}
