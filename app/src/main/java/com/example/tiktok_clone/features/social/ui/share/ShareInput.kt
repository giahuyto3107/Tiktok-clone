package com.example.tiktok_clone.features.social.ui.share

import android.util.Log
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiktok_clone.features.social.viewModel.SocialAction
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel

@Composable
fun ShareInput(
    modifier: Modifier = Modifier,
    viewModel: SocialViewModel = viewModel(),
    shareFriendCount: Int = 0

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
                    color = Color.LightGray
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
                    SocialAction.Share(messageText))
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red.copy(alpha=0.8f),
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.White
            ),
            modifier = Modifier
                .padding(top = 6.dp, start = 8.dp, end = 8.dp)
                .fillMaxWidth()

        ) {
            Text(
                text = "Gửi",
                fontSize = 14.sp
            )
        }
        if (shareFriendCount > 1) {
            Button(
                onClick = {
                    viewModel.onAction(
                        SocialAction.Share(messageText))
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray.copy(alpha=0.8f),
                    contentColor = Color.Black,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.White
                ),
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .fillMaxWidth()

            ) {
                Text(
                    text = "Gửi đến nhóm mới",
                    fontSize = 14.sp
                )
            }
        }

    }

}