package com.example.tiktok_clone.features.social.data.model

data class PostLike(
    val postId: String,
    val userId: String,
    val createdAt: Long = System.currentTimeMillis()
)
