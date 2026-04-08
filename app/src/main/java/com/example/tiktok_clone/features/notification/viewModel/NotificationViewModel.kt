package com.example.tiktok_clone.features.notification.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktok_clone.core.network.RealtimeWebSocketClient
import com.example.tiktok_clone.features.notification.data.model.SocialNotificationAction
import com.example.tiktok_clone.features.notification.ui.NotificationUiState
import com.example.tiktok_clone.features.social.data.NotificationRepository
import com.example.tiktok_clone.features.social.data.model.Notification
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

class NotificationViewModel(
    private val repository: NotificationRepository,
    okHttpClient: OkHttpClient,
) : ViewModel() {
    private val _uiState = MutableStateFlow<NotificationUiState<Notification>>(
        NotificationUiState.Loading
    )
    val uiState: StateFlow<NotificationUiState<Notification>> = _uiState.asStateFlow()

    private val wsClient = RealtimeWebSocketClient(okHttpClient)
    private var connectedUid: String? = null

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications.asStateFlow()

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()

    private val _isLoading = MutableStateFlow(false)

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Tranh reload list lien tuc khi WS gui don event
    private var wsReloadInFlight: Boolean = false
    private var hasLoadedOnce: Boolean = false

    // preload thong bao lan dau neu chua co du lieu
    fun preloadNotificationsIfNeeded() {
        if (hasLoadedOnce || _isLoading.value) return
        loadNotifications()
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
        if (event != "notification_created" && event != "notification_updated") return
        if (wsReloadInFlight) return
        wsReloadInFlight = true
        viewModelScope.launch {
            try {
                refreshListAndBadge()
            } catch (_: Exception) {
                // im lặng
            } finally {
                wsReloadInFlight = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun onAction(action: SocialNotificationAction) {
        when (action) {
            SocialNotificationAction.PreloadIfNeeded -> preloadNotificationsIfNeeded()
            SocialNotificationAction.LoadNotifications -> loadNotifications()
            SocialNotificationAction.MarkAllSeen -> markAllSeen()
            SocialNotificationAction.ClearError -> clearError()
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
            // im lặng
        }
        try {
            refreshListAndBadge()
        } catch (_: Exception) {
            // im lặng
        }
    }

    private suspend fun refreshListAndBadge() {
        _notifications.value = repository.getNotifications()
        _unreadCount.value = repository.getUnreadCount()
    }

    override fun onCleared() {
        super.onCleared()
        wsClient.disconnect()
    }
}

