package com.example.tiktok_clone.core.user.domain.repository

import com.example.tiktok_clone.core.user.domain.model.UserProfile

interface UserRepository {
    fun getCurrentUser(): UserProfile?
    fun isUserLoggedIn(): Boolean
}