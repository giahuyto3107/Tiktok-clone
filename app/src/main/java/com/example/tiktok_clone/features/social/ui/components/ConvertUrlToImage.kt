package com.example.tiktok_clone.features.social.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import coil.Coil.imageLoader
import coil.compose.AsyncImage
@Composable
fun String.toImage(){
   return AsyncImage(
        model = this,
        contentDescription = null,
    )
}