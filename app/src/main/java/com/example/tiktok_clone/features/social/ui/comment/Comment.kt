package com.example.tiktok_clone.features.social.ui.comment

import android.util.Log

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalView
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
import com.example.tiktok_clone.features.social.data.model.SocialAction
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
    onCommentClick: (String) -> Unit = {},
) {

    var isCommenting by remember { mutableStateOf(false) }
    var commentRoot by remember { mutableStateOf<Comment?>(null) }
    var isSort by remember { mutableStateOf(false) }
    var isTimeSort by remember { mutableStateOf(false) }

    val postId = currentPost.id.toString()
    val socialUiState by socialViewModel.uiState.collectAsState()
    val commentUiState = socialUiState.toCommentUiState(postId)

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(postId) {
        socialViewModel.onAction(SocialAction.LoadComment(postId))
    }
    val listState = remember(postId, isTimeSort) { LazyListState() }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        dragHandle = null,
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
                    commentCount = (commentUiState as? CommentUiState.Success)?.comments
                        ?.size
                        ?.toLong() ?: 0L,
                    onClose = {
                        onDismiss()
                        isSort = false
                        isTimeSort = false
                    },
                    onSort = { isSort = !isSort }
                )

                when (commentUiState) {
                    CommentUiState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is CommentUiState.Error -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = commentUiState.message,
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }

                    is CommentUiState.Success -> {
                        val comments = remember(commentUiState.comments, isTimeSort) {
                            if (isTimeSort)
                                commentUiState.comments.sortedByDescending { it.createdAt }
                            else
                                commentUiState.comments.sortedByDescending { it.likeCount }
                        }
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
                                    modifier = Modifier.size(180.dp),
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
                                hasMore = commentUiState.hasMore,
                                onLoadMore = {
                                    socialViewModel.onAction(
                                        SocialAction.LoadMoreComment(postId)
                                    )
                                },
                                onReply = { value -> isCommenting = value },
                                parent = { value -> commentRoot = value },
                                listState = listState,
                                onCommentClick = onCommentClick,

                            )
                        }
                    }
                }
                CommentBottomBar(
                    post = currentPost,
                    currentUser = currentUser,
                    onReply = isCommenting,
                    commentRoot = commentRoot
                )
            }
            if (isSort) {
                SortCard(
                    modifier = Modifier
                        .align(Alignment.TopEnd),
                    isTimeSort = isTimeSort,
                    onSelect = {
                        if (it != isTimeSort) {
                            isTimeSort = it
                            socialViewModel.onAction(SocialAction.LoadComment(postId))
                        }
                        isSort = false
                    }
                )
            }
        }
    }
}

@Composable
private fun SortCard(
    isTimeSort: Boolean,
    onSelect: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = listOf("Hàng đầu" to false, "Mới nhất" to true)

    Card(
        modifier = Modifier
            .offset(x = (-16).dp, y = 40.dp)
            .wrapContentSize()
            .shadow(
                elevation = (20).dp,
                shape = RoundedCornerShape(8.dp)
            )
            .then(modifier)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .background(Color.White)
        ) {
            options.forEach { (label, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSelect(
                                value
                            )
                        }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        label,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    if (isTimeSort == value)
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                }
            }
        }
    }
}
