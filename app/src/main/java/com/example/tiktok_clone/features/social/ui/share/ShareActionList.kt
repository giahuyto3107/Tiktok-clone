package com.example.tiktok_clone.features.social.ui.share

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.features.post.data.model.Post
import com.example.tiktok_clone.features.social.data.model.User

@Composable
fun ShareActionList(
    currentPost: Post,
    currentUser: User?,
    isShared: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        ShareActions(
            currentPost = currentPost,
            isShared = isShared
        )
        PostOptions(
            currentPost = currentPost,
            currentUser = currentUser,
            isShared = isShared
        )
    }
}