package com.example.tiktok_clone.features.inbox.ui.inboxState

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.features.inbox.data.ChatResponse
import com.example.tiktok_clone.features.inbox.data.model.Message
import com.example.tiktok_clone.features.inbox.data.model.MessageStatus
import com.example.tiktok_clone.features.inbox.data.model.MessageType
import com.example.tiktok_clone.features.inbox.viewmodel.InboxViewModel
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.features.social.viewModel.FollowNotificationViewModel
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import com.example.tiktok_clone.ui.theme.TikTokCyanDark
import com.example.tiktok_clone.ui.theme.TikTokRed
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Heart
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.tiktok_clone.features.home.viewmodel.HomeViewModel
import com.example.tiktok_clone.features.post.data.model.PostType
import com.example.tiktok_clone.features.social.data.model.FollowNotificationReceiptStatus
import com.example.tiktok_clone.features.social.data.model.NotificationActionType
import com.example.tiktok_clone.features.social.data.model.NotificationReceiptStatus
import com.example.tiktok_clone.features.social.viewModel.NotificationViewModel

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
    onChatClick: (userId: String) -> Unit = {},
    onUserNotificationClick: () -> Unit = {},
    onSocialNotificationClick: () -> Unit = {},
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            InboxFriendList(currentUser, onChatClick = onChatClick)
        }
        item {
            val flvm: FollowNotificationViewModel = koinViewModel()
            LaunchedEffect(Unit) { flvm.loadNotifications() }

            val lastNoti by flvm.notifications.collectAsState()
            val firstNoti = lastNoti.firstOrNull()
            val user = firstNoti?.let { socialViewModel.getUser(it.followerId) }
            val notiContent = user?.let { "${it.userName} đã theo dõi bạn" } ?: "Chưa có thông báo"

            InboxNotiItem(
                icon = Icons.Filled.Group,
                iconSize = 36.dp,
                bgColor = TikTokCyanDark,
                notiType = "Những Follower mới",
                notiContent = notiContent,
                isNew = firstNoti?.receiptStatus == FollowNotificationReceiptStatus.DELIVERED,
                onNotiClick = onUserNotificationClick
            )
        }
        item {
            val notiVM: NotificationViewModel = koinViewModel()
            val homeViewModel: HomeViewModel = koinViewModel()
            LaunchedEffect(Unit) { notiVM.loadNotifications() }
            val posts by homeViewModel.posts.collectAsState()
            val lastSocialNoti = notiVM.notifications.collectAsState().value.firstOrNull()
            val socialUser = lastSocialNoti?.let { socialViewModel.getUser(it.fromUserId) }
            val socialPost = posts.find { it.id == lastSocialNoti?.postId }
            val socialPostType = if (socialPost?.type == PostType.VIDEO) "video" else "ảnh"
            val socialNotiContent = socialUser?.let {
                "${it.userName} ${when (lastSocialNoti.actionType) {
                    NotificationActionType.LIKE -> "đã thích $socialPostType của bạn"
                    NotificationActionType.COMMENT -> "đã bình luận về $socialPostType của bạn"
                    NotificationActionType.COMMENT_REPLY -> "đã trả lời bình luận của bạn"
                    NotificationActionType.COMMENT_LIKE -> "đã thích bình luận của bạn"
                    NotificationActionType.REPOST -> "đã đăng lại cùng một bài đăng"
                }}"
            } ?: "Chưa có thông báo"

            InboxNotiItem(
                icon = FontAwesomeIcons.Solid.Heart,
                iconSize = 28.dp,
                bgColor = TikTokRed,
                notiType = "Hoạt động",
                notiContent = socialNotiContent,
                isNew = lastSocialNoti?.receiptStatus == NotificationReceiptStatus.DELIVERED,
                onNotiClick = onSocialNotificationClick,
            )
        }
//        item {
//            InboxNotiItem(
//                icon = Icons.Filled.Notifications,
//                iconSize = 36.dp,
//                bgColor = Purple,
//                notiType = "Thông báo hệ thống",
//                notiContent = "",
//                onNotiClick = { }
//            )
//        }
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
