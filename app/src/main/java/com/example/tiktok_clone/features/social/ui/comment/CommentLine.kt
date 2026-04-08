package com.example.tiktok_clone.features.social.ui.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.tiktok_clone.features.social.data.model.Comment
import com.example.tiktok_clone.features.social.ui.components.Avatar
import com.example.tiktok_clone.features.social.ui.components.formatCount
import com.example.tiktok_clone.features.social.ui.components.toDateString
import com.example.tiktok_clone.R.dimen
import com.example.tiktok_clone.features.social.data.model.SocialAction
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import org.koin.androidx.compose.koinViewModel
import com.example.tiktok_clone.ui.theme.TextPrimaryGray
import com.example.tiktok_clone.ui.theme.TikTokRed
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.Heart
import compose.icons.fontawesomeicons.regular.ThumbsDown
import compose.icons.fontawesomeicons.solid.Heart
import compose.icons.fontawesomeicons.solid.ThumbsDown


// hàm load 1 dòng comment
@Composable
fun CommentLine(
    viewModel: SocialViewModel = koinViewModel(),
    repliesByParent: Map<String, List<Comment>>,
    commentRoot: Comment,
    isRoot: Boolean = true,
    parent: (Comment) -> Unit = {},
    onReply: (Boolean) -> Unit = {},
    onCommentClick: (String) -> Unit = {},
) {
    var isReply by remember { mutableStateOf(false) }
    val user = viewModel.getUser(commentRoot.userId)
    val replies = repliesByParent[commentRoot.id].orEmpty()
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Avatar(
                avatarUrl = user.avatarUrl,
                avatarSize = if (isRoot) 40 else 30,
                modifier = Modifier.clickable {
                    onCommentClick(commentRoot.userId)
                })
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                CommentContent(
                    user = user,
                    comment = commentRoot,
                    onCommentClick = onCommentClick
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = commentRoot.createdAt.toDateString(),
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .wrapContentWidth()
                    )
                    Text(
                        text = if (isRoot) "Trả lời" else "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {
                                    onReply(true)
                                    parent(commentRoot)
                                    isReply = true
                                },
                            ),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    CommentReact(
                        viewModel = viewModel, commentRoot = commentRoot
                    )
                }
            }
        }
        CommentChild(
            replyCount = commentRoot.replyCount,
            replies = replies,
            repliesByParent = repliesByParent,
            isReply = isReply,
            parent = parent,
            onReply = onReply,
            onCommentClick = onCommentClick
        )
    }
}

@Composable
fun CommentChild(
    replyCount: Long,
    replies: List<Comment>,
    repliesByParent: Map<String, List<Comment>>,
    isReply: Boolean = false,
    parent: (Comment) -> Unit = {},
    onReply: (Boolean) -> Unit = {},
    onCommentClick: (String) -> Unit = {}
) {
    var isExpanded by remember(isReply) { mutableStateOf(isReply) }
    if (replyCount <= 0) return
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 35.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (isExpanded) {
            replies.forEach { reply ->
                CommentLine(
                    repliesByParent = repliesByParent,
                    commentRoot = reply,
                    isRoot = false,
                    parent = parent,
                    onReply = onReply,
                    onCommentClick = onCommentClick
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(24.dp)
                    .height(0.5.dp)
                    .background(TextPrimaryGray.copy(alpha = 0.5f))
            )
            Text(
                text = if (isExpanded) "Ẩn" else "Xem ${formatCount(replyCount)} câu trả lời",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryGray,
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        isExpanded = !isExpanded
                    }
                )
            )
        }
    }
}

@Composable
fun CommentReact(
    viewModel: SocialViewModel = koinViewModel(),
    commentRoot: Comment,
) {
    var isLiked by remember { mutableStateOf(commentRoot.isLiked) }
    var likeCount by remember { mutableLongStateOf(commentRoot.likeCount) }
    var isDislike by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = if (isLiked) FontAwesomeIcons.Solid.Heart else FontAwesomeIcons.Regular.Heart,
            contentDescription = null,
            tint = if (isLiked) TikTokRed else TextPrimaryGray,
            modifier = Modifier
                .size(16.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        viewModel.onAction(SocialAction.LikeComment(commentRoot.id))
                        isLiked = !isLiked
                        likeCount = if (isLiked) likeCount + 1 else maxOf(0, likeCount - 1)
                        if (isLiked) isDislike = false
                    }
                )
        )
        Text(
            text = if (likeCount > 0) formatCount(likeCount) else "",
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimaryGray,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .width(40.dp)
        )
        Icon(
            imageVector = if (isDislike) FontAwesomeIcons.Solid.ThumbsDown else FontAwesomeIcons.Regular.ThumbsDown,
            contentDescription = null,
            tint = TextPrimaryGray,
            modifier = Modifier
                .graphicsLayer(scaleX = -1f)
                .padding(1.dp)
                .size(16.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        isDislike = !isDislike
                        if (isDislike && isLiked) {
                            isLiked = false
                            likeCount = maxOf(0, likeCount - 1)
                            viewModel.onAction(SocialAction.LikeComment(commentRoot.id))
                        }
                    }
                )
        )
    }
}

@Composable
fun CommentContent(
    user: User,
    comment: Comment,
    onCommentClick: (String) -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        var isFullScreen by remember { mutableStateOf(false) }
        Text(
            text = user.userName,
            fontWeight = FontWeight.Bold,
            fontSize = dimensionResource(dimen.font_xs).value.sp,
            color = TextPrimaryGray,
            style = TextStyle(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false // bỏ khoảng trắng padding mặc định
                )
            ),
            modifier = Modifier
                .offset(y = (-3).dp)
                .clickable {
                    onCommentClick(user.id)
                }
        )
        if (comment.content.isNotEmpty()) {
            Text(
                text = comment.content,
                fontSize = dimensionResource(dimen.font_m).value.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
        if (!comment.imageUri.isNullOrBlank()) {
            AsyncImage(
                model = comment.imageUri,
                contentDescription = "Comment image",
                modifier = Modifier
                    .fillMaxSize(0.3f)
                    .padding(top = 4.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = { isFullScreen = true }
                    ),
            )
            if (isFullScreen) {
                Dialog(
                    onDismissRequest = { isFullScreen = false },
                    properties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = { isFullScreen = false })
                    ) {
                        AsyncImage(
                            model = comment.imageUri,
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }

    }
}