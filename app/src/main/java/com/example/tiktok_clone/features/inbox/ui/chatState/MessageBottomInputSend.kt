package com.example.tiktok_clone.features.inbox.ui.chatState

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.AddReaction
import androidx.compose.material.icons.outlined.MicNone
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.features.social.ui.components.CommentItem
import com.example.tiktok_clone.ui.theme.BlueAccent
import com.example.tiktok_clone.ui.theme.RedHeart

@Composable
fun MessageBottomInputSend(
    modifier: Modifier = Modifier,
    onSendClick: () -> Unit = {},
    onStickerClick: () -> Unit = {},
){
    Row(
        modifier = Modifier
            .padding(start = 2.dp)
            .then(modifier),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CommentItem(
            icon = Icons.Outlined.AddReaction,
            showText = false,
            modifier = Modifier
                .size(32.dp),
            onClick = onStickerClick,
            tint = Color.Black,
            text = "Ảnh"
        )
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(RedHeart)
                .clickable(onClick = onSendClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Send,
                contentDescription = "State",
                modifier = Modifier
                    .size(26.dp)
                    .rotate(-45f)
                    .scale(0.7f,1f)
                    .clip(RoundedCornerShape(30.dp))
                    .offset(x = 3.dp),
                tint = Color.White
            )
        }
    }
}