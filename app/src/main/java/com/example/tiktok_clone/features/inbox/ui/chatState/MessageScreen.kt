package com.example.tiktok_clone.features.inbox.ui.chatState

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import com.example.tiktok_clone.features.inbox.data.model.InboxAction
import com.example.tiktok_clone.features.inbox.data.model.Message
import com.example.tiktok_clone.features.inbox.viewmodel.InboxViewModel
import com.example.tiktok_clone.features.inbox.ui.InboxUiState
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.compose.koinViewModel

@Composable
fun MessageScreen(
    modifier: Modifier = Modifier,
    chatWithId: String,
    onBack: () -> Unit = {},
    onAvatarClick: (String) -> Unit = {}
) {
    val inboxViewModel: InboxViewModel = koinViewModel()
    val socialViewModel: SocialViewModel = koinViewModel()
    val messagesUiState by inboxViewModel.messagesUiState.collectAsState()
    val messages = (messagesUiState as? InboxUiState.Success<Message>)?.items ?: emptyList()
    val chatWithUser = socialViewModel.getUser(chatWithId)
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    LaunchedEffect(chatWithId) {
        inboxViewModel.onAction(InboxAction.LoadMessages(chatWithId))
    }

    // Đóng WebSocket khi rời màn chat
    DisposableEffect(chatWithId) {
        onDispose { inboxViewModel.onAction(InboxAction.DisconnectChat) }
    }

    Column(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        MessageHead(
            chatWithUser = chatWithUser,
            onBack = {
                onBack()
            },
            onAvatarClick = {
                onAvatarClick(chatWithId)
            }
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .size(0.5.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .imePadding()
        ) {
            when (val state = messagesUiState) {
                InboxUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.padding(12.dp))
                    }
                }

                is InboxUiState.Error -> {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = state.message,
                            modifier = Modifier.padding(12.dp),
                        )
                    }
                }
                is InboxUiState.Success -> Unit
            }

            MessageList(
                modifier = Modifier.weight(1f),
                messages = messages,
                chatWithUser = chatWithUser,
                currentUser = currentUserId,
                onLoadMore = { inboxViewModel.onAction(InboxAction.LoadMoreMessages(chatWithId)) },
            )
            MessageBottom(
                otherUid = chatWithId,
                onSend = { text ->
                    inboxViewModel.onAction(InboxAction.SendTextMessage(chatWithId, text))
                },
            )
        }
    }
}
