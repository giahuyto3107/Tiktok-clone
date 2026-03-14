package com.example.tiktok_clone.features.social.data.model

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector


data class App (
    val modifier: Modifier = Modifier,
    val icon: ImageVector? = null,
    val iconSize: Int = 36,
    val appIconByLetter: String = "",
    val fontSize: Int = 16,
    val appName: String,
    val tint: Color = Color.White,
    val backgroundColor: Color = Color.White,
    val onClick: () -> Unit
)

