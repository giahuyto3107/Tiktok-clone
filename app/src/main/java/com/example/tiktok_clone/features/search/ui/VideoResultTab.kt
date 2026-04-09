package com.example.tiktok_clone.features.search.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.features.search.model.VideoResult
import com.example.tiktok_clone.features.search.ui.components.VideoResultItem

@Composable
fun VideoResultTab(
    videos: List<VideoResult>
) {

    if (videos.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Không có video")
        }
        return
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(videos, key = { it.id }) { video ->
            VideoResultItem(video)
        }
    }
}