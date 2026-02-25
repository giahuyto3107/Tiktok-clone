package com.example.tiktok_clone.core.user.data.repository

import com.example.tiktok_clone.core.user.domain.model.UserProfile
import com.example.tiktok_clone.core.user.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth

class UserRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : UserRepository {
    override fun getCurrentUser(): UserProfile? {
        return firebaseAuth.currentUser?.let { user ->
            UserProfile(
                id = user.uid,
                email = user.email,
                displayName = user.displayName,
                avtPhotoUrl = user.photoUrl.toString()
            )
        }
    }

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
}