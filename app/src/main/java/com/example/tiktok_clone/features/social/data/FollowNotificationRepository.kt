package com.example.tiktok_clone.features.social.data

import com.example.tiktok_clone.features.social.data.model.FollowNotification
import com.example.tiktok_clone.features.social.data.model.FollowNotificationReceiptStatus

class FollowNotificationRepository(
    private val api: FollowNotificationApiService,
) {
    suspend fun getNotifications(
        limit: Int = 20,
        offset: Int = 0,
    ): List<FollowNotification> {
        val res = api.getFollowNotifications(limit = limit, offset = offset)
        return res.notifications.map { it.toModel() }
    }

    suspend fun getUnreadCount(): Int = api.getUnreadCount().unreadCount

    suspend fun getLatestNotification(): FollowNotification? =
        api.getLatestNotification().notification?.toModel()
    suspend fun seenAll(): Int {
        val res = api.seenAll()
        if (res.isSuccessful) {
            return res.body()?.unreadCount ?: 0
        }

        val resCamel = api.seenAllCamel()
        return if (resCamel.isSuccessful) {
            resCamel.body()?.unreadCount ?: 0
        } else {
            0
        }
    }
}