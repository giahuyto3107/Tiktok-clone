package com.example.tiktok_clone.features.social.ui.share

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ShareActionItem(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Filled.MoreHoriz,
    iconName: String = "",
    iconNameSize: Int = 10,
    iconSize: Int = 24,
    iconColor: Color = Color.White,
    isText: Boolean = true,
    textIcon: String = "",
    textIconSize: Int = 12,
    textColor: Color = Color.White,
    backgroundColor: Color = Color.White,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(backgroundColor)
                .border(0.3.dp, Color.LightGray, CircleShape)
                .size(50.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        )
        {
            if (!isText)
                Icon(
                    imageVector = icon,
                    contentDescription = iconName,
                    tint = iconColor,
                    modifier = Modifier
                        .size(iconSize.dp)
                        .then(modifier)
                )
            else
                Text(
                    text = textIcon,
                    fontSize = textIconSize.sp,
                    lineHeight = textIconSize.sp,
                    color = textColor,
                    textAlign = TextAlign.Center,
                )
        }
        Text(
            text = iconName,
            fontSize = iconNameSize.sp,
            lineHeight = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black,
            modifier = Modifier
                .width(60.dp)
        )
    }
}
