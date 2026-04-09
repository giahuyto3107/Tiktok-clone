package com.example.tiktok_clone.features.notification.data.model

sealed interface FollowNotificationAction {
    data class SetScreenOpen(val open: Boolean) : FollowNotificationAction
    data object LoadNotifications : FollowNotificationAction
    data object MarkAllSeen : FollowNotificationAction
}

