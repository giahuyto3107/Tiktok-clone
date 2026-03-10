package com.example.tiktok_clone.features.inbox.ui.inboxState

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiktok_clone.features.social.model.User
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel

@Composable
fun InboxScreen(
    currentUserId: String,
    onChatClick: (userId: String) -> Unit = {}
) {
    val socialViewModel: SocialViewModel = viewModel()
    val currentUser = socialViewModel.getUser(currentUserId)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            InboxHeader()
            InboxFriendList(currentUser)
            InboxChatList(currentUser, onChatClick = onChatClick)
        }
    }
}

