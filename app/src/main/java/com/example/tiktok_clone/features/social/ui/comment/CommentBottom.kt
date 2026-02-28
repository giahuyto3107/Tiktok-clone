package com.example.tiktok_clone.features.social.ui.comment

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiktok_clone.features.post.data.model.Post
import com.example.tiktok_clone.features.social.model.User
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel

// bottom bar comment
@Composable
fun CommentBottomBar(
    modifier: Modifier = Modifier,
    viewModel: SocialViewModel = viewModel(),
    post: Post,
    user: User,

    ) {
    var isCommenting by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        tonalElevation = 4.dp,
        shadowElevation = 8.dp

    ) {
        CommentInput(
            viewModel = viewModel,
            modifier = Modifier,
            post = post,
            user = user,
            isCommenting = isCommenting,
            onDismiss = { isCommenting = false },
            onCommenting = { isCommenting=true }
        )
    }
}