package com.example.tiktok_clone.core.network

import com.example.tiktok_clone.core.config.ApiConfig
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

/**
 * Quản lý một WebSocket connection.
 * Mỗi ViewModel giữ một instance riêng.
 *
 * Cách dùng:
 *   wsClient.connect("api/v1/ws/inbox/chats/123") { event, json -> ... }
 *   wsClient.disconnect()
 */
class RealtimeWebSocketClient(private val okHttpClient: OkHttpClient) {

    private var socket: WebSocket? = null

    /**
     * Mở kết nối tới [path] (không cần dấu / đầu).
     * Token Firebase tự động gắn vào query param.
     * [onEvent] gọi trên IO thread → ViewModel phải dùng viewModelScope.launch nếu cần.
     */
    suspend fun connect(path: String, onEvent: (event: String, rawJson: String) -> Unit) {
        val token = getFirebaseToken()
        val url = "${ApiConfig.getWsBaseUrl()}$path?token=$token"
        val request = Request.Builder().url(url).build()

        socket = okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val event = JSONObject(text).optString("event", "")
                    if (event.isNotEmpty()) onEvent(event, text)
                } catch (_: Exception) { }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                // Có thể thêm retry sau nếu cần
            }
        })
    }

    fun disconnect() {
        socket?.close(1000, "disconnect")
        socket = null
    }

    private suspend fun getFirebaseToken(): String = try {
        FirebaseAuth.getInstance().currentUser
            ?.getIdToken(false)?.await()?.token ?: ""
    } catch (_: Exception) { "" }
}
