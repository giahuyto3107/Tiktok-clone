package com.example.tiktok_clone.features.inbox.ui.inbox

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.features.social.ui.components.Avatar

@Composable
// Render 1 friend item trong inbox header
fun InboxFriendItem(
    modifier: Modifier = Modifier,
    friend: User,
    isUser: Boolean = false,
    onChatClick: (friendId: String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    onChatClick(friend.id)
                }
            )
            .then(modifier),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Avatar(
            avatarUrl = friend.avatarUrl,
            avatarSize = 90,
        )
        Text(
            text = if (isUser) "Quay" else friend.userName,
            maxLines = 1,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(70.dp)
        )
    }
}