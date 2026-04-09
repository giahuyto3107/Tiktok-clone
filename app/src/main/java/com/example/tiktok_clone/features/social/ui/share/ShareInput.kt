package com.example.tiktok_clone.features.social.ui.share

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.features.inbox.data.model.InboxAction
import com.example.tiktok_clone.features.inbox.viewmodel.InboxViewModel
import com.example.tiktok_clone.features.post.data.model.Post
import com.example.tiktok_clone.features.social.data.model.SocialAction
import com.example.tiktok_clone.features.social.ui.components.EmotionRow
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import com.example.tiktok_clone.ui.theme.TextPrimaryGray
import com.example.tiktok_clone.ui.theme.TikTokRed
import org.koin.androidx.compose.koinViewModel

@Composable
// Input tin nhan khi share cho ban
fun ShareInput(
    modifier: Modifier = Modifier,
    selectedFriendShare: List<String>,
    socialViewModel: SocialViewModel = koinViewModel(),
    inboxViewModel: InboxViewModel = koinViewModel(),
    currentPost: Post,
    onDismiss: () -> Unit
) {
    var messageText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = messageText,
            onValueChange = {
                messageText = it
            },
            textStyle = TextStyle(
                fontSize = 14.sp,
                lineHeight = 16.sp,
                color = TextPrimaryGray
            ),
            maxLines = 3,
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp),
            placeholder = {
                Text(
                    text = "Viết một tin nhắn...",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    color = TextPrimaryGray
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
        )
        EmotionRow(
            onSelect = {
                messageText += it
            }
        )
        Button(
            onClick = {
                val shareText = buildString {
                    val custom = messageText.trim()
                    if (custom.isNotEmpty()) {
                        append(custom)
                        append("\n")
                    }
                    if (currentPost.caption.isNotBlank()) {
                        append(currentPost.caption)
                        append("\n")
                    }
                    append(currentPost.mediaUrl)
                }.trim()

                for (friend in selectedFriendShare) {
                    inboxViewModel.onAction(
                        InboxAction.SendTextMessage(
                            otherUid = friend,
                            content = shareText,
                        )
                    )
                }
                messageText = ""
                socialViewModel.onAction(SocialAction.ClearSelectedFriendShare)
                onDismiss()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = TikTokRed,
                contentColor = Color.White,
                disabledContainerColor = TextPrimaryGray.copy(alpha = 0.2f),
                disabledContentColor = Color.White
            ),
            modifier = Modifier
                .padding(top = 6.dp, start = 8.dp, end = 8.dp)
                .fillMaxWidth()

        ) {
            Text(
                text = "Gửi",
                fontSize = 18.sp
            )
        }
    }
}
