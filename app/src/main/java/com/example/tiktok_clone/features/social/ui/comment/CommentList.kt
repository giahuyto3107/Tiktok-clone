package com.example.tiktok_clone.features.social.ui.comment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.features.social.data.model.Comment
import com.example.tiktok_clone.features.social.ui.components.SetKeyboardOverlayMode

@Composable
fun CommentList(
    modifier: Modifier = Modifier,
    comments: List<Comment>,
    hasMore: Boolean,
    onLoadMore: () -> Unit,
    parent: (Comment) -> Unit = {},
    onReply: (Boolean) -> Unit = {},
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
            )
        }
        if (hasMore) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Xem thêm bình luận…",
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .clickable { onLoadMore() },
                    )
                }
            }
        }
    }
}
