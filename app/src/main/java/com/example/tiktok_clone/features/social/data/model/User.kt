package com.example.tiktok_clone.features.social.data.model


data class User(
    val id: String,
    val userName: String,
    val avatarUrl: String? = null,

    val followerCount: Long = 0,
    val followingCount: Long = 0,

    // user-contextual state
    val isFollowing: Boolean = false
)
