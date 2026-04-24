package com.example.tiktok_clone.features.social.ui.comment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.features.social.data.model.Comment

@Composable
// Render list comment (root + replies)
fun CommentList(
    modifier: Modifier = Modifier,
    comments: List<Comment>,
    parent: (Comment) -> Unit = {},
    onReply: (Boolean) -> Unit = {},
    onCommentClick: (String) -> Unit = {},
    listState: LazyListState = rememberLazyListState(),
) {
    val commentRoots = remember(comments) {
        comments.filter { it.parentId == null }
    }
    val repliesByParent = remember(comments) {
        comments.filter { it.parentId != null }.groupBy({ it.parentId!! }, { it })
    }
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .nestedScroll(rememberNestedScrollInteropConnection())
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = commentRoots,
            key = { it.id },
        ) { root ->
            CommentLine(
                repliesByParent = repliesByParent,
                commentRoot = root,
                onReply = onReply,
                parent = parent,
                onCommentClick = onCommentClick
            )
        }
    }
}
