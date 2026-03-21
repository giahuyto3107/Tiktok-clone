package com.example.tiktok_clone.features.social.ui.comment

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.R
import com.example.tiktok_clone.features.post.data.model.Post
import com.example.tiktok_clone.features.post.ui.UploadState
import com.example.tiktok_clone.features.social.data.model.Comment
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
    var isCommenting by remember { mutableStateOf(false) }
    var commentRoot by remember { mutableStateOf<Comment?>(null) }
    val allComments by socialViewModel.comments.collectAsState()
    val postId = currentPost.id.toString()
    val comments = remember(allComments, postId) {
        allComments.filter { it.postId == postId }
    }
    val uploadState by socialViewModel.uploadState.collectAsState()
    val commentHasMore by socialViewModel.commentHasMore.collectAsState()
    val hasMoreForPost = commentHasMore[currentPost.id.toString()] ?: true
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    LaunchedEffect(uploadState) {
        if (uploadState is UploadState.Success)
            socialViewModel.loadComments(currentPost.id.toString(), force = true)
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
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CommentHeader(
                    commentCount = comments.size.toLong(),
                    search = "Search",
                    onClose = onDismiss,
                )
                if (comments.isEmpty() && currentUser?.id != currentPost.userId) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.no_comment),
                            contentDescription = "No comment",
                            modifier = Modifier
                                .size(180.dp),
                        )
                        Text(
                            text = "Bạn thấy bài đăng này hay chứ? Hãy là người đầu tiên bình luận",
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                } else {
                    CommentList(
                        modifier = Modifier
                            .weight(1f)
                            .nestedScroll(rememberNestedScrollInteropConnection()),
                        comments = comments,
                        hasMore = hasMoreForPost,
                        onLoadMore = { socialViewModel.loadMoreComments(currentPost.id.toString()) },
                        onReply = { value -> isCommenting = value },
                        parent = { value -> commentRoot = value }
                    )
                }
                CommentBottomBar(
                    post = currentPost,
                    currentUser = currentUser,
                    onReply = isCommenting,
                    commentRoot = commentRoot
                )

            }
        }
    }
}










