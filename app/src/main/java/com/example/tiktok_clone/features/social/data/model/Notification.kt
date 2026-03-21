package com.example.tiktok_clone.features.social.data.model

data class Notification(
    val id: Int,
    val fromUserId: String,
    val postId: Int,
    val actionType: NotificationActionType,
    val commentId: Int? = null,
    val createdAt: Long,
    val receiptStatus: NotificationReceiptStatus,
)

enum class NotificationActionType {
    LIKE,
    COMMENT,
    COMMENT_REPLY,
    COMMENT_LIKE,
    REPOST,
}

enum class NotificationReceiptStatus {
    DELIVERED, SEEN
}