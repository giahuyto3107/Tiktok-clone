package com.example.tiktok_clone.features.social.ui.comment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.features.social.ui.components.CommentItem


@Composable
fun CommentInputItem(
    modifier: Modifier = Modifier,
    onGalleryClick: () -> Unit = {},
//    onStickerClick: () -> Unit = {},
//    onTagClick: () -> Unit = {},
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CommentItem(
            icon = Icons.Outlined.Image,
            showText = false,
            modifier = Modifier
                .size(32.dp),
            onClick = onGalleryClick,
            tint = Color.Gray,
            text = "Ảnh"
        )
//        CommentItem(
//            icon = Icons.Outlined.InsertEmoticon,
//            showText = false,
//            modifier = Modifier
//                .size(28.dp),
//            onClick = onStickerClick,
//            tint = Color.Gray,
//            text = "Sticker"
//        )
//        CommentItem(
//            icon = Icons.Outlined.AlternateEmail,
//            showText = false,
//            modifier = Modifier
//                .size(28.dp),
//            onClick = onTagClick,
//            tint = Color.Gray,
//            text = "tag"
//        )
    }
}