package com.example.tiktok_clone.features.social.ui.share

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmotionRow(
    onSelect: (String) -> Unit
){
    val emotions: List<String> = listOf("👍", "❤️", "😂", "😮", "😢", "😡","😎")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 14.dp, end = 14.dp)
        ,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        emotions.forEach { emotion ->
            Text(
                text = emotion,
                fontSize = 24.sp,
                modifier = Modifier
                    .clickable { onSelect(emotion) },
            )
        }
    }
}