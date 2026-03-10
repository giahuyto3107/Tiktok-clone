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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiktok_clone.features.social.model.User
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel

@Composable
fun MessageScreen(
    modifier: Modifier = Modifier,
    userId: String,
    onBack: () -> Unit = {}

) {
    val socialViewModel: SocialViewModel = viewModel()
    val currentUser = socialViewModel.getUser(userId)
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .imePadding(),
    ) {
        MessageHead(user = currentUser, onBack = onBack)
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .size(0.5.dp)
        )
        MessageList(modifier = Modifier.weight(1f))
        MessageBottom()
    }
}

