package com.example.tiktok_clone.features.inbox.ui.chatState

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.features.inbox.data.model.Message
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.ui.theme.GrayBackground
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop

@Composable
fun MessageList(
    modifier: Modifier = Modifier,
    messages: List<Message> = emptyList(),
    chatWithUser: User,
    currentUser: String,
    onLoadMore: () -> Unit = {},
) {
    val listState = rememberLazyListState()

    // Chỉ scroll xuống đáy khi tin nhắn CUỐI cùng thay đổi (tin mới gửi/nhận).
    // Khi prepend tin cũ (load more), lastMessageId không đổi → không scroll.
    val lastMessageId = messages.lastOrNull()?.id
    LaunchedEffect(lastMessageId) {
        if (lastMessageId != null) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    // Trigger load more khi user kéo lên tới đầu list.
    // drop(1): bỏ qua emit đầu tiên ngay lúc compose (index = 0 lúc mở màn).
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .drop(1)
            .collect { index ->
                if (index == 0) onLoadMore()
            }
    }
    val showLastMessage = remember(messages) {
        messages
            .mapIndexedNotNull { index, message ->
                val nextMessage = messages.getOrNull(index + 1)
                val isLastInGroup = nextMessage == null || nextMessage.senderId != message.senderId
                if (isLastInGroup) message.id else null
            }
            .toSet()
    }
    val lastCurrentUserMessageId = remember(messages, currentUser) {
        messages.lastOrNull { it.senderId == currentUser }?.id
    }
    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(GrayBackground),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
    ) {
        itemsIndexed(
            items = messages,
            key = { _, message -> message.id },
        ) { index, message ->
            val prev = messages.getOrNull(index - 1)

            if (prev?.senderId != message.senderId && index != 0) // tạo dòng để phân biệt tin nhắn user và other
                Spacer(modifier = Modifier.height(16.dp))

            Messageline(
                message = message,
                isCurrentUser = message.senderId == currentUser,
                chatWithUser = chatWithUser,
                isLastMessage = message.id in showLastMessage,
                isLastMessageInList = message.id == lastCurrentUserMessageId,
            )
        }
    }

}