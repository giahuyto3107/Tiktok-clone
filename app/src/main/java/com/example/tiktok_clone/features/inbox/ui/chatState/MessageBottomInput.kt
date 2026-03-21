package com.example.tiktok_clone.features.inbox.ui.chatState

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.features.social.ui.components.CommentItem
import com.example.tiktok_clone.ui.theme.TikTokCyanDark
import com.example.tiktok_clone.ui.theme.TikTokRed

@Composable
fun MessageBottomInput(
    modifier: Modifier = Modifier,
    isMessage: Boolean = false,
    onGalleryClick: () -> Unit = {},
    onStickerClick: () -> Unit = {},
    onMicClick: () -> Unit = {},
    onSendClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier.padding(start = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (isMessage) Color.Gray else TikTokCyanDark),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.CameraAlt,
                contentDescription = "State",
                modifier = Modifier.size(30.dp),
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .padding(horizontal = if (isMessage) 4.dp else 12.dp)
                .then(modifier),
//            horizontalArrangement = if (isMessage) Arrangement.spacedBy(4.dp) else Arrangement.spacedBy(
//                12.dp
//            ),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isMessage) {
                CommentItem(
                    icon = Icons.Outlined.Photo,
                    showText = false,
                    modifier = Modifier
                        .size(32.dp),
                    onClick = onGalleryClick,
                    tint = Color.Black,
                    text = "Photos"
                )
//            CommentItem(
//                icon = Icons.Outlined.AddReaction,
//                showText = false,
//                modifier = Modifier
//                    .size(32.dp),
//                onClick = onStickerClick,
//                tint = Color.Black,
//                text = "Sticker"
//            )
//            if (!isMessage) {
//                CommentItem(
//                    icon = Icons.Outlined.MicNone,
//                    showText = false,
//                    modifier = Modifier
//                        .size(32.dp),
//                    onClick = onMicClick,
//                    tint = Color.Black,
//                    text = "Mic"
//                )
            } else {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(TikTokRed)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onSendClick
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send Message",
                        modifier = Modifier
                            .size(26.dp)
                            .rotate(-45f)
                            .scale(0.7f, 1f)
                            .clip(RoundedCornerShape(30.dp))
                            .offset(x = 3.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}