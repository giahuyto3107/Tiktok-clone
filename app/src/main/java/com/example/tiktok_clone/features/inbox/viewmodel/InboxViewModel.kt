package com.example.tiktok_clone.features.inbox.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktok_clone.core.network.RealtimeWebSocketClient
import com.example.tiktok_clone.features.inbox.data.ChatResponse
import com.example.tiktok_clone.features.inbox.data.InboxRepository
import com.example.tiktok_clone.features.inbox.data.MessageDto
import com.example.tiktok_clone.features.inbox.data.model.InboxAction
import com.example.tiktok_clone.features.inbox.data.model.Message
import com.example.tiktok_clone.features.inbox.ui.InboxUiState
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import kotlin.math.abs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.json.JSONObject

class InboxViewModel(
    private val repo: InboxRepository,
    okHttpClient: OkHttpClient,
) : ViewModel() {

    //Config 
    private val pageSize = 15

    //Chat-list state
    private val _chats = MutableStateFlow<List<ChatResponse>>(emptyList())
    private val _chatsUiState = MutableStateFlow<InboxUiState<ChatResponse>>(InboxUiState.Loading)
    val chatsUiState: StateFlow<InboxUiState<ChatResponse>> = _chatsUiState.asStateFlow()

    //Message state
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    private val _messagesUiState = MutableStateFlow<InboxUiState<Message>>(
        InboxUiState.Success(items = emptyList(), hasMore = false)
    )
    val messagesUiState: StateFlow<InboxUiState<Message>> = _messagesUiState.asStateFlow()

    //Error 
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    //Pagination
    private var currentOffset = 0
    private var hasMoreMessages = false
    private var isLoadingMore = false

    //Active chat tracking 
    private var openChatWithId: String? = null   // otherUserId của chat đang mở
    private var openChatId: Int? = null          // chatId DB của chat đang mở

    //WebSocket 
    private val chatWsClient = RealtimeWebSocketClient(okHttpClient)
    private val userInboxWsClient = RealtimeWebSocketClient(okHttpClient)
    private var connectedInboxUid: String? = null
    private var wsReloadInFlight = false

    //Public entry point

    fun onAction(action: InboxAction) = when (action) {
        is InboxAction.LoadChats -> loadChats(silent = false)
        is InboxAction.LoadMessages -> loadMessages(action.chatWithId)
        is InboxAction.LoadMoreMessages -> loadMoreMessages(action.chatWithId)
        is InboxAction.DisconnectChat -> disconnectChat()
        is InboxAction.SendTextMessage -> sendTextMessage(action.otherUid, action.content)
        is InboxAction.SendMediaMessage -> sendMediaMessage(
            action.otherUid,
            action.imageUri,
            action.type,
            action.content
        )

        is InboxAction.SendMessageWithFile -> sendMessageWithFile(
            action.otherUid,
            action.file,
            action.type,
            action.content,
            action.mimeType
        )

        is InboxAction.ClearError -> _error.value = null
    }

    //User-level WebSocket (inbox badge / unread realtime) 

    fun ensureUserInboxWsConnected(uid: String) {
        if (uid.isBlank() || connectedInboxUid == uid) return
        connectedInboxUid = uid
        viewModelScope.launch {
            userInboxWsClient.disconnect()
            userInboxWsClient.connect("api/v1/ws/inbox/users/$uid") { event, rawJson ->
                onUserInboxWsEvent(event, rawJson)
            }
        }
    }

    private fun onUserInboxWsEvent(event: String, rawJson: String) {
        // Strict contract: chỉ xử lý inbox_message_created từ user-level WS.
        if (event != "inbox_message_created") return
        val payload = parseInboxCreatedPayload(rawJson) ?: return
        if (wsReloadInFlight) return

        wsReloadInFlight = true
        viewModelScope.launch {
            try {
                loadChats(silent = true)
                // Nếu đang mở đúng chat nhận tin → sync thêm message mới vào list
                val isCurrentChat = openChatWithId != null && openChatId != null &&
                        (payload.chatId == openChatId || payload.otherUserId == openChatWithId)
                if (isCurrentChat) syncLatestMessage(openChatWithId!!, openChatId!!)
            } finally {
                wsReloadInFlight = false
            }
        }
    }

    //Chat list
     // @param silent = true: reload không show loading indicator (dùng cho WS refresh).
     // @param silent = false: show Loading state bình thường (dùng khi mở màn hình).
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
                currentOffset = 0

                val page = repo.getMessagesPage(chatId = chatId, limit = pageSize, offset = 0)
                _messages.value = page.messages
                currentOffset = page.messages.size
                hasMoreMessages = page.messages.size == pageSize

                publishMessages()
                loadChats(silent = true)
                connectChatWs(chatWithId, chatId)
            } catch (e: Exception) {
                val msg = e.message ?: "Lỗi tải tin nhắn"
                _error.value = msg
                _messages.value = emptyList()
                hasMoreMessages = false
                _messagesUiState.value = InboxUiState.Error(msg)
            }
        }
    }

    private fun loadMoreMessages(chatWithId: String) {
        if (chatWithId.isBlank() || isLoadingMore || !hasMoreMessages) return
        viewModelScope.launch {
            isLoadingMore = true
            publishMessages() // cập nhật isLoadingMore = true lên UI
            _error.value = null
            try {
                val chatId = openChatId ?: resolveChatId(chatWithId) ?: run {
                    hasMoreMessages = false
                    return@launch
                }
                openChatId = chatId

                val page =
                    repo.getMessagesPage(chatId = chatId, limit = pageSize, offset = currentOffset)
                if (page.messages.isEmpty()) {
                    hasMoreMessages = false
                } else {
                    _messages.value += page.messages
                    currentOffset += page.messages.size
                    hasMoreMessages = page.messages.size == pageSize
                }
            } catch (e: Exception) {
                val msg = e.message ?: "Lỗi tải thêm tin nhắn"
                _error.value = msg
                _messagesUiState.value = InboxUiState.Error(msg)
            } finally {
                isLoadingMore = false
                if (_messagesUiState.value !is InboxUiState.Error) publishMessages()
            }
        }
    }

    //Chat-level WebSocket 

    private fun connectChatWs(chatWithId: String, chatId: Int) {
        viewModelScope.launch {
            chatWsClient.disconnect()
            chatWsClient.connect("api/v1/ws/inbox/chats/$chatId") { event, _ ->
                if (event == "message_created") {
                    viewModelScope.launch { syncLatestMessage(chatWithId, chatId) }
                }
            }
        }
    }

    // Lấy 1 tin mới nhất từ API rồi upsert vào list hiện tại (tránh duplicate)
    private suspend fun syncLatestMessage(chatWithId: String, chatId: Int) {
        try {
            val latest = repo.getMessagesPage(chatId = chatId, limit = 1, offset = 0)
                .messages.firstOrNull() ?: return
            _messages.value = listOf(latest) + _messages.value.filterNot { it.id == latest.id }
            publishMessages()
            upsertChatSummary(
                latest,
                chatWithId,
                incrementUnread = latest.senderId != currentUserId()
            )
        } catch (_: Exception) {
            // fallback: reload toàn bộ nếu sync lẻ thất bại
            loadMessages(chatWithId)
        }
    }

    fun disconnectChat() {
        openChatWithId = null
        chatWsClient.disconnect()
    }

    //Send

    private fun sendTextMessage(otherUid: String, content: String) {
        if (content.isBlank()) return
        viewModelScope.launch {
            runCatching { repo.sendMessage(otherUid, content) }
                .onSuccess { appendSentMessage(it, otherUid) }
                .onFailure { _error.value = it.message ?: "Gửi tin nhắn thất bại" }
        }
    }

    private fun sendMediaMessage(
        otherUid: String,
        imageUri: String,
        type: String,
        content: String?
    ) {
        if (imageUri.isBlank()) return
        viewModelScope.launch {
            runCatching {
                repo.sendMediaMessage(
                    otherUid,
                    imageUri,
                    type,
                    content?.ifBlank { null })
            }
                .onSuccess { appendSentMessage(it, otherUid) }
                .onFailure { _error.value = it.message ?: "Gửi tin nhắn thất bại" }
        }
    }

    private fun sendMessageWithFile(
        otherUid: String,
        file: File,
        type: String,
        content: String?,
        mimeType: String?
    ) {
        viewModelScope.launch {
            runCatching { repo.sendMessageWithFile(otherUid, file, type, content, mimeType) }
                .onSuccess { appendSentMessage(it, otherUid) }
                .onFailure { _error.value = it.message ?: "Gửi tin nhắn thất bại" }
        }
    }

    //Helpers 

    // Chèn tin vừa gửi vào đầu list ngay lập tức không chờ reload
    private fun appendSentMessage(message: Message, otherUid: String) {
        _messages.value = listOf(message) + _messages.value
        publishMessages()
        upsertChatSummary(message, otherUid, incrementUnread = false)
    }

    //Ép unread = 0 cho chat đang mở trong local state trước khi API trả về
    private fun zeroPendingUnreadFor(chatWithId: String) {
        _chats.value = _chats.value.map { chat ->
            if (chat.otherUserId == chatWithId) chat.copy(unreadCount = 0) else chat
        }
        _chatsUiState.value = InboxUiState.Success(
            items = _chats.value,
            unreadCount = _chats.value.sumOf { it.unreadCount },
        )
    }

    private fun List<ChatResponse>.zeroPendingUnread(): List<ChatResponse> {
        val openId = openChatWithId ?: return this
        return map { if (it.otherUserId == openId) it.copy(unreadCount = 0) else it }
    }

    //Cập nhật hoặc tạo mới ChatResponse summary trong lis
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
    private suspend fun resolveChatId(chatWithId: String): Int? {
        chatWithId.toIntOrNull()?.let { return it }
        return repo.getChats(limit = 100, offset = 0).chats
            .find { it.otherUserId == chatWithId }?.chatId
    }

    // đẩy tin nhắn lên7
    private fun publishMessages() {
        _messagesUiState.value = InboxUiState.Success(
            items = _messages.value,
            isLoadingMore = isLoadingMore,
            hasMore = hasMoreMessages,
        )
    }

    private fun resetChatState() {
        _messages.value = emptyList()
        openChatId = null
        openChatWithId = null
        currentOffset = 0
        hasMoreMessages = false
        isLoadingMore = false
        disconnectChat()
        publishMessages()
    }

    // InboxChatList convert MessageDto → Message cho lastMessage preview.
    fun getLastMessage(dto: MessageDto?): Message? = dto?.let { repo.mapToMessage(it) }

    private fun currentUserId(): String =
        FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    //Message → DTO (chỉ dùng nội bộ cho upsertChatSummary) 

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

    //WS payload

    private data class InboxCreatedPayload(
        val chatId: Int,
        val otherUserId: String,
        val messageId: String
    )

    private fun parseInboxCreatedPayload(rawJson: String): InboxCreatedPayload? = try {
        val data = JSONObject(rawJson).optJSONObject("data") ?: return null
        val chatId = data.optInt("chatId", -1).takeIf { it >= 0 } ?: return null
        val otherUserId = data.optString("otherUserId").takeIf { it.isNotBlank() } ?: return null
        val messageId = data.opt("messageId")?.toString()?.takeIf { it.isNotBlank() } ?: return null
        InboxCreatedPayload(chatId, otherUserId, messageId)
    } catch (_: Exception) {
        null
    }

    //Lifecycle 

    override fun onCleared() {
        super.onCleared()
        chatWsClient.disconnect()
        userInboxWsClient.disconnect()
    }
}