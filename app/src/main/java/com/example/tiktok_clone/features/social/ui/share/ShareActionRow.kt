package com.example.tiktok_clone.features.social.ui.share

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.features.social.model.ShareCategory
import com.example.tiktok_clone.features.social.model.ShareItem

@Composable
fun ShareActionRow(
    items: List<ShareItem>,
    category: ShareCategory,
    onActionClick: (ShareItem) -> Unit
){
    val items = items.filter { it.category == category }
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),

    ){
        items(items.size) { index ->
            if(items[index].category == category) {
                val item = items[index]
                ShareActionItem(itemUi = item.toUi(), onActionClick)
            }
        }
    }
}