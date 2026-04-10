package com.example.tiktok_clone.core.user.domain.repository

import com.example.tiktok_clone.core.user.domain.model.UserProfile

interface UserRepository {
    fun getCurrentUser(): UserProfile?
    fun isUserLoggedIn(): Boolean
    fun updateProfile(displayName: String, photoUrl: String, onComplete: (Boolean) -> Unit)
    suspend fun uploadAvatarLocal(uri: android.net.Uri): String?
}