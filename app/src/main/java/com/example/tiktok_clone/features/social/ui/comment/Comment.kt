package com.example.tiktok_clone.features.social.ui.comment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.features.post.data.model.Post
import com.example.tiktok_clone.features.post.ui.UploadState
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentSheetContent(
    currentPost: Post,
    currentUser: User?,
    socialViewModel: SocialViewModel = koinViewModel(),
    onDismiss: () -> Unit,
) {
    val comments = socialViewModel.getComment(currentPost.id.toString())
    val uploadState by socialViewModel.uploadState.collectAsState()
    val listState = rememberLazyListState()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    LaunchedEffect(uploadState) {
        if (uploadState is UploadState.Success)
            socialViewModel.getComment(currentPost.id.toString())
    }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        dragHandle = null,
        modifier = Modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.65f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                CommentHeader(
                    commentCount = comments.size.toLong(),
                    search = "Search",
                    onClose = onDismiss,
                )
                CommentList(
                    modifier = Modifier
                        .weight(1f)
                        .nestedScroll(rememberNestedScrollInteropConnection()),
                    comments = comments
                )
                CommentBottomBar(
                    post = currentPost,
                    currentUser = currentUser,
                )
            }
        }
    }
}










