package com.example.tiktok_clone.features.notification.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktok_clone.features.notification.data.model.SocialNotificationAction
import com.example.tiktok_clone.features.notification.ui.NotificationUiState
import com.example.tiktok_clone.features.social.data.NotificationRepository
import com.example.tiktok_clone.features.social.data.model.Notification
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val repository: NotificationRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<NotificationUiState<Notification>>(
        NotificationUiState.Loading
    )
    val uiState: StateFlow<NotificationUiState<Notification>> = _uiState.asStateFlow()

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications.asStateFlow()

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var hasLoadedOnce: Boolean = false

    // Tai danh sach thong bao va unread count
    fun loadNotifications() {
        viewModelScope.launch {
            _uiState.value = NotificationUiState.Loading
            try {
                val items = repository.getNotifications()
                val unread = repository.getUnreadCount()
                _notifications.value = items
                _unreadCount.value = unread
                _uiState.value = NotificationUiState.Success(
                    items = items,
                    unreadCount = unread,
                )
                hasLoadedOnce = true
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = NotificationUiState.Error(
                    message = e.message ?: "Lỗi tải thông báo"
                )
            }
        }
    }

    // Xoa loi hien tai
    fun clearError() {
        _error.value = null
    }

    // Dieu huong action thong bao
    fun onAction(action: SocialNotificationAction) {
        when (action) {
            SocialNotificationAction.LoadNotifications -> loadNotifications()
            SocialNotificationAction.MarkAllSeen -> markAllSeen()
            SocialNotificationAction.ClearError -> clearError()
        }
    }

    // Danh dau tat ca da xem
    fun markAllSeen() {
        viewModelScope.launch {
            markAllSeenNow()
        }
    }

    // Danh dau da xem va refresh lai list/badge
    suspend fun markAllSeenNow() {
        try {
            repository.seenAll()
        } catch (_: Exception) {
            // im lặng
        }
        try {
            refreshListAndBadge()
        } catch (_: Exception) {
            // im lặng
        }
    }

    // Refresh list va unread count tu server
    private suspend fun refreshListAndBadge() {
        _notifications.value = repository.getNotifications()
        _unreadCount.value = repository.getUnreadCount()
    }
}

