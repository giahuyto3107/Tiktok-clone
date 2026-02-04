package com.example.tiktok_clone.features.social.model

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class ShareAcction(
    val modifier: Modifier = Modifier,
    val iconSize: Int = 24,
    val icon: ImageVector? = null,
    val appName: String,
    val tint: Color = Color.Black.copy(alpha = 0.8f),
    val backgroundColor: Color = Color.LightGray.copy(alpha = 0.5f),
    val industryType: String? = null,
    val onClick: (() -> Unit)?= null
)
