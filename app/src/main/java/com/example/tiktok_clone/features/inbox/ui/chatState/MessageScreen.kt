package com.example.tiktok_clone.features.inbox.ui.chatState

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
fun Message(
    user: User,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .imePadding(),
    ) {
        MessageHead(user = user)
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

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PreviewMessage() {
    val socialViewModel: SocialViewModel = viewModel()
    val user: List<User> = socialViewModel.user.collectAsState().value
    Message(user[0])
}