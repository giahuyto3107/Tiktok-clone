package com.example.tiktok_clone.features.social.ui.share

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiktok_clone.features.social.ui.components.Avatar
import com.example.tiktok_clone.features.social.ui.components.EmotionRow
import com.example.tiktok_clone.features.social.data.model.SocialAction
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import com.example.tiktok_clone.ui.theme.RedHeart
import com.example.tiktok_clone.ui.theme.TextPrimaryGray
import org.koin.androidx.compose.koinViewModel

@Composable
fun ShareInput(
    modifier: Modifier = Modifier,
    selectedFriendShare: List<String>,
    socialViewModel: SocialViewModel = koinViewModel(),

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
                .height(96.dp),
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
                socialViewModel.onAction(
                    SocialAction.Share(messageText)
                )
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = RedHeart,
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
        if (selectedFriendShare.size > 1) {
            Button(
                onClick = {
                    socialViewModel.onAction(
                        SocialAction.Share(messageText)
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray.copy(alpha = 0.8f),
                    contentColor = Color.Black,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.White
                ),
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .fillMaxWidth()

            ) {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Gửi đến nhóm mới",
                        fontSize = 18.sp
                    )
                    SendList(
                        selectedFriendShare = selectedFriendShare,
                        socialViewModel = socialViewModel
                    )
                }
            }
        }

    }

}

@Composable
fun SendList(
    selectedFriendShare: List<String>,
    socialViewModel: SocialViewModel = koinViewModel()
) {
    val users = socialViewModel.getUserList(selectedFriendShare).take(2)
    Row(
        modifier = Modifier
    ) {
        users.forEachIndexed {index, user ->
            Box(
                modifier = Modifier.offset(x = (-index * 26).dp)
            ) {
                Avatar(
                    avatarUrl = user.avatarUrl,
                    avatarSize = 20,
                )
            }
        }
    }
}

