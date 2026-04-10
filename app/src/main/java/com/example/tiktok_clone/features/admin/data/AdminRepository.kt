package com.example.tiktok_clone.features.admin.data

class AdminRepository(private val api: AdminApiService) {

    suspend fun getDashboardStats(): AdminDashboardStats =
        api.getDashboardStats()

    suspend fun getUsers(page: Int = 1, limit: Int = 10): AdminUserListResponse =
        api.getUsers(page = page, limit = limit)

    suspend fun updateUserStatus(
        userId: String,
        isBanned: Boolean? = null,
        isVerified: Boolean? = null
    ): AdminUpdateStatusResponse =
        api.updateUserStatus(
            userId,
            AdminUserUpdateStatusRequest(is_banned = isBanned, is_verified = isVerified)
        )
}
