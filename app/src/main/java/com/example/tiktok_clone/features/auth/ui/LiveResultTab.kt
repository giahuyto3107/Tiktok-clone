package com.example.tiktok_clone.features.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.R

@Composable
fun LiveResultTab() {
    val lives = listOf(
        R.drawable.video_1,
        R.drawable.video_1,
        R.drawable.video_1
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(lives) {
            Box {
                Image(
                    painter = painterResource(it),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(9f / 16f)
                )
                Text(
                    "LIVE",
                    color = Color.White,
                    modifier = Modifier
                        .background(Color.Red)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                        .align(Alignment.TopStart)
                )
            }
        }
    }
}