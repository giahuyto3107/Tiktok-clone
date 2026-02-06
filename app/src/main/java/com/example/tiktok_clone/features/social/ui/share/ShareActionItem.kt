package com.example.tiktok_clone.features.social.ui.share

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import com.example.tiktok_clone.features.social.model.ShareItem


@Composable
fun ShareActionItem(
    itemUi: ShareActionUi,
    onActionClick: (ShareItem) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .padding(start = 4.dp, end = 4.dp)
            .clickable {
            onActionClick(itemUi.action)
        },
    ) {
        Box(
            modifier = Modifier
                .size(55.dp)
                .border(0.2.dp, Color.LightGray, CircleShape)
                .clip(CircleShape)
                .background(itemUi.backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            when (val iconStyle = itemUi.iconStyle) {
                is ShareIconStyle.Vector -> {
                    Icon(
                        imageVector = iconStyle.icon,
                        contentDescription = itemUi.action.title,
                        tint = itemUi.tint,
                        modifier = itemUi.modifier
                            .size(itemUi.iconStyle.size)
                    )
                }

                is ShareIconStyle.Letter -> {
                    Text(
                        text = iconStyle.text,
                        fontSize = iconStyle.fontSize,
                        color = itemUi.tint,
                        fontWeight = FontWeight.Medium,
                        modifier = itemUi.modifier
                    )
                }
            }
        }
        Text(
            text = itemUi.action.title,
            fontSize = 12.sp,
            maxLines = 2,
            lineHeight = 14.sp,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(55.dp),
            textAlign = TextAlign.Center
        )
    }
}
