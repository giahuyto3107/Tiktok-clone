package com.example.tiktok_clone.features.social.fakeData

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Cast
import androidx.compose.material.icons.outlined.FileDownload
import com.example.tiktok_clone.features.social.model.ShareAcction

object FakeShareAcctionData {
    val shareAcctions = listOf(
        ShareAcction(
            icon = Icons.Filled.Flag,
            appName = "Báo cáo",
            industryType = "report",
        ),
        ShareAcction(
            icon = Icons.Filled.HeartBroken,
            appName = "Không quan tâm",
            industryType = "not_interested"
        ),
        ShareAcction(
            icon = Icons.Outlined.FileDownload,
            appName = "Tải về",
            industryType = "download"
        ),
        ShareAcction(
            icon = Icons.Outlined.AddCircleOutline,
            appName = "Thêm vào nhật ký",
            industryType = "add_to_story"
        ),
        ShareAcction(
            icon = Icons.Outlined.Cast,
            appName = "Chiếu",
            industryType = "cast"
        ),
        ShareAcction(
            icon = Icons.Filled.Speed,
            appName = "Tốc độ phát lại",
            industryType = "speed"
        ),
    )
}