package com.example.tiktok_clone.features.social.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktok_clone.core.network.RealtimeWebSocketClient
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

    private val wsClient = RealtimeWebSocketClient(okHttpClient)
    private var connectedUid: String? = null

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications.asStateFlow()

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Tránh reload list liên tục khi WS gửi dồn event.
    private var wsReloadInFlight: Boolean = false
    private var hasLoadedOnce: Boolean = false

    fun preloadNotificationsIfNeeded() {
        if (hasLoadedOnce || _isLoading.value) return
        loadNotifications()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                if (!uid.isNullOrBlank()) ensureWsConnected(uid)

                _notifications.value = repository.getNotifications()
                _unreadCount.value = repository.getUnreadCount()
                hasLoadedOnce = true
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _error.value = e.message ?: "Lỗi tải thông báo"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun ensureWsConnected(uid: String) {
        if (connectedUid == uid) return
        connectedUid = uid
        viewModelScope.launch {
            wsClient.disconnect()
            wsClient.connect("api/v1/ws/social/users/$uid") { event, _ ->
                // Callback chạy trên IO thread
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
                _notifications.value = repository.getNotifications()
                _unreadCount.value = repository.getUnreadCount()
            } catch (_: Exception) {
                // Không làm gì nếu lỗi để tránh crash.
            } finally {
                wsReloadInFlight = false
            }
        }
    }

    fun clearError() {
        _error.value = null
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
            _notifications.value = repository.getNotifications()
            _unreadCount.value = repository.getUnreadCount()
        } catch (_: Exception) {
            // im lặng
        }
    }

    // Để giữ code đơn giản, realtime chỉ trigger reload từ REST thay vì parse payload WS.

    override fun onCleared() {
        super.onCleared()
        wsClient.disconnect()
    }
}
