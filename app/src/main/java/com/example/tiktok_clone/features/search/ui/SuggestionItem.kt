package com.example.tiktok_clone.features.search.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
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
    onRemove: (() -> Unit)? = null,
    /** true = gợi ý từ server/DB; false = lịch sử / chủ đề cố định */
    isFromDatabase: Boolean = false,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            if (isFromDatabase) Icons.Default.Search else Icons.Default.History,
            contentDescription = null,
        )

        Spacer(Modifier.width(10.dp))

        Text(
            text = text,
            modifier = Modifier.weight(1f)
        )

        onRemove?.let {
            Icon(
                Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier.clickable { it() }
            )
        }
    }
}