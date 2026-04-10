package com.example.tiktok_clone.features.search.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.features.search.model.VideoResult
import com.example.tiktok_clone.features.search.ui.components.VideoResultItem
import com.example.tiktok_clone.features.search.ui.components.ImageResultItem

@Composable
fun TopResultTab(
    videos: List<VideoResult>,
    images: List<String>,
    onClickVideo: (VideoResult) -> Unit = {},
    onClickImage: (String) -> Unit = {}
) {

    if (videos.isEmpty() && images.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Không có kết quả")
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
            Box(Modifier.clickable { onClickVideo(video) }) {
                VideoResultItem(video)
            }
        }

        items(images.size, key = { "top_img_$it" }) { i ->
            val image = images[i]
            Box(Modifier.clickable { onClickImage(image) }) {
                ImageResultItem(image)
            }
        }
    }
}