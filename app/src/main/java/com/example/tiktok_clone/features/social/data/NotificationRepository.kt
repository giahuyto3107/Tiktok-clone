package com.example.tiktok_clone.features.social.data

import com.example.tiktok_clone.features.social.data.model.Notification

class NotificationRepository(
    private val api: NotificationApiService,
) {

    suspend fun getNotifications(limit: Int = 20, offset: Int = 0): List<Notification> =
        api.getNotifications(limit = limit, offset = offset).notifications.map { it.toModel() }

    suspend fun getLatestNotification(): Notification? =
        api.getLatestNotification().notification?.toModel()

    suspend fun getUnreadCount(): Int = api.getUnreadCount().unreadCount

    suspend fun seenAll(): Int {
        val res = api.seenAll()
        return res.body()?.unreadCount ?: 0
    }

}
