package com.example.tiktok_clone.features.inbox.ui.inbox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel

@Composable
// Render hang friend o header inbox
fun InboxFriendList(
    currentUser: User,
    viewModel: SocialViewModel,
    onChatClick: (userId: String) -> Unit = {},
) {
    val friendState by viewModel.friends.collectAsState()
    val friend = viewModel.getUserList(friendState.map { it.uid })
    val chatWiths = friend.filter { it.id != currentUser.id }
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
        contentPadding = PaddingValues(start = 8.dp, top = 16.dp),
    ) {
        item {
            InboxFriendItem(
                friend = currentUser,
                isUser = true
            )
        }
        items(chatWiths.size) { index ->
            InboxFriendItem(
                friend = chatWiths[index],
                onChatClick = { onChatClick(chatWiths[index].id) }
            )
        }
    }
}