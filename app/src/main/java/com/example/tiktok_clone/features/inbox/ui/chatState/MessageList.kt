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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.features.inbox.data.model.Message
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.ui.theme.GrayBackground
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun MessageList(
    modifier: Modifier = Modifier,
    messages: List<Message> = emptyList(),
    chatWithUser: User,
    currentUser: String,
    onLoadMore: () -> Unit = {},
) {
    val listState = rememberLazyListState()
    var hasInitialScrollDone by remember { mutableStateOf(false) }

    val newestMessage = messages.firstOrNull()
    val newestMessageId = newestMessage?.id
    LaunchedEffect(newestMessageId, currentUser) {
        if (newestMessageId != null && messages.isNotEmpty()) {
            val shouldScroll = !hasInitialScrollDone || newestMessage.senderId == currentUser
            if (shouldScroll) {
                hasInitialScrollDone = true
                listState.animateScrollToItem(0)
                hasInitialScrollDone
            }
        } else if (messages.isEmpty()) {
            hasInitialScrollDone = false
            hasInitialScrollDone
        }
    }

    LaunchedEffect(listState, messages.size) {
        snapshotFlow {
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            lastVisibleIndex == messages.lastIndex && listState.isScrollInProgress
        }
            .distinctUntilChanged()
            .collect { atTopAndScrolling ->
                if (atTopAndScrolling) onLoadMore()
            }
    }
    val showLastMessage = remember(messages) {
        messages
            .mapIndexedNotNull { index, message ->
                val previousMessage = messages.getOrNull(index - 1)
                val isLastInGroup = previousMessage == null || previousMessage.senderId != message.senderId
                if (isLastInGroup) message.id else null
            }
            .toSet()
    }
    val lastCurrentUserMessageId = remember(messages, currentUser) {
        messages.firstOrNull { it.senderId == currentUser }?.id
    }
    LazyColumn(
        state = listState,
        reverseLayout = true,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
    ) {
        itemsIndexed(
            items = messages,
            key = { index, message ->
                message.id.ifBlank { "${message.senderId}_${message.timestamp}" } + "_$index"
            },
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
