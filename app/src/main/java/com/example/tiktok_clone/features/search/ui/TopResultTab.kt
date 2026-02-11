package com.example.tiktok_clone.features.search.ui


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TopResultTab() {
    Column {
        Text("Video nổi bật", modifier = Modifier.padding(12.dp))
        VideoResultTab()
    }
}