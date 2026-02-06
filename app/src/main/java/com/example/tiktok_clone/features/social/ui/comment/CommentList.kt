package com.example.tiktok_clone.features.social.ui.comment

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.tiktok_clone.features.social.model.Comment

@Composable
fun CommentList(
    modifier: Modifier = Modifier,
    comments: List<Comment>,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        items(comments.size) { index ->
            CommentLine(
                comment = comments[index],
            )
        }
    }
}