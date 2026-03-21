package com.example.tiktok_clone.features.inbox.ui.chatState

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.features.inbox.viewmodel.InboxViewModel
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.compose.koinViewModel

@Composable
fun MessageScreen(
    modifier: Modifier = Modifier,
    chatWithId: String,
    onBack: () -> Unit = {},
) {
    val inboxViewModel: InboxViewModel = koinViewModel()
    val socialViewModel: SocialViewModel = koinViewModel()
    val messages by inboxViewModel.messages.collectAsState()
    val chatWithUser = socialViewModel.getUser(chatWithId)
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    LaunchedEffect(chatWithId) {
        inboxViewModel.loadMessages(chatWithId)
    }

    // Đóng WebSocket khi rời màn chat
    DisposableEffect(chatWithId) {
        onDispose { inboxViewModel.disconnectChat() }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .imePadding(),
    ) {
        MessageHead(chatWithUser = chatWithUser, onBack = onBack)
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .size(0.5.dp)
        )
        MessageList(
            modifier = Modifier.weight(1f),
            messages = messages,
            chatWithUser = chatWithUser,
            currentUser = currentUserId,
            onLoadMore = { inboxViewModel.loadMoreMessages(chatWithId) },
        )
        MessageBottom(
            otherUid = chatWithId,
            onSend = { text ->
                inboxViewModel.sendTextMessage(chatWithId, text)
            },
        )
    }
}
