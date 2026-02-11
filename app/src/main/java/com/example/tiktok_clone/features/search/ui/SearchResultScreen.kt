package com.example.tiktok_clone.features.search.ui


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchResultScreen(
    query: String,
    onBack: () -> Unit
) {
    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf(
        "Top", "Video", "Người dùng", "Cửa hàng", "Ảnh", "LIVE"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {

        // 🔙 HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier.clickable { onBack() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = query, fontSize = 16.sp)
        }

        // 🧭 TAB
        ScrollableTabRow(
            selectedTabIndex = tabIndex,
            edgePadding = 12.dp
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    text = { Text(title, fontSize = 13.sp) }
                )
            }
        }

        // 🔥 CONTENT THEO TAB
        when (tabIndex) {
            0 -> TopResultTab()
            1 -> VideoResultTab()
            2 -> UserResultTab()
            3 -> ShopResultTab()
            4 -> ImageResultTab()
            5 -> LiveResultTab()
        }
    }
}