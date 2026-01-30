package com.example.tiktok_clone.features.social.model

data class Comment(
    val id: String,
    val postId: String,
    val userId: String,
    val userName: String,
    val AvatarUrl: String,
    val commentTime: String,
    val comment: String,
    val isLiked: Boolean = false,
    val likeCount: Int,
    val isReply: Boolean = false,
    val replyCount: Int,
) {
}