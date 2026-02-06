package com.example.tiktok_clone.features.social.ui.share

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.features.social.model.ShareCategory
import com.example.tiktok_clone.features.social.model.ShareItem

@Composable
fun ShareActionList(
    items: List<ShareItem>,
    onActionClick: (ShareItem) -> Unit
){
    Column(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Box() {
            ShareActionRow(items = items, category = ShareCategory.APP, onActionClick)
        }
        Box() {
            ShareActionRow(items = items, category = ShareCategory.REPORT, onActionClick)
        }
    }
}