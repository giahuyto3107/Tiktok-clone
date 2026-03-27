package com.example.tiktok_clone.features.social.data.model

data class Comment(
    val id: String,
    val postId: String,
    val userId: String,
    val content: String,
    val imageUri: String? = null,
    val likeCount: Long = 0,
    val isLiked: Boolean = false,
    val parentId: String? = null, // null = comment gốc
    val replyCount: Long = 0,
    val createdAt: Long // thời gian comment
)
