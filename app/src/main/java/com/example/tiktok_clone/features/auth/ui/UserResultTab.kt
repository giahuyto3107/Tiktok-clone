package com.example.tiktok_clone.features.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UserResultTab() {
    val users = listOf(
        "Nghịch Thủy Hàn Official",
        "Chill Nghịch Thủy Hàn",
        "Nghịch Thủy Hàn Mobile",
        "Code Face Nghịch Thủy Hàn"
    )

    Column(modifier = Modifier.fillMaxSize()) {
        users.forEach {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {}
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color.LightGray, shape = CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(it, fontSize = 14.sp)
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = {}) {
                    Text("Follow", fontSize = 12.sp)
                }
            }
        }
    }
}