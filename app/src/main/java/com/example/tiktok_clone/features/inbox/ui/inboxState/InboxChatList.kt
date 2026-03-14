package com.example.tiktok_clone.features.inbox.ui.inboxState

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.features.inbox.data.ChatResponse
import com.example.tiktok_clone.features.inbox.data.model.Message
import com.example.tiktok_clone.features.inbox.data.model.MessageStatus
import com.example.tiktok_clone.features.inbox.data.model.MessageType
import com.example.tiktok_clone.features.inbox.viewmodel.InboxViewModel
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import com.example.tiktok_clone.ui.theme.Purple
import com.example.tiktok_clone.ui.theme.TikTokCyanDark
import com.example.tiktok_clone.ui.theme.TikTokRed
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Heart

private val placeholderLastMessage = Message(
    id = "",
    content = "",
    senderId = "",
    timestamp = 0L,
    type = MessageType.TEXT,
    status = MessageStatus.SENT,
    imageUri = null,
)

@Composable
fun InboxChatList(
    currentUser: User,
    chats: List<ChatResponse>,
    inboxViewModel: InboxViewModel,
    socialViewModel: SocialViewModel,
    onChatClick: (userId: String) -> Unit = {}
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            InboxFriendList(currentUser, onChatClick = onChatClick)
        }
        item {
            InboxNotiItem(
                icon = Icons.Filled.Group,
                iconSize = 36.dp,
                bgColor = TikTokCyanDark,
                notiType = "Những Follower mới",
                notiContent = "Thêm bạn bè"
            )
        }
        item {
            InboxNotiItem(
                icon = FontAwesomeIcons.Solid.Heart,
                iconSize = 28.dp,
                bgColor = TikTokRed,
                notiType = "Hoạt động",
                notiContent = "Thêm bạn bè"
            )
        }
        item {
            InboxNotiItem(
                icon = Icons.Filled.Notifications,
                iconSize = 36.dp,
                bgColor = Purple,
                notiType = "Thông báo hệ thống",
                notiContent = "Thêm bạn bè"
            )
        }
        items(chats, key = { it.chatId }) { chat ->
            val chatWith = socialViewModel.getUser(chat.otherUserId)
            val lastMessage =
                inboxViewModel.getLastMessageMessage(chat.lastMessage) ?: placeholderLastMessage
            InboxChatLine(
                chatWith = chatWith,
                message = lastMessage,
                currentUserId = currentUser.id,
                onChatClick = { onChatClick(chat.otherUserId) },
            )
        }
    }
}
