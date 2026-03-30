package com.example.tiktok_clone.features.social.ui.share

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import com.example.tiktok_clone.features.social.ui.SocialUiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun ShareFriendList(
    socialViewModel: SocialViewModel = koinViewModel(),
) {
    val uiState by socialViewModel.uiState.collectAsState()
    val friendState = (uiState as? SocialUiState.Success)?.data?.friends ?: emptyList()
    val friend = socialViewModel.getUserList(friendState.map { it.uid })

    LazyRow(
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(friend.size) { index ->
            Log.d("ShareFriendList", "ShareFriendList: ${friend[index].id}")
            ShareFriendItem(
                friend = friend[index],
                onShare = {
//                    viewModel.onAction(SocialAction.Share(it.userID))
                },
                socialViewModel = socialViewModel
            )
        }
    }
}