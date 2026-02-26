package com.example.tiktok_clone.features.inbox.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiktok_clone.features.social.model.User
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel

@Composable
fun InboxFriendList(
    currentUser: User,
    viewModel: SocialViewModel = viewModel()
) {
    val friends by viewModel.friends.collectAsState()
    viewModel.loadFriends(currentUser.id)
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            InboxFriendItem(
                friend = currentUser,
                isUser = true
            )
        }
        items(friends.size) { index ->
            InboxFriendItem(friend = friends[index])
        }
    }
}