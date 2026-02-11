package com.example.tiktok_clone.features.social.ui.share

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.tiktok_clone.features.social.model.ShareItem

data class ShareActionUi(
    val id: String,
    val action: ShareItem,
    val iconStyle: ShareIconStyle,
    val tint: Color,
    val backgroundColor: Color,
    val modifier: Modifier = Modifier
)