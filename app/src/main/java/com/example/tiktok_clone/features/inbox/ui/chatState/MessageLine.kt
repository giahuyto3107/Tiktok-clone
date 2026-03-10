package com.example.tiktok_clone.features.inbox.ui.chatState

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tiktok_clone.features.social.model.Message
import com.example.tiktok_clone.features.social.model.MessageStatus
import com.example.tiktok_clone.features.social.model.MessageType
import com.example.tiktok_clone.features.social.ui.components.Avatar
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import com.example.tiktok_clone.ui.theme.TextPrimaryBlue

@Composable
fun Messageline(
    message: Message,
    isCurrentUser: Boolean,
    otherUserId: String,
    isLastMessage: Boolean
) {
    val viewModel: SocialViewModel = viewModel()
    val user = viewModel.getUser(otherUserId)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom,
    ) {
        if (!isCurrentUser) {
            if (isLastMessage) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                ) {
                    Avatar(
                        avatarUrl = user.avatarUrl,
                        modifier = Modifier.matchParentSize()
                    )
                }
            }
            else Spacer(modifier = Modifier.width(50.dp))
        }
        Spacer(modifier = Modifier.width(8.dp)) // ← thay thế spacedBy
        MessageContent(message, isCurrentUser, isLastMessage)
    }
}

@Composable
private fun MessageContent(
    message: Message,
    isCurrentUser: Boolean,
    isLastMessage: Boolean
) {
    when (message.type) {
        MessageType.TEXT -> TextContent(message, isCurrentUser,isLastMessage )
        MessageType.IMAGE -> ImageContent(message, isCurrentUser)
        MessageType.VIDEO -> VideoContent(message, isCurrentUser)
    }
}

@Composable
private fun TextContent(message: Message, isCurrentUser: Boolean, isLastMessage: Boolean) {
    Box(
        modifier = Modifier
            .wrapContentWidth()        // ← co lại theo content
            .widthIn(max = 320.dp)
            .clip(
                if (isLastMessage)
                RoundedCornerShape(
                    8.dp,
                    8.dp,
                    if (isCurrentUser) 0.dp else 8.dp,
                    if (isCurrentUser) 8.dp else 0.dp
                )
                else RoundedCornerShape(8.dp)
            )
            .background(if (isCurrentUser) TextPrimaryBlue else Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = message.content,
            fontSize = 20.sp,
            lineHeight = 30.sp,
            color = if (isCurrentUser) Color.White else Color.Black,
            modifier = Modifier
        )
    }
}

@Composable
private fun ImageContent(message: Message, isCurrentUser: Boolean) {
    Box(
        modifier = Modifier
            .size(200.dp, 150.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        AsyncImage(
            model = message.imageUri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Trạng thái đang gửi
        if (message.status == MessageStatus.SENDING) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            }
        }
    }
}

@Composable
private fun VideoContent(message: Message, isCurrentUser: Boolean) {
    Box(
        modifier = Modifier
            .size(200.dp, 150.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black)
    ) {
        AsyncImage(
            model = message.imageUri, // thumbnail
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Play icon overlay
        Icon(
            imageVector = Icons.Filled.PlayCircle,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.Center)
        )
    }
}