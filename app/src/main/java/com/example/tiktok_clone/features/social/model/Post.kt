package com.example.tiktok_clone.features.social.model

data class Post(
    val id: String,
    val userId: String,
    val likes: Int,
    val sharesTime: String,
    val views: Int,
    val privacy: Boolean,
    val imageUrl: String,
    val description: String,
)