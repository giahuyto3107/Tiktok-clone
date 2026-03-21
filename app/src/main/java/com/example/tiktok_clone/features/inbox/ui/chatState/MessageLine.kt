@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH")

package com.example.tiktok_clone.features.inbox.ui.chatState

import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import com.example.tiktok_clone.features.inbox.data.model.Message
import com.example.tiktok_clone.features.inbox.data.model.MessageStatus
import com.example.tiktok_clone.features.inbox.data.model.MessageType
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.features.social.ui.components.Avatar
import com.example.tiktok_clone.ui.theme.TikTokCyanDark

@Composable
fun Messageline(
    message: Message,
    isCurrentUser: Boolean,
    chatWithUser: User,
    isLastMessage: Boolean,
    isLastMessageInList: Boolean,
) {
    Column(
        horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start,
            verticalAlignment = Alignment.Bottom,
        ) {
            if (!isCurrentUser) {
                if (isLastMessage) {
                    Avatar(
                        avatarUrl = chatWithUser.avatarUrl,
                        avatarSize = 30,
                    )
                } else Spacer(modifier = Modifier.width(30.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
            MessageContent(message, isCurrentUser, isLastMessage)
        }
        if (isCurrentUser && isLastMessageInList) {
            val label = when (message.receiptStatus) {
                MessageStatus.SEEN -> "Đã xem"
                MessageStatus.DELIVERED -> "Đã gửi"
                else -> when (message.status) {
                    MessageStatus.SENDING -> "Đang gửi"
                    else -> "Đã gửi"
                }
            }
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.padding(end = 4.dp, top = 2.dp),
            )
        }
    }
}

@Composable
private fun MessageContent(
    message: Message,
    isCurrentUser: Boolean,
    isLastMessage: Boolean
) {
    if (message.type == MessageType.TEXT) {
        TextContent(message, isCurrentUser, isLastMessage)
        return
    }

    Column(
        horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        when (message.type) {
            MessageType.IMAGE -> ImageContent(message, isCurrentUser)
            MessageType.VIDEO -> VideoContent(message, isCurrentUser)
            MessageType.TEXT -> Unit
        }
        if (message.content.isNotBlank()) {
            TextContent(message, isCurrentUser, isLastMessage = isLastMessage)
        }
    }
}

@Composable
private fun TextContent(message: Message, isCurrentUser: Boolean, isLastMessage: Boolean) {
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .widthIn(max = 315.dp)
            .clip(
                if (isLastMessage)
                    RoundedCornerShape(
                        15.dp,
                        15.dp,
                        if (isCurrentUser) 0.dp else 15.dp,
                        if (isCurrentUser) 15.dp else 0.dp
                    )
                else RoundedCornerShape(15.dp)
            )
            .background(if (isCurrentUser) TikTokCyanDark else Color.White)
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Text(
            text = message.content,
            fontSize = 14.sp,
            color = if (isCurrentUser) Color.White else Color.Black,
            modifier = Modifier
        )
    }
}

@Composable
private fun ImageContent(message: Message, isCurrentUser: Boolean) {
    var isFullScreen by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize(0.3f)
            .clip(RoundedCornerShape(10.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    isFullScreen = true
                }
            )
    ) {
        AsyncImage(
            model = message.imageUri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
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
    if (isFullScreen) {
        Dialog(
            onDismissRequest = { isFullScreen = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            isFullScreen = false
                        }
                    )
            ) {
                AsyncImage(
                    model = message.imageUri,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun VideoContent(message: Message, isCurrentUser: Boolean) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize(0.3f)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Black)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    isPlaying = true
                }
            )
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(message.imageUri)
                .decoderFactory(VideoFrameDecoder.Factory())
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Icon(
            imageVector = Icons.Filled.PlayCircle,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.Center)
        )
    }
    if (isPlaying) {
        Dialog(
            onDismissRequest = { isPlaying = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            val exoPlayer = remember {
                ExoPlayer.Builder(context).build().apply {
                    setMediaItem(MediaItem.fromUri(message.imageUri ?: ""))
                    prepare()
                    playWhenReady = true
                }
            }
            DisposableEffect(Unit) { onDispose { exoPlayer.release() } }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            isPlaying = false
                        }
                    )
            ) {
                AndroidView(
                    factory = { PlayerView(it).apply { player = exoPlayer } },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}