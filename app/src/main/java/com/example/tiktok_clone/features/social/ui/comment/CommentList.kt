package com.example.tiktok_clone.features.social.ui.comment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.features.social.data.model.Comment

@Composable
fun CommentList(
    modifier: Modifier = Modifier,
    comments: List<Comment>,
) {
    val commentRoot = comments.filter { it.parentId == null }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(commentRoot.size) { index ->
            CommentLine(
                commentRoot = commentRoot[index],
            )
        }
    }
}