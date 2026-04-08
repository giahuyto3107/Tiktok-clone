package com.example.tiktok_clone.features.notification.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktok_clone.core.network.RealtimeWebSocketClient
import com.example.tiktok_clone.features.notification.data.model.FollowNotificationAction
import com.example.tiktok_clone.features.notification.ui.NotificationUiState
import com.example.tiktok_clone.features.social.data.FollowNotificationRepository
import com.example.tiktok_clone.features.social.data.model.FollowNotification
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

class FollowNotificationViewModel(
    private val repository: FollowNotificationRepository,
    okHttpClient: OkHttpClient,
) : ViewModel() {
    private val _uiState = MutableStateFlow<NotificationUiState<FollowNotification>>(
        NotificationUiState.Loading
    )
    val uiState: StateFlow<NotificationUiState<FollowNotification>> = _uiState.asStateFlow()
    private val wsClient = RealtimeWebSocketClient(okHttpClient)
    private var connectedUid: String? = null

    private val _notifications = MutableStateFlow<List<FollowNotification>>(emptyList())
    val notifications: StateFlow<List<FollowNotification>> = _notifications.asStateFlow()

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var isScreenOpen: Boolean = false
    private var wsReloadInFlight: Boolean = false
    private var hasLoadedOnce: Boolean = false

    fun preloadNotificationsIfNeeded() {
        if (hasLoadedOnce || _isLoading.value) return
        loadNotifications()
    }

    private fun ensureWsConnected(uid: String) {
        if (connectedUid == uid) return
        connectedUid = uid

        viewModelScope.launch {
            wsClient.disconnect()
            wsClient.connect("api/v1/ws/social/users/$uid") { event, _ ->
                handleWsEvent(event)
            }
        }
    }

    private fun handleWsEvent(event: String) {
        if (event != "follow_notification_created" && event != "follow_notification_updated") return
        if (wsReloadInFlight) return
        wsReloadInFlight = true
        viewModelScope.launch {
            try {
                _notifications.value = repository.getNotifications(limit = 20, offset = 0)
                _unreadCount.value = repository.getUnreadCount()
            } catch (_: Exception) {
                // im lang
            } finally {
                wsReloadInFlight = false
            }
        }
    }

    fun setScreenOpen(open: Boolean) {
        isScreenOpen = open
    }

    fun onAction(action: FollowNotificationAction) {
        when (action) {
            is FollowNotificationAction.SetScreenOpen -> setScreenOpen(action.open)
            FollowNotificationAction.PreloadIfNeeded -> preloadNotificationsIfNeeded()
            FollowNotificationAction.LoadNotifications -> loadNotifications()
            FollowNotificationAction.MarkAllSeen -> markAllSeen()
        }
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _uiState.value = NotificationUiState.Loading
            try {
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                if (!uid.isNullOrBlank()) ensureWsConnected(uid)
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

    fun markAllSeen() {
        viewModelScope.launch {
            markAllSeenNow()
        }
    }

    suspend fun markAllSeenNow() {
        try {
            repository.seenAll()
        } catch (_: Exception) {
        }

        try {
            _notifications.value = repository.getNotifications(limit = 20, offset = 0)
            _unreadCount.value = repository.getUnreadCount()
        } catch (_: Exception) {
        }
    }

    private suspend fun refreshUnreadOnly() {
        _unreadCount.value = repository.getUnreadCount()
    }

    private suspend fun refreshListAndBadge() {
        _notifications.value = repository.getNotifications(limit = 20, offset = 0)
        _unreadCount.value = repository.getUnreadCount()
    }

    override fun onCleared() {
        super.onCleared()
        wsClient.disconnect()
    }
}

