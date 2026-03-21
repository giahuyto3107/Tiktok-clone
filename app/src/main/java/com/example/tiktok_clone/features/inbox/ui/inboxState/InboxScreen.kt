package com.example.tiktok_clone.features.inbox.ui.inboxState

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.setValue
import com.example.tiktok_clone.features.inbox.viewmodel.InboxViewModel
import com.example.tiktok_clone.features.social.viewModel.FollowNotificationViewModel
import com.example.tiktok_clone.features.social.viewModel.NotificationViewModel
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.compose.koinViewModel

@Composable
fun InboxScreen(
    onChatClick: (userId: String) -> Unit = {},
    onUserNotificationClick: () -> Unit = {},
    onSocialNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
) {
    val inboxViewModel: InboxViewModel = koinViewModel()
    val socialViewModel: SocialViewModel = koinViewModel()
    val notificationViewModel: NotificationViewModel = koinViewModel()
    val followNotificationViewModel: FollowNotificationViewModel = koinViewModel()
    val chats by inboxViewModel.chats.collectAsState()
    val auth = FirebaseAuth.getInstance()
    var currentUserId by remember { mutableStateOf(auth.currentUser?.uid.orEmpty()) }

    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener { fbAuth ->
            currentUserId = fbAuth.currentUser?.uid.orEmpty()
        }
        auth.addAuthStateListener(listener)
        onDispose { auth.removeAuthStateListener(listener) }
    }

    LaunchedEffect(currentUserId) {
        if (currentUserId.isNotBlank()) {
            inboxViewModel.loadChats()
            notificationViewModel.preloadNotificationsIfNeeded()
            followNotificationViewModel.preloadNotificationsIfNeeded()
        }
    }

    if (currentUserId.isBlank()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val currentUser = socialViewModel.getUser(currentUserId)
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        InboxHeader()
        InboxChatList(
            currentUser = currentUser,
            chats = chats,
            inboxViewModel = inboxViewModel,
            socialViewModel = socialViewModel,
            onChatClick = onChatClick,
            onUserNotificationClick = onUserNotificationClick,
            onSocialNotificationClick = onSocialNotificationClick,
        )
    }
}
