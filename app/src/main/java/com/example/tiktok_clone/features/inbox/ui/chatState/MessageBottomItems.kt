package com.example.tiktok_clone.features.inbox.ui.chatState

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddReaction
import androidx.compose.material.icons.outlined.MicNone
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.features.social.ui.components.CommentItem

@Composable
fun MessageBottomItems(
    modifier: Modifier = Modifier,
    onGalleryClick: () -> Unit = {},
    onStickerClick: () -> Unit = {},
    onMicClick: () -> Unit = {},
){
    Row(
        modifier = Modifier
            .padding(start = 2.dp)
            .then(modifier),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CommentItem(
            icon = Icons.Outlined.Photo,
            showText = false,
            modifier = Modifier
                .size(32.dp),
            onClick = onGalleryClick,
            tint = Color.Black,
            text = "Photos"
        )
        CommentItem(
            icon = Icons.Outlined.AddReaction,
            showText = false,
            modifier = Modifier
                .size(32.dp),
            onClick = onStickerClick,
            tint = Color.Black,
            text = "Sticker"
        )
        CommentItem(
            icon = Icons.Outlined.MicNone,
            showText = false,
            modifier = Modifier
                .size(32.dp),
            onClick = onMicClick,
            tint = Color.Black,
            text = "Mic"
        )
    }
}