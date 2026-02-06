package com.example.tiktok_clone.features.social.ui.share

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiktok_clone.features.social.ui.components.Avatar
import com.example.tiktok_clone.features.social.ui.components.EmotionRow
import com.example.tiktok_clone.features.social.viewModel.SocialAction
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import com.example.tiktok_clone.ui.theme.RedHeart
import com.example.tiktok_clone.ui.theme.TextPrimaryGray
import com.google.common.collect.Multimaps.index

@Composable
fun ShareInput(
    modifier: Modifier = Modifier,
    selectedFriendShare: List<String>,
    viewModel: SocialViewModel = viewModel(),

    ) {
    var messageText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        TextField(
            value = messageText,
            onValueChange = {
                messageText = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 34.dp),
            placeholder = {
                Text(
                    text = "Viết một tin nhắn...",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize = 18.sp,
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
                viewModel.onAction(
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
                    viewModel.onAction(
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
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = "Gửi đến nhóm mới",
                        fontSize = 18.sp
                    )
                    sendList(
                        selectedFriendShare = selectedFriendShare,
                        viewModel = viewModel
                    )
                }
            }
        }

    }

}

@Composable
fun sendList(
    selectedFriendShare: List<String>,
    viewModel: SocialViewModel = viewModel()
) {
    val users = viewModel.getUserList(selectedFriendShare).take(2)
    Row(
        modifier = Modifier
    ) {
        users.forEachIndexed {index, user ->
            Box(
                modifier = Modifier.offset(x = (-index * 10).dp)
            ) {
                sendItem(
                    avatarUrl = user.avatarUrl
                )
            }
        }
    }
}

@Composable
fun sendItem(
    avatarUrl: String?,
) {
    Box(
        modifier = Modifier
            .border(0.2.dp, Color.LightGray, CircleShape)
            .size(20.dp)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Avatar(
            avatarUrl = avatarUrl ?: "",
            modifier = Modifier
                .matchParentSize()
        )
    }
}