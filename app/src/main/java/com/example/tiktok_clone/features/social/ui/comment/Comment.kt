package com.example.tiktok_clone.features.social.ui.comment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiktok_clone.features.social.model.Post
import com.example.tiktok_clone.features.social.model.User
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel

// toàn bộ commnet sheet
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentSheetContent(
    currentPost: Post,
    currentUser: User,
    viewModel: SocialViewModel = viewModel(),
    onDismiss: () -> Unit,
) {
    val comments = viewModel.getComment(currentPost.id)
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        dragHandle = null, //{ BottomSheetDefaults.DragHandle() },
        modifier = Modifier


    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
//                .heightIn(max = maxHeight)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 75.dp)

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
            }
            CommentBottomBar(
                viewModel = viewModel,
                post = currentPost,
                user = currentUser,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            )
        }
    }
}










