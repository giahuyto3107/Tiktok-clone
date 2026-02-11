package com.example.tiktok_clone.features.search.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SuggestionItem(
    text: String,
    onClick: () -> Unit,
    onRemove: (() -> Unit)? = null // 👈 cho lịch sử search
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }   // 👈 BẤM CẢ DÒNG
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Default.History,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = text,
            modifier = Modifier.weight(1f)
        )

        if (onRemove != null) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove",
                modifier = Modifier
                    .size(18.dp)
                    .clickable { onRemove() }
            )
        }
    }
}