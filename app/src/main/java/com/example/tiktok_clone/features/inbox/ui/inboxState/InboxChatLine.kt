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
import com.example.tiktok_clone.features.inbox.data.model.Message
import com.example.tiktok_clone.features.inbox.data.model.MessageStatus
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.features.social.ui.components.Avatar
import com.example.tiktok_clone.features.social.ui.components.toDateString

@Composable
fun InboxChatLine(
    modifier: Modifier = Modifier,
    chatWith: User,
    message: Message,
    currentUserId: String = "",
    onChatClick: (chatWithId: String) -> Unit = {}
) {
    val isMessageNew: Boolean = message.status == MessageStatus.NEW
    val lastMessageStatus: String =
        if (message.senderId == currentUserId) {
            if (isMessageNew)
                "Đã gửi ${message.timestamp.toDateString()}"
            else "Đã xem"
        } else {
            message.content
        }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
            .clickable { onChatClick(chatWith.id) }
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(
            avatarUrl = chatWith.avatarUrl,
            avatarSize = 60,
        )
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
                fontWeight = if (isMessageNew) FontWeight.Bold else FontWeight.Normal,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center

            )
            Text(
                text = lastMessageStatus,
                maxLines = 1,
                fontSize = 12.sp,
                lineHeight = 14.sp,
                fontWeight = if (isMessageNew) FontWeight.Bold else FontWeight.Normal,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                color = if (isMessageNew) Color.Black else Color.Gray,
            )
        }
        Icon(
            imageVector = Icons.Outlined.CameraAlt,
            contentDescription = "Send photo",
        )
    }
}