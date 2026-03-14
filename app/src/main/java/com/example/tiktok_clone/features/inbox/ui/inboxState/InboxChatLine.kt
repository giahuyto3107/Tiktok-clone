package com.example.tiktok_clone.features.inbox.ui.inboxState

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.features.inbox.model.MessageStatus
import com.example.tiktok_clone.features.social.model.User
import com.example.tiktok_clone.features.social.ui.components.Avatar

@Composable
fun InboxChatItem(
    modifier: Modifier = Modifier,
    chatWith: User,
    messageStatus: MessageStatus,
    onChatClick: () -> Unit = {}
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
            .clickable { onChatClick() }
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = modifier
                .border(
                    0.2.dp,
                    Color.LightGray.copy(alpha = 0.5f),
                    CircleShape
                )
                .size(60.dp)
                .clip(CircleShape),
        ) {
            Avatar(
                avatarUrl = chatWith.avatarUrl,
                modifier = Modifier
                    .matchParentSize()
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = chatWith.userName,
                maxLines = 1,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                fontWeight = if( messageStatus == MessageStatus.NEW) FontWeight.Bold else FontWeight.Normal,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center

            )
            Text(
                text = "Đã xem",
                maxLines = 1,
                fontSize = 12.sp,
                lineHeight = 14.sp,
                fontWeight = if( messageStatus == MessageStatus.NEW) FontWeight.Bold else FontWeight.Normal,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
        Icon(
            imageVector = Icons.Outlined.CameraAlt,
            contentDescription = "Send photo",
        )
    }
}