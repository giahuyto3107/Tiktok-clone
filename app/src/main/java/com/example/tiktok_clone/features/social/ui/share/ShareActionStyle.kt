package com.example.tiktok_clone.features.social.ui.share

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

sealed class ShareIconStyle {
    data class Vector(
        val icon: ImageVector,
        val size: Dp
    ) : ShareIconStyle()

    data class Letter(
        val text: String,
        val fontSize: TextUnit
    ) : ShareIconStyle()
}
