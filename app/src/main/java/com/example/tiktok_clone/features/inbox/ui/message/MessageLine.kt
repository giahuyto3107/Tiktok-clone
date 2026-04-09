@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH")

package com.example.tiktok_clone.features.inbox.ui.message

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.features.inbox.data.model.Message
import com.example.tiktok_clone.features.inbox.ui.components.statusLabel
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.features.social.ui.components.Avatar
import com.example.tiktok_clone.ui.theme.TikTokCyanDark

@Composable
// Render 1 bong bong tin nhan
fun Messageline(
    message: Message,
    isCurrentUser: Boolean,
    chatWithUser: User,
    isLastMessage: Boolean,
    isLastMessageInList: Boolean,
) {
    if (message.content.isBlank()) return

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
                } else {
                    Spacer(modifier = Modifier.width(30.dp))
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .widthIn(max = 315.dp)
                    .clip(
                        if (isLastMessage) {
                            RoundedCornerShape(
                                15.dp,
                                15.dp,
                                if (isCurrentUser) 0.dp else 15.dp,
                                if (isCurrentUser) 15.dp else 0.dp,
                            )
                        } else {
                            RoundedCornerShape(15.dp)
                        }
                    )
                    .background(if (isCurrentUser) TikTokCyanDark else Color.LightGray.copy(alpha = 0.2f))
                    .padding(horizontal = 10.dp, vertical = 8.dp),
            ) {
                Text(
                    text = message.content,
                    fontSize = 14.sp,
                    color = if (isCurrentUser) Color.White else Color.Black,
                )
            }
        }
        if (isCurrentUser && isLastMessageInList) {
            Text(
                text = message.statusLabel(),
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.padding(end = 4.dp, top = 2.dp),
            )
        }
    }
}
