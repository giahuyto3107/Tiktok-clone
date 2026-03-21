package com.example.tiktok_clone.features.social.ui.notification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.features.home.viewmodel.HomeViewModel
import com.example.tiktok_clone.features.post.data.model.PostType
import com.example.tiktok_clone.features.social.data.model.FollowNotification
import com.example.tiktok_clone.features.social.data.model.Notification
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NotificationList(
    notificationType: String = "social",
    socialNotifications: List<Notification> = emptyList(),
    followNotifications: List<FollowNotification> = emptyList(),
    socialViewModel: SocialViewModel = koinViewModel(),
    homeViewModel: HomeViewModel = koinViewModel(),
) {
    val posts by homeViewModel.posts.collectAsState()
    val postById = remember(posts) { posts.associateBy { it.id } }
    val socialUsers = remember(socialNotifications) {
        socialNotifications
            .asSequence()
            .map { it.fromUserId }
            .distinct()
            .associateWith { socialViewModel.getUser(it) }
    }
    val followUsers = remember(followNotifications) {
        followNotifications
            .asSequence()
            .map { it.followerId }
            .distinct()
            .associateWith { socialViewModel.getUser(it) }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (notificationType == "social") {
            items(
                items = socialNotifications,
                key = { it.id },
            ) { socialNotification ->
                val fromUser = socialUsers[socialNotification.fromUserId]
                val fromPost = postById[socialNotification.postId]
                NotificationLine(
                    socialNotification = socialNotification,
                    notificationType = notificationType,
                    fromUserName = fromUser?.userName ?: socialNotification.fromUserId,
                    fromAvatarUrl = fromUser?.avatarUrl.orEmpty(),
                    postMediaUrl = fromPost?.mediaUrl,
                    postType = if (fromPost?.type == PostType.VIDEO) "video" else "ảnh",
                )
            }
        } else {
            items(
                items = followNotifications,
                key = { it.id },
            ) { followNotification ->
                val fromUser = followUsers[followNotification.followerId]
                NotificationLine(
                    followNotification = followNotification,
                    notificationType = notificationType,
                    fromUserName = fromUser?.userName ?: followNotification.followerId,
                    fromAvatarUrl = fromUser?.avatarUrl.orEmpty(),
                )
            }
        }
    }
}