package com.example.tiktok_clone.features.social.fakeData

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Cast
import androidx.compose.material.icons.outlined.FileDownload
import com.example.tiktok_clone.features.social.model.ShareAcction

object FakeDataShareAcction {
    val shareAcctions = listOf(
        ShareAcction(
            icon = Icons.Filled.Flag,
            appName = "Đăng lại",
            onClick = {}
        ),
        ShareAcction(
            icon = Icons.Filled.HeartBroken,
            appName = "Không quan tâm",
            onClick = {}
        ),
        ShareAcction(
            icon = Icons.Outlined.FileDownload,
            appName = "Tải về",
            onClick = {}
        ),
        ShareAcction(
            icon = Icons.Outlined.AddCircleOutline,
            appName = "Thêm vào nhật ký",
            onClick = {}
        ),
        ShareAcction(
            icon = Icons.Outlined.Cast,
            appName = "Chiếu",
            onClick = {}
        ),
        ShareAcction(
            icon = Icons.Filled.Speed,
            appName = "Tốc độ phát lại",
            onClick = {}
        ),
    )
}