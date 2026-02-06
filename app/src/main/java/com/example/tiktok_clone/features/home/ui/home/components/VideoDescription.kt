package com.example.tiktok_clone.features.home.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VideoDescriptionSection(
    userName: String,
    description: String?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier
            .padding(bottom = 4.dp)
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = userName,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
        Text(
            text = description ?: "",
            fontSize = 14.sp,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White
        )
    }
}