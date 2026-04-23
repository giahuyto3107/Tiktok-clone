package com.example.tiktok_clone.features.inbox.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktok_clone.features.inbox.data.ChatResponse
import com.example.tiktok_clone.features.inbox.data.InboxRepository
import com.example.tiktok_clone.features.inbox.data.MessageDto
import com.example.tiktok_clone.features.inbox.data.model.InboxAction
import com.example.tiktok_clone.features.inbox.data.model.Message
import com.example.tiktok_clone.features.inbox.data.model.MessageStatus
import com.example.tiktok_clone.features.inbox.ui.InboxUiState
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.abs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InboxViewModel(
    private val repo: InboxRepository,
) : ViewModel() {

    //Config

    //Chat-list state
    private val _chats = MutableStateFlow<List<ChatResponse>>(emptyList())
    private val _chatsUiState = MutableStateFlow<InboxUiState<ChatResponse>>(InboxUiState.Loading)
    val chatsUiState: StateFlow<InboxUiState<ChatResponse>> = _chatsUiState.asStateFlow()

    //Message state
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    private val _messagesUiState =
        MutableStateFlow<InboxUiState<Message>>(InboxUiState.Success(items = emptyList()))
    val messagesUiState: StateFlow<InboxUiState<Message>> = _messagesUiState.asStateFlow()

    //Error 
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    //Active chat tracking 
    private var openChatWithId: String? = null   // otherUserId của chat đang mở
    private var openChatId: Int? = null          // chatId DB của chat đang mở

    //Public entry point

    // Dieu huong action inbox tu UI
    fun onAction(action: InboxAction) = when (action) {
        is InboxAction.LoadChats -> loadChats(silent = false)
        is InboxAction.LoadMessages -> loadMessages(action.chatWithId)
        is InboxAction.DisconnectChat -> disconnectChat()
        is InboxAction.SendTextMessage -> sendTextMessage(action.otherUid, action.content)
        is InboxAction.ClearError -> _error.value = null
    }

    //Chat list — silent: reload sau khi mở chat / gửi tin (không nháy Loading toàn màn).
    // Tai danh sach chat (co the silent)
    private fun loadChats(silent: Boolean = false) {
        viewModelScope.launch {
            if (!silent) _chatsUiState.value = InboxUiState.Loading
            _error.value = null
            try {
                val res = repo.getChats()
                val chats = res.chats.zeroPendingUnread()
                _chats.value = chats
                _chatsUiState.value = InboxUiState.Success(
                    items = chats,
                    unreadCount = chats.sumOf { it.unreadCount },
                )
            } catch (e: Exception) {
                val msg = e.message ?: "Lỗi tải danh sách chat"
                _error.value = msg
                if (!silent) _chatsUiState.value = InboxUiState.Error(msg)
            }
        }
    }

    //Messages 

    // Tai tin nhan theo userId dang chat
    private fun loadMessages(chatWithId: String) {
        if (chatWithId.isBlank()) {
            resetChatState(); return
        }
        viewModelScope.launch {
            _messagesUiState.value = InboxUiState.Loading
            _error.value = null
            openChatWithId = chatWithId
            zeroPendingUnreadFor(chatWithId)
            try {
                val chatId = resolveChatId(chatWithId) ?: run { resetChatState(); return@launch }
                openChatId = chatId

                _messages.value = repo.getMessages(chatId = chatId)

                publishMessages()
                loadChats(silent = true)
            } catch (e: Exception) {
                val msg = e.message ?: "Lỗi tải tin nhắn"
                _error.value = msg
                _messages.value = emptyList()
                _messagesUiState.value = InboxUiState.Error(msg)
            }
        }
    }

    // Reset active chat khi roi man chat
    fun disconnectChat() {
        openChatWithId = null
    }

    //Send

    // Gui tin nhan text va optimistic update
    private fun sendTextMessage(otherUid: String, content: String) {
        if (content.isBlank()) return

        val tempId = "temp_${System.currentTimeMillis()}"
        val tempMessage = Message(
            id = tempId,
            content = content,
            status = MessageStatus.SENDING,
            senderId = currentUserId(),
            timestamp = System.currentTimeMillis(),
            type = com.example.tiktok_clone.features.inbox.data.model.MessageType.TEXT,
        )

        _messages.value = listOf(tempMessage) + _messages.value

        viewModelScope.launch {
            runCatching { repo.sendMessage(otherUid, content) }
                .onSuccess { realMsg ->
                    _messages.value = _messages.value.map {
                        if (it.id == tempId) realMsg else it
                    }
                    publishMessages()
                }
                .onFailure {
                    // THẤT BẠI: Khôi phục danh sách, báo lỗi
                    _messages.value = _messages.value.filter { it.id != tempId }
                    publishMessages()
                    _error.value = "Gửi thất bại, mạng kém"

                }
        }
    }

    //Helpers 

    // Chèn tin vừa gửi vào đầu list ngay lập tức không chờ reload
    // Chen tin vua gui vao dau list tin nhan
    private fun appendSentMessage(message: Message, otherUid: String) {
        _messages.value = listOf(message) + _messages.value
        publishMessages()
        upsertChatSummary(message, otherUid, incrementUnread = false)
    }

    //Ép unread = 0 cho chat đang mở trong local state trước khi API trả về
    // Set unread = 0 cho chat dang mo trong local state
    private fun zeroPendingUnreadFor(chatWithId: String) {
        _chats.value = _chats.value.map { chat ->
            if (chat.otherUserId == chatWithId) chat.copy(unreadCount = 0) else chat
        }
        _chatsUiState.value = InboxUiState.Success(
            items = _chats.value,
            unreadCount = _chats.value.sumOf { it.unreadCount },
        )
    }

    // Zero unread cho chat dang mo trong list chats
    private fun List<ChatResponse>.zeroPendingUnread(): List<ChatResponse> {
        val openId = openChatWithId ?: return this
        return map { if (it.otherUserId == openId) it.copy(unreadCount = 0) else it }
    }

    //Cập nhật hoặc tạo mới ChatResponse summary trong lis
    // Cap nhat chat summary trong danh sach chat
    private fun upsertChatSummary(message: Message, otherUid: String, incrementUnread: Boolean) {
        val shouldAdd = incrementUnread
                && message.senderId != currentUserId()
                && otherUid != openChatWithId

        val existing = _chats.value.firstOrNull { it.otherUserId == otherUid }
        val updated = existing?.copy(
            lastMessage = message.toDto(),
            unreadCount = if (shouldAdd) existing.unreadCount + 1 else 0,
        ) ?: ChatResponse(
            chatId = openChatId ?: otherUid.toIntOrNull() ?: abs(otherUid.hashCode()),
            otherUserId = otherUid,
            lastMessage = message.toDto(),
            unreadCount = if (shouldAdd) 1 else 0,
        )

        _chats.value = buildList {
            add(updated)
            addAll(_chats.value.filter { it.otherUserId != otherUid })
        }
        _chatsUiState.value = InboxUiState.Success(
            items = _chats.value,
            unreadCount = _chats.value.sumOf { it.unreadCount },
        )
    }

    // Resolve chatId từ otherUserId (hoặc trả thẳng nếu input đã là só
    // Resolve chatId tu otherUid
    private suspend fun resolveChatId(chatWithId: String): Int? {
        chatWithId.toIntOrNull()?.let { return it }
        return repo.getChats(limit = 100, offset = 0).chats
            .find { it.otherUserId == chatWithId }?.chatId
    }

    // Publish messages sang uiState
    private fun publishMessages() {
        _messagesUiState.value = InboxUiState.Success(items = _messages.value)
    }

    // Reset state chat va messages
    private fun resetChatState() {
        _messages.value = emptyList()
        openChatId = null
        openChatWithId = null
        disconnectChat()
        publishMessages()
    }

    // InboxChatList convert MessageDto → Message cho lastMessage preview.
    // Convert lastMessage dto sang model
    fun getLastMessage(dto: MessageDto?): Message? = dto?.let { repo.mapToMessage(it) }

    // Lay current user id tu FirebaseAuth
    private fun currentUserId(): String =
        FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    //Message → DTO (chỉ dùng nội bộ cho upsertChatSummary) 

    // Convert Message sang dto de update chat summary
    private fun Message.toDto() = MessageDto(
        id = id.toIntOrNull() ?: 0,
        content = content.ifBlank { null },
        senderId = senderId,
        timestamp = timestamp,
        type = type.name,
        status = status.name,
        imageUri = imageUri,
        receiptStatus = receiptStatus?.name,
    )

    //Lifecycle
}