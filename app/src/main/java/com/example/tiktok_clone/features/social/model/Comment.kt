package com.example.tiktok_clone.features.social.model

data class Comment(
    val id: String,
    val postId: String,
    val userId: String,
    val userName: String = "",
    val avatarUrl: String = "",
    val createdAt: Long,
    val commentContent: String,
    val isLiked: Boolean = false,
    val likeCount: Int = 0,
    val isReply: Boolean = false,
    val replyCount: Int = 0,
) {
}