package com.example.tiktok_clone.features.social.ui.share

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.Speed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.features.post.data.model.Post
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
// Render cac option khac trong sheet share
fun PostOptions(
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
        contentPadding = PaddingValues(start = 8.dp)
    ) {
        item {
            ShareActionItem(
                icon = Icons.Filled.Flag,
                iconName = "Báo cáo",
                iconSize = 28,
                iconColor = Color.Black.copy(0.6f),
                isText = false,
                backgroundColor = Color.LightGray.copy(0.5f),
                onClick = {}
            )
        }
        item {
            ShareActionItem(
                icon = Icons.Filled.HeartBroken,
                iconName = "Không quan tâm",
                iconSize = 25,
                iconColor = Color.Black.copy(0.6f),
                isText = false,
                backgroundColor = Color.LightGray.copy(0.5f),
                onClick = {}
            )
        }
        item {
            ShareActionItem(
                icon = Icons.Filled.Download,
                iconName = "Tải về",
                iconSize = 25,
                iconColor = Color.Black.copy(0.6f),
                isText = false,
                backgroundColor = Color.LightGray.copy(0.5f),
                onClick = {}
            )
        }
        item {
            ShareActionItem(
                icon = Icons.Filled.Cast,
                iconName = "Chiếu",
                iconSize = 25,
                iconColor = Color.Black.copy(0.6f),
                isText = false,
                backgroundColor = Color.LightGray.copy(0.5f),
                onClick = {}
            )
        }
        item {
            ShareActionItem(
                icon = Icons.Filled.Speed,
                iconName = "Tốc độ phát lại",
                iconSize = 25,
                iconColor = Color.Black.copy(0.6f),
                isText = false,
                backgroundColor = Color.LightGray.copy(0.5f),
                onClick = {}
            )
        }
        item {
            ShareActionItem(
                icon = Icons.AutoMirrored.Filled.Help,
                iconName = "tại sao bài đăng này hiển thị",
                iconNameSize = 8,
                iconSize = 25,
                iconColor = Color.Black.copy(0.6f),
                isText = false,
                backgroundColor = Color.LightGray.copy(0.5f),
                onClick = {}
            )
        }
    }
}