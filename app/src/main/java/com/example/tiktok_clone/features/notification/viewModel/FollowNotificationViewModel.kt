package com.example.tiktok_clone.features.notification.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktok_clone.features.notification.data.model.FollowNotificationAction
import com.example.tiktok_clone.features.notification.ui.NotificationUiState
import com.example.tiktok_clone.features.social.data.FollowNotificationRepository
import com.example.tiktok_clone.features.social.data.model.FollowNotification
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FollowNotificationViewModel(
    private val repository: FollowNotificationRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<NotificationUiState<FollowNotification>>(
        NotificationUiState.Loading
    )
    val uiState: StateFlow<NotificationUiState<FollowNotification>> = _uiState.asStateFlow()
    private val _notifications = MutableStateFlow<List<FollowNotification>>(emptyList())
    val notifications: StateFlow<List<FollowNotification>> = _notifications.asStateFlow()

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var isScreenOpen: Boolean = false
    private var hasLoadedOnce: Boolean = false

    // Danh dau man hinh follow noti dang mo/dong
    fun setScreenOpen(open: Boolean) {
        isScreenOpen = open
    }

    // Dieu huong action follow notification
    fun onAction(action: FollowNotificationAction) {
        when (action) {
            is FollowNotificationAction.SetScreenOpen -> setScreenOpen(action.open)
            FollowNotificationAction.LoadNotifications -> loadNotifications()
            FollowNotificationAction.MarkAllSeen -> markAllSeen()
        }
    }

    // Tai danh sach follow notification va unread count
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
        }

        try {
            refreshListAndBadge()
        } catch (_: Exception) {
        }
    }

    // Refresh list va unread count tu server
    private suspend fun refreshListAndBadge() {
        _notifications.value = repository.getNotifications()
        _unreadCount.value = repository.getUnreadCount()
    }
}

