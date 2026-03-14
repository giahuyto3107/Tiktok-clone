package com.example.tiktok_clone.features.social.ui.share

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ShareFriendList(
    socialViewModel: SocialViewModel = koinViewModel(),
) {
    val friendState by socialViewModel.friends.collectAsState()
    val friend = socialViewModel.getUserList(friendState.map { it.uid })

    LazyRow(
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(friend.size) { index ->
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