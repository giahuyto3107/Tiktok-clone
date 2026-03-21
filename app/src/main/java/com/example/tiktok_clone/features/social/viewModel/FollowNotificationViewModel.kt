package com.example.tiktok_clone.features.social.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktok_clone.core.network.RealtimeWebSocketClient
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

    private val wsClient = RealtimeWebSocketClient(okHttpClient)
    private var connectedUid: String? = null

    private val _notifications = MutableStateFlow<List<FollowNotification>>(emptyList())
    val notifications: StateFlow<List<FollowNotification>> = _notifications.asStateFlow()

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var isScreenOpen: Boolean = false
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
                if (event == "follow_notification_created" || event == "follow_notification_updated") {
                    viewModelScope.launch {
                        if (isScreenOpen) refreshListAndBadge() else refreshUnreadOnly()
                    }
                }
            }
        }
    }

    fun setScreenOpen(open: Boolean) {
        isScreenOpen = open
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                if (!uid.isNullOrBlank()) ensureWsConnected(uid)

                _notifications.value = repository.getNotifications(limit = 20, offset = 0)
                _unreadCount.value = repository.getUnreadCount()
                hasLoadedOnce = true
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
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