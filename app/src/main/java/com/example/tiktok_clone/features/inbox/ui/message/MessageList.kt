package com.example.tiktok_clone.features.inbox.ui.message

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.features.inbox.data.model.Message
import com.example.tiktok_clone.features.social.data.model.User

@Composable
// Render list tin nhan trong man chat
fun MessageList(
    modifier: Modifier = Modifier,
    messages: List<Message> = emptyList(),
    chatWithUser: User,
    currentUser: String,
) {
    val outgoingTopId = messages.firstOrNull()
        ?.takeIf { it.senderId == currentUser }
        ?.id
    val listState = remember(chatWithUser.id, outgoingTopId) { LazyListState() }

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
