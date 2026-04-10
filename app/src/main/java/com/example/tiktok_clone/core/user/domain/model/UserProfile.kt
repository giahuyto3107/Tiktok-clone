package com.example.tiktok_clone.core.user.domain.model

import android.net.Uri

data class UserProfile(
    val id: String,
    val email: String?,
    val displayName: String?,
    val avtPhotoUrl: String?,
)
