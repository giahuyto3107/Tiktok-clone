package com.example.tiktok_clone.features.inbox.ui.inboxState

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import com.example.tiktok_clone.features.social.model.User
import com.example.tiktok_clone.features.social.ui.components.Avatar

@Composable
fun InboxFriendItem(
    modifier: Modifier = Modifier,
    friend: User,
    isUser: Boolean = false,
    onItemClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .width(90.dp)
            .then(modifier),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier
                .border(
                    0.4.dp,
                    Color.LightGray.copy(alpha = 0.5f),
                    CircleShape
                )
                .size(80.dp)
                .clip(CircleShape),
        ) {
            Avatar(
                avatarUrl = friend.avatarUrl,
                modifier = Modifier
                    .matchParentSize()
            )
        }
        Text(
            text = if (isUser) "Quay" else friend.userName,
            maxLines = 1,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center

        )
    }
}