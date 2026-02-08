package com.example.tiktok_clone.features.auth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ShopResultTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Không có kết quả", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            "Thử từ khóa khác và xem bạn tìm thấy gì!",
            fontSize = 13.sp,
            color = Color.Gray
        )
    }
}