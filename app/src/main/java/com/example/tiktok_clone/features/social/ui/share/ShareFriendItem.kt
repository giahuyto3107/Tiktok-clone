package com.example.tiktok_clone.features.social.ui.shareComponents

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.features.social.ui.commentComponents.Avatar

@Composable
fun ShareFriendItem(
    modifier: Modifier = Modifier,
    onShare: () -> Unit,
    avatarUrl: String,
    userName: String
) {
    Column(
        modifier = Modifier.clickable(onClick = onShare),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .border(0.2.dp, Color.LightGray, CircleShape)
                .size(55.dp)
                .clip(CircleShape)
        ) {
            Avatar(
                avatarUrl = avatarUrl,
                modifier = Modifier
                    .matchParentSize()
            )
        }
        Text(
            text = userName,
            fontSize = 12.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(50.dp),
            maxLines = 2,
            lineHeight = 14.sp,
            overflow = TextOverflow.Ellipsis
        )
    }
}