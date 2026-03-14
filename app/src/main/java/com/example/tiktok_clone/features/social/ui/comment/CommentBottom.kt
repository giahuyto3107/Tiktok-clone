package com.example.tiktok_clone.features.social.ui.comment

import android.util.Log
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
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel

// bottom bar comment
@Composable
fun CommentBottomBar(
    post: Post,
    currentUser: User?,
    ) {
    var isCommenting by remember { mutableStateOf(false) }
        CommentInput(
            post = post,
            currentUser = currentUser,
            isCommenting = isCommenting,
            onDismiss = { isCommenting = false },
            onCommenting = { isCommenting=true }
        )
}