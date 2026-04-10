package com.example.tiktok_clone.features.notification.ui

sealed interface NotificationUiState<out T> {
    data object Loading : NotificationUiState<Nothing>

    data class Success<T>(
        val items: List<T> = emptyList(),
        val unreadCount: Int = 0,
    ) : NotificationUiState<T>

    data class Error(
        val message: String,
    ) : NotificationUiState<Nothing>
}