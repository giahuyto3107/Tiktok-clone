package com.example.tiktok_clone.features.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.R

@Composable
fun ImageResultTab() {
    val images = listOf(
        R.drawable.video_1,
        R.drawable.video_1,
        R.drawable.video_1,
        R.drawable.video_1
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(images) {
            Image(
                painter = painterResource(it),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
        }
    }
}