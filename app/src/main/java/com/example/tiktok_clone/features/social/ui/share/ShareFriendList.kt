package com.example.tiktok_clone.features.social.ui.share

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiktok_clone.features.social.model.User
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel

@Composable
fun ShareFriendList(
    currentUser: User,
    viewModel: SocialViewModel = viewModel(),
) {
    viewModel.loadFriends(currentUser.id)
    val friends by viewModel.friends.collectAsState()
    LazyRow(
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(friends.size) { friend ->
            ShareFriendItem(
                friend = friends[friend],
                onShare = {
//                    viewModel.onAction(SocialAction.Share(it.userID))
                },
                viewModel = viewModel
            )
        }
    }
}