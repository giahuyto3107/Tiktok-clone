package com.example.tiktok_clone.features.social.model

data class Comment(
    val id: String,
    val postId: String,
    val author: User,

    val content: String,

    val likeCount: Long = 0,
    val isLiked: Boolean = false,

    val parentId: String? = null, // null = comment gốc
    val replyCount: Long = 0,

    val createdAt: Long
)
