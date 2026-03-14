package com.example.tiktok_clone.features.inbox.ui.inboxState

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.features.inbox.viewmodel.InboxViewModel
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun InboxScreen(
    currentUserId: String,
    onChatClick: (userId: String) -> Unit = {}
) {
    val inboxViewModel: InboxViewModel = koinViewModel()
    val socialViewModel: SocialViewModel = koinViewModel()
    val chats by inboxViewModel.chats.collectAsState()
    val currentUser = socialViewModel.getUser(currentUserId)

    LaunchedEffect(Unit) {
        inboxViewModel.loadChats()
    }

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
        )
    }
}
