package com.example.tiktok_clone.features.user.data

import android.util.Log
import com.example.tiktok_clone.features.social.data.model.User

/**
 * Repository for fetching Firebase user data from the backend.
 * Includes in-memory caching to avoid repeated network calls for the same user.
 */
class UserRepository(
    private val apiService: UserApiService
) {
    private companion object {
        private const val TAG = "UserRepository"
    }

    // Simple in-memory cache: uid -> User
    private val cache = mutableMapOf<String, User>()

    /**
     * Get a user by UID. Returns from cache if available.
     */
    suspend fun getUser(uid: String): User? {
        cache[uid]?.let { return it }

        return try {
            val response = apiService.getUser(uid)
            val user = response.toUser()
            cache[uid] = user
            user
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get user $uid", e)
            null
        }
    }

    /**
     * Fetch all users and cache them. Returns a map of uid -> User.
     */
    suspend fun getAllUsers(): Map<String, User> {
        return try {
            val response = apiService.getUsers()
            val userMap = response.users.associate { it.uid to it.toUser() }
            cache.putAll(userMap)
            userMap
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get users", e)
            emptyMap()
        }
    }

    /**
     * Get cached user or null (no network call).
     */
    fun getCachedUser(uid: String): User? = cache[uid]

    /**
     * Fetch users for a specific list of UIDs. Returns a map of uid -> User.
     */
    suspend fun getUsersByIds(uids: List<String>): Map<String, User> {
        val result = mutableMapOf<String, User>()
        val missing = mutableListOf<String>()

        for (uid in uids.distinct()) {
            val cached = cache[uid]
            if (cached != null) {
                result[uid] = cached
            } else {
                missing.add(uid)
            }
        }

        // Fetch missing users individually
        for (uid in missing) {
            getUser(uid)?.let { result[uid] = it }
        }

        return result
    }
}

/**
 * Convert API response to domain User model.
 */
fun FirebaseUserResponse.toUser(): User {
    return User(
        id = uid,
        userName = displayName ?: email ?: uid,
        avatarUrl = photoUrl,
    )
}
