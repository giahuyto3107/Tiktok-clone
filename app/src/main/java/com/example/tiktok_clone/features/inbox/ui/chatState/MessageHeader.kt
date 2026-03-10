package com.example.tiktok_clone.features.inbox.ui.chatState

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.features.social.model.User
import com.example.tiktok_clone.features.social.ui.components.Avatar
import com.example.tiktok_clone.ui.theme.GrayBackground
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.ArrowLeft

@Composable
fun MessageHead(
    user: User,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(GrayBackground)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = FontAwesomeIcons.Solid.ArrowLeft,
                contentDescription = "Back",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onBack)
            )
            Box(
                modifier = modifier
                    .border(
                        0.2.dp,
                        Color.LightGray.copy(alpha = 0.5f),
                        CircleShape
                    )
                    .size(50.dp)
                    .clip(CircleShape),
            ) {
                Avatar(
                    avatarUrl = user.avatarUrl,
                    modifier = Modifier.matchParentSize()
                )
            }
            Text(
                text = user.userName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Flag,
                contentDescription = "Report",
                modifier = Modifier
                    .size(32.dp)
            )
            Icon(
                imageVector = Icons.Filled.MoreHoriz,
                contentDescription = "See More",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
