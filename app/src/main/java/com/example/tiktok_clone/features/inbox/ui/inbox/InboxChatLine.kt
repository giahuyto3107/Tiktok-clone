package com.example.tiktok_clone.features.inbox.ui.inbox

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.features.inbox.data.model.Message
import com.example.tiktok_clone.features.inbox.ui.components.buildChatPreviewText
import com.example.tiktok_clone.features.inbox.ui.components.statusLabel
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.features.social.ui.components.Avatar

@Composable
// Render 1 dong chat trong inbox
fun InboxChatLine(
    modifier: Modifier = Modifier,
    chatWith: User,
    message: Message,
    unreadCount: Int,
    currentUserId: String?,
    onChatClick: (chatWithId: String) -> Unit = {},
) {
    val lastMessageStatus = if (message.senderId == currentUserId) {
        message.statusLabel()
    } else {
        buildChatPreviewText(message, chatWith, currentUserId)
    }
    val isNewMessage = unreadCount > 0
    val previewText = buildChatPreviewText(message, chatWith, currentUserId)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
            .clickable { onChatClick(chatWith.id) }
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Avatar(
            avatarUrl = chatWith.avatarUrl,
            avatarSize = 60,
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = chatWith.userName,
                maxLines = 1,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                fontWeight = if (isNewMessage) FontWeight.Bold else FontWeight.Normal,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
            )
            Text(
                text = if (isNewMessage) previewText else lastMessageStatus,
                maxLines = 1,
                fontSize = 12.sp,
                lineHeight = 14.sp,
                fontWeight = if (isNewMessage) FontWeight.Bold else FontWeight.Normal,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                color = if (isNewMessage) Color.Black else Color.Gray,
            )
        }
    }
}
