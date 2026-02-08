package com.example.tiktok_clone.features.auth.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchResultScreen(
    query: String,
    onBack: () -> Unit
) {
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Video", "Người dùng", "Âm thanh", "Hashtag")

    // 🔥 Danh sách ảnh (LẤY TỪ res/drawable)
    val videos: List<Int> = listOf(
        R.drawable.video_1,
        R.drawable.video_2,
        R.drawable.video_3

    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {

        // ===== HEADER =====
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier.clickable { onBack() }
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = query,
                fontSize = 16.sp
            )
        }

        // ===== TAB =====
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    text = {
                        Text(text = title, fontSize = 13.sp)
                    }
                )
            }
        }

        // ===== GRID VIDEO =====
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(1.dp)
        ) {
            items(items = videos) { videoRes ->
                Box(
                    modifier = Modifier
                        .aspectRatio(9f / 16f)
                        .padding(1.dp)
                ) {
                    Image(
                        painter = painterResource(id = videoRes),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}