package com.example.tiktok_clone.features.social.ui.comment

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel

// bottom bar comment
@Composable
fun CommentBottomBar(
    viewModel: SocialViewModel = viewModel(),
    modifier: Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        tonalElevation = 4.dp,
        shadowElevation = 8.dp

    ) {
        CommentInput(
            viewModel = viewModel,
            modifier = Modifier
        )
    }
}