package com.example.tiktok_clone.features.inbox.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktok_clone.features.inbox.data.ChatResponse
import com.example.tiktok_clone.features.inbox.data.InboxRepository
import com.example.tiktok_clone.features.inbox.data.MessageDto
import com.example.tiktok_clone.features.inbox.data.model.Message
import java.io.File
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InboxViewModel(
    private val inboxRepository: InboxRepository,
) : ViewModel() {

    private val _chats = MutableStateFlow<List<ChatResponse>>(emptyList())
    val chats: StateFlow<List<ChatResponse>> = _chats.asStateFlow()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

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
     * Load tin nhắn theo chatWithId:
     * - Nếu chatWithId là số (chatId) -> gọi GET messages theo chat_id.
     * - Nếu là otherUserId (Firebase UID) -> lấy danh sách chat, tìm chat có otherUserId, rồi load messages; không có thì để trống.
     */
    fun loadMessages(chatWithId: String) {
        if (chatWithId.isBlank()) {
            _messages.value = emptyList()
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val chatId = chatWithId.toIntOrNull()
                if (chatId != null) {
                    _messages.value = inboxRepository.getMessages(chatId = chatId)
                } else {
                    val res = inboxRepository.getChats(limit = 100, offset = 0)
                    val chat = res.chats.find { it.otherUserId == chatWithId }
                    _messages.value = if (chat != null) {
                        inboxRepository.getMessages(chatId = chat.chatId)
                    } else {
                        emptyList()
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Lỗi tải tin nhắn"
                _messages.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Gửi tin TEXT thuần bằng JSON body.
     */
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

    /**
     * Gửi tin nhắn kèm file (IMAGE / VIDEO) – dùng endpoint upload.
     * File đã được chuẩn bị sẵn (từ Uri hoặc camera) ở tầng UI.
     */
    fun sendMessageWithFile(
        otherUid: String,
        file: File,
        type: String,
        content: String? = null,
    ) {
        viewModelScope.launch {
            _error.value = null
            try {
                val sent = inboxRepository.sendMessageWithFile(
                    otherUid = otherUid,
                    file = file,
                    type = type,
                    content = content,
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
    fun getLastMessageMessage(dto: MessageDto?): Message? = dto?.let { inboxRepository.mapToMessage(it) }
}
