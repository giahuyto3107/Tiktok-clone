package com.example.tiktok_clone.features.inbox.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiktok_clone.R
import com.example.tiktok_clone.features.social.model.User
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel

@Composable
fun InboxScreen(
    socialViewModel: SocialViewModel = viewModel()
) {
    val user: List<User> = socialViewModel.user.collectAsState().value
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
            InboxFriendList(user[0])
            InboxChatList(user[0])
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InboxScreenPreview() {
    InboxScreen()
}
