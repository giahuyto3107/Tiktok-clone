package com.example.tiktok_clone.features.social.ui.share

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel

@Composable
fun ShareFriendList(
    viewModel: SocialViewModel = viewModel(),
) {
    val friends by viewModel.friends.collectAsState()
    LazyRow(
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(friends.size) { friend ->
            ShareFriendItem(
                friend = friends[friend],
                onShare = {}
            )
        }
    }
}