package com.example.tiktok_clone.features.search.ui


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.R
import com.example.tiktok_clone.features.search.model.VideoResult
import com.example.tiktok_clone.features.search.ui.components.VideoResultItem

@Composable
fun VideoResultTab() {
    val videos = listOf(
        VideoResult(R.drawable.video_1, "Nghịch Thủy Hàn collab Kinh Kịch", "Michi", 1200),
        VideoResult(R.drawable.video_2, "Ngoại trang 2880 bá vương biệt cơ", "BiBi", 980),
        VideoResult(R.drawable.video_3, "Combat ingame quá đã", "GameZone", 2100)
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(videos) {
            VideoResultItem(video = it)
        }
    }
}