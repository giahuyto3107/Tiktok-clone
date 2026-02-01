package com.example.tiktok_clone.features.social.ui.commentComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.InsertEmoticon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun CommentInputItem(
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CommentItem(
            icon = Icons.Outlined.Image,
            showText = false,
            modifier = Modifier
                .size(28.dp),
//                            .graphicsLayer( //phóng to UI element lên 1.2 lần, vì Icons có padding ẩn
//                                scaleX = 1.2f,
//                                scaleY = 1.2f
//                            ),
            onClick = {},
            tint = Color.Black,
            text = "Ảnh"
        )
        CommentItem(
            icon = Icons.Outlined.InsertEmoticon,
            showText = false,
            modifier = Modifier
                .size(28.dp),
            onClick = {},
            tint = Color.Black,
            text = "Sticker"
        )
        CommentItem(
            icon = Icons.Outlined.AlternateEmail,
            showText = false,
            modifier = Modifier
                .size(28.dp),
//                            .graphicsLayer(
//                                scaleX = 1.2f,
//                                scaleY = 1.2f
//                            ),
            onClick = {},
            tint = Color.Black,
            text = "tag"
        )
    }
}