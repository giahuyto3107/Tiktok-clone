package com.example.tiktok_clone.features.inbox.ui.inbox

import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
// Render 1 noti item trong inbox
fun InboxNotiItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconSize: Dp,
    bgColor: Color,
    notiType: String,
    notiContent: String = "",
    isNew: Boolean = false,
    onNotiClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
            .clickable {
                onNotiClick()
            }
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        ) {
        Box(
            modifier = modifier
                .border(
                    0.2.dp,
                    bgColor,
                    CircleShape
                )
                .size(60.dp)
                .clip(CircleShape)
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "State",
                modifier = Modifier
                    .size(iconSize),
                tint = Color.White

            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = notiType,
                maxLines = 1,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                fontWeight = if (isNew) FontWeight.Bold else FontWeight.Normal,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center

            )
            Text(
                text = notiContent,
                maxLines = 1,
                fontSize = 12.sp,
                lineHeight = 14.sp,
                fontWeight = if (isNew) FontWeight.Bold else FontWeight.Normal,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                color =if (isNew) Color.Black else Color.Gray,
            )
        }
    }
}