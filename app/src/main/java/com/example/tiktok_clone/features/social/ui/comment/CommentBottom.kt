package com.example.tiktok_clone.features.social.ui.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.tiktok_clone.features.post.data.model.Post
import com.example.tiktok_clone.features.social.data.model.Comment
import com.example.tiktok_clone.features.social.data.model.User

// bottom bar comment
@Composable
fun CommentBottomBar(
    post: Post,
    currentUser: User?,
    onReply: Boolean,
    commentRoot: Comment?
) {
    var isCommenting by remember(onReply) { mutableStateOf(onReply) }
    CommentInput(
        postId = post.id.toString(),
        currentUser = currentUser,
        commentRoot = commentRoot,
        isCommenting = isCommenting,
        onDismiss = {
            isCommenting = false
            isCommenting
        },
        onCommenting = {
            isCommenting = true
            isCommenting
        }
    )
}
