package com.example.tiktok_clone.features.inbox.ui.inboxState

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.features.home.viewmodel.HomeViewModel
import com.example.tiktok_clone.features.inbox.data.ChatResponse
import com.example.tiktok_clone.features.inbox.viewmodel.InboxViewModel
import com.example.tiktok_clone.features.inbox.ui.components.buildFollowNotificationText
import com.example.tiktok_clone.features.inbox.ui.components.buildSocialNotificationText
import com.example.tiktok_clone.features.inbox.ui.components.resolveLastMessage
import com.example.tiktok_clone.features.notification.data.model.FollowNotificationAction
import com.example.tiktok_clone.features.notification.data.model.SocialNotificationAction
import com.example.tiktok_clone.features.notification.viewModel.FollowNotificationViewModel
import com.example.tiktok_clone.features.notification.viewModel.NotificationViewModel
import com.example.tiktok_clone.features.social.data.model.FollowNotificationReceiptStatus
import com.example.tiktok_clone.features.social.data.model.NotificationReceiptStatus
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import com.example.tiktok_clone.ui.theme.TikTokCyanDark
import com.example.tiktok_clone.ui.theme.TikTokRed
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Heart
import org.koin.androidx.compose.koinViewModel

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
    val homeViewModel: HomeViewModel = koinViewModel()
    val posts by homeViewModel.posts.collectAsState()

    val socialNotiVM: NotificationViewModel = koinViewModel()
    val latestSocialNoti = socialNotiVM.notifications.collectAsState().value.firstOrNull()
    val socialActor = latestSocialNoti?.let { socialViewModel.getUser(it.fromUserId) }

    val followNotiVM: FollowNotificationViewModel = koinViewModel()
    val latestFollowNoti = followNotiVM.notifications.collectAsState().value.firstOrNull()
    val follower = latestFollowNoti?.let { socialViewModel.getUser(it.followerId) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            InboxFriendList(currentUser, onChatClick = onChatClick)
        }

        item {
            LaunchedEffect(Unit) { followNotiVM.onAction(FollowNotificationAction.LoadNotifications) }

            InboxNotiItem(
                icon = Icons.Filled.Group,
                iconSize = 36.dp,
                bgColor = TikTokCyanDark,
                notiType = "Nhung Follower moi",
                notiContent = buildFollowNotificationText(follower, latestFollowNoti),
                isNew = latestFollowNoti?.receiptStatus == FollowNotificationReceiptStatus.DELIVERED,
                onNotiClick = onUserNotificationClick,
            )
        }

        item {
            LaunchedEffect(Unit) { socialNotiVM.onAction(SocialNotificationAction.LoadNotifications) }

            val post = posts.find { it.id == latestSocialNoti?.postId }

            InboxNotiItem(
                icon = FontAwesomeIcons.Solid.Heart,
                iconSize = 28.dp,
                bgColor = TikTokRed,
                notiType = "Hoat dong",
                notiContent = buildSocialNotificationText(
                    actor = socialActor,
                    latestNotification = latestSocialNoti,
                    postType = post?.type,
                ),
                isNew = latestSocialNoti?.receiptStatus == NotificationReceiptStatus.DELIVERED,
                onNotiClick = onSocialNotificationClick,
            )
        }

        items(chats, key = { it.chatId }) { chat ->
            val chatWith = socialViewModel.getUser(chat.otherUserId)
            val lastMessage = resolveLastMessage(chat, inboxViewModel::getLastMessage)

            InboxChatLine(
                chatWith = chatWith,
                message = lastMessage,
                unreadCount = chat.unreadCount,
                currentUserId = currentUser.id,
                onChatClick = { onChatClick(chat.otherUserId) },
            )
        }
    }
}
