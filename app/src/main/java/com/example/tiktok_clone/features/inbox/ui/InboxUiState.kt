package com.example.tiktok_clone.features.inbox.ui

sealed interface InboxUiState<out T> {
    data object Loading : InboxUiState<Nothing>

    data class Success<T>(
        val items: List<T> = emptyList(),
        val unreadCount: Int = 0,
        val isLoadingMore: Boolean = false,
        val hasMore: Boolean = false,
    ) : InboxUiState<T>

    data class Error(
        val message: String,
    ) : InboxUiState<Nothing>
}

