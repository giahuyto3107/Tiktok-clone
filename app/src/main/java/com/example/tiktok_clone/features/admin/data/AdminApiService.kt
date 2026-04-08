package com.example.tiktok_clone.features.admin.data

import retrofit2.http.*

data class AdminDashboardStats(
    val total_users: Int,
    val new_users: Int,
    val active_users: Int
)

data class AdminUserItem(
    val id: String,
    val stt: String,
    val name: String,
    val handle: String,
    val email: String,
    val date: String,
    val avatarUrl: String = "",
    val bio: String = "",
    val followers: String = "0",
    val following: String = "0",
    val likes: String = "0",
    val isVerified: Boolean = false,
    val isBanned: Boolean = false
)

data class AdminUserListResponse(
    val items: List<AdminUserItem>,
    val total: Int,
    val page: Int,
    val limit: Int,
    val total_pages: Int
)

data class AdminUserUpdateStatusRequest(
    val is_banned: Boolean? = null,
    val is_verified: Boolean? = null
)

data class AdminUpdateStatusResponse(
    val message: String,
    val user_id: String
)

interface AdminApiService {
    @GET("api/v1/admin/dashboard/stats")
    suspend fun getDashboardStats(): AdminDashboardStats

    @GET("api/v1/admin/users")
    suspend fun getUsers(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): AdminUserListResponse

    @PUT("api/v1/admin/users/{userId}/status")
    suspend fun updateUserStatus(
        @Path("userId") userId: String,
        @Body request: AdminUserUpdateStatusRequest
    ): AdminUpdateStatusResponse
}
