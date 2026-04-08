package com.example.tiktok_clone.features.notification.data.model

sealed interface SocialNotificationAction {
    data object PreloadIfNeeded : SocialNotificationAction
    data object LoadNotifications : SocialNotificationAction
    data object MarkAllSeen : SocialNotificationAction
    data object ClearError : SocialNotificationAction
}

