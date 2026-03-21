package com.example.tiktok_clone.features.inbox.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktok_clone.core.network.RealtimeWebSocketClient
import com.example.tiktok_clone.features.inbox.data.ChatResponse
import com.example.tiktok_clone.features.inbox.data.InboxRepository
import com.example.tiktok_clone.features.inbox.data.MessageDto
import com.example.tiktok_clone.features.inbox.data.model.Message
import java.io.File
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

class InboxViewModel(
    private val inboxRepository: InboxRepository,
    okHttpClient: OkHttpClient,
) : ViewModel() {

    private val pageSize = 15
    private var currentChatId: Int? = null
    private var currentOffset: Int = 0

    private val wsClient = RealtimeWebSocketClient(okHttpClient)

    private val _chats = MutableStateFlow<List<ChatResponse>>(emptyList())
    val chats: StateFlow<List<ChatResponse>> = _chats.asStateFlow()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _hasMoreMessages = MutableStateFlow(true)
    val hasMoreMessages: StateFlow<Boolean> = _hasMoreMessages.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private fun resetChatState(disconnectWs: Boolean = true) {
        _messages.value = emptyList()
        currentChatId = null
        currentOffset = 0
        _hasMoreMessages.value = false
        if (disconnectWs) disconnectChat()
    }

    fun loadChats(limit: Int? = null, offset: Int? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val res = inboxRepository.getChats(limit = limit, offset = offset)
                _chats.value = res.chats
            } catch (e: Exception) {
                _error.value = e.message ?: "Lỗi tải danh sách chat"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Load lần đầu và kết nối WebSocket cho chat.
     * Backend trả DESC → reversed() để hiển thị cũ→mới từ trên xuống dưới.
     */
    fun loadMessages(chatWithId: String) {
        if (chatWithId.isBlank()) {
            resetChatState(disconnectWs = true)
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val resolvedChatId = resolveChatId(chatWithId) ?: run {
                    resetChatState(disconnectWs = true)
                    return@launch
                }

                currentChatId = resolvedChatId
                currentOffset = 0

                val page = inboxRepository.getMessagesPage(
                    chatId = resolvedChatId,
                    limit = pageSize,
                    offset = 0,
                )

                _messages.value = page.messages.reversed()
                currentOffset = page.messages.size
                _hasMoreMessages.value = page.messages.size == pageSize

                // Cập nhật unreadCount về 0 cho chat list (backend đã mark SEEN khi GET messages)
                loadChats()
                connectChatWs(chatWithId, resolvedChatId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Lỗi tải tin nhắn"
                _messages.value = emptyList()
                _hasMoreMessages.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    /** Kéo lên trên để load thêm tin cũ hơn. */
    fun loadMoreMessages(chatWithId: String) {
        if (chatWithId.isBlank()) return
        if (_isLoadingMore.value || !_hasMoreMessages.value) return

        viewModelScope.launch {
            _isLoadingMore.value = true
            _error.value = null
            try {
                val resolvedChatId = currentChatId ?: resolveChatId(chatWithId) ?: run {
                    _hasMoreMessages.value = false
                    return@launch
                }
                currentChatId = resolvedChatId

                val page = inboxRepository.getMessagesPage(
                    chatId = resolvedChatId,
                    limit = pageSize,
                    offset = currentOffset,
                )

                if (page.messages.isEmpty()) _hasMoreMessages.value = false
                else {
                    _messages.value = page.messages.reversed() + _messages.value
                    currentOffset += page.messages.size
                    _hasMoreMessages.value = page.messages.size == pageSize
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Lỗi tải thêm tin nhắn"
            } finally {
                _isLoadingMore.value = false
            }
        }
    }

    /** Đóng WebSocket khi rời màn chat. */
    fun disconnectChat() {
        wsClient.disconnect()
    }

    /** Gửi tin TEXT. */
    fun sendTextMessage(otherUid: String, content: String) {
        if (content.isBlank()) return
        viewModelScope.launch {
            _error.value = null
            try {
                val sent = inboxRepository.sendMessage(
                    otherUid = otherUid,
                    content = content.ifBlank { null },
                )
                _messages.value += sent
            } catch (e: Exception) {
                _error.value = e.message ?: "Gửi tin nhắn thất bại"
            }
        }
    }

    /** Gửi tin nhắn kèm file (IMAGE / VIDEO). */
    fun sendMessageWithFile(
        otherUid: String,
        file: File,
        type: String,
        content: String? = null,
        mimeType: String? = null,
    ) {
        viewModelScope.launch {
            _error.value = null
            try {
                val sent = inboxRepository.sendMessageWithFile(
                    otherUid = otherUid,
                    file = file,
                    type = type,
                    content = content,
                    mimeType = mimeType,
                )
                _messages.value += sent
            } catch (e: Exception) {
                _error.value = e.message ?: "Gửi tin nhắn thất bại"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    /** Chuyển lastMessage (MessageDto) từ ChatResponse sang Message để hiển thị. */
    fun getLastMessageMessage(dto: MessageDto?): Message? =
        dto?.let { inboxRepository.mapToMessage(it) }

    override fun onCleared() {
        super.onCleared()
        wsClient.disconnect()
    }

    private fun connectChatWs(chatWithId: String, chatId: Int) {
        viewModelScope.launch {
            wsClient.disconnect()
                wsClient.connect("api/v1/ws/inbox/chats/$chatId") { event, _ ->
                    if (event == "message_created") {
                        viewModelScope.launch {
                            syncLatestMessage(chatWithId = chatWithId, chatId = chatId)
                        }
                    }
                }
        }
    }

    private suspend fun syncLatestMessage(chatWithId: String, chatId: Int) {
        try {
            val page = inboxRepository.getMessagesPage(
                chatId = chatId,
                limit = 1,
                offset = 0,
            )
            val latest = page.messages.firstOrNull() ?: return
            _messages.value = _messages.value
                .filterNot { it.id == latest.id } + latest
        } catch (_: Exception) {
            // fallback an toàn nếu realtime miss payload hoặc API tạm lỗi
            loadMessages(chatWithId)
        }
    }

    private suspend fun resolveChatId(chatWithId: String): Int? {
        val chatId = chatWithId.toIntOrNull()
        if (chatId != null) return chatId
        val res = inboxRepository.getChats(limit = 100, offset = 0)
        return res.chats.find { it.otherUserId == chatWithId }?.chatId
    }
}
