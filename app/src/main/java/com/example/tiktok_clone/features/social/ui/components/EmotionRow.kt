package com.example.tiktok_clone.features.social.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.ui.theme.GrayIcon

@Composable
fun EmotionRow(
    onSelect: (String) -> Unit
) {
    val emotions: List<String> = listOf("👍", "❤️", "😂", "😮", "😢", "😡", "😎")
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(emotions.size) {index->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color.LightGray.copy(0.4f))
                    .padding(horizontal = 15.dp, vertical = 3.dp)
                    .clickable{onSelect(emotions[index])}
            ) {
                Text(
                    text = emotions[index],
                    fontSize = 16.sp,
                )
            }
        }

    }
}