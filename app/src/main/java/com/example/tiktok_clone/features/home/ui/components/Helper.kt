package com.example.tiktok_clone.features.home.ui.components


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.Log
import com.example.tiktok_clone.features.post.data.model.Post
import com.example.tiktok_clone.features.social.data.PostStateResponse
import com.example.tiktok_clone.features.social.data.model.SocialAction
import org.koin.androidx.compose.koinViewModel
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.features.social.ui.share.ShareSheetContent
import com.example.tiktok_clone.features.social.ui.comment.CommentSheetContent
import com.example.tiktok_clone.features.social.ui.components.Avatar
import com.example.tiktok_clone.features.social.ui.components.formatCount
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import com.example.tiktok_clone.ui.theme.RedHeart
import com.example.tiktok_clone.ui.theme.YellowSave


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiddleSection(
    modifier: Modifier = Modifier,
    author: User,
    currentPost: Post,
    currentUser: User?,
    postState: PostStateResponse?,
    socialViewModel: SocialViewModel = koinViewModel(),
) {
    val thisPostState = postState?.takeIf { it.postId == currentPost.id.toString() }

    val isLiked = thisPostState?.isLiked == true
    val likeCount = thisPostState?.likeCount?.toLong() ?: 0L
    val isSaved = thisPostState?.isSaved == true
    val saveCount = thisPostState?.saveCount?.toLong() ?: 0L
    val commentCount = thisPostState?.commentCount?.toLong() ?: 0L
    val shareCount = thisPostState?.shareCount?.toLong() ?: 0L
    val isShared = thisPostState?.isShared == true

    var isFollowing by remember(author.id) {
        mutableStateOf(socialViewModel.isFollowing(author.id))
    }
    var isOpenCommentSheet by remember { mutableStateOf(false) }
    var isOpenShareSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AuthorSection(
            avtarUrl = author.avatarUrl,
            isFollowing = isFollowing,
            onClick = {
                isFollowing = !isFollowing
                socialViewModel.onAction(
                    SocialAction
                        .Follow(currentUser?.id.toString(), author.id)
                )
            }
        )
        // Like
        MainInteractiveItem(
            icon = Icons.Filled.Favorite,
            tint = if (isLiked) RedHeart else Color.White.copy(alpha = 0.9f),
            name = "Like",
            numberOfInteraction = likeCount,
            onClick = {
                socialViewModel.onAction(
                    SocialAction.Like(
                        postId = currentPost.id.toString(),
                    )
                )
            },
        )
        // Comment
        OpenComment(
            socialViewModel = socialViewModel,
            commentCount = commentCount,
            currentPost = currentPost,
            onComment = {
                isOpenCommentSheet = it
            }
        )
        if (isOpenCommentSheet) {
            CommentSheetContent(
                currentPost = currentPost,
                currentUser = currentUser,
                onDismiss = {
                    isOpenCommentSheet = false
                }
            )
        }
        // Save
        MainInteractiveItem(
            icon = Icons.Filled.Bookmark,
            tint = if (isSaved) YellowSave else Color.White.copy(alpha = 0.9f),
            name = "Save",
            numberOfInteraction = saveCount,
            onClick = {
                socialViewModel.onAction(
                    SocialAction.Save(currentPost.id.toString())
                )
            }
        )
        // Share
        MainInteractiveItem(
            icon = Icons.AutoMirrored.Filled.Reply,
            name = "Share",
            numberOfInteraction = shareCount,
            tint = Color.White,
            onClick = {
                isOpenShareSheet = true
            },
            modifier = Modifier
                .scale(scaleX = -1f, scaleY = 1.2f)
        )
        if (isOpenShareSheet) {
            ShareSheetContent(
                currentPost = currentPost,
                currentUser = currentUser,
                isShared = isShared,
                onDismiss = {
                    isOpenShareSheet = false
                }
            )

        }
    }
}

@Composable
fun OpenComment(
    socialViewModel: SocialViewModel = koinViewModel(),
    commentCount: Long,
    currentPost: Post,
    onComment: (isOpenCommentSheet: Boolean) -> Unit = {}
) {
    // Comment
    Column(
        modifier = Modifier
            .clickable(
                onClick = {
                    onComment(true)
                    socialViewModel.onAction(
                        SocialAction.Comment(currentPost.id.toString())
                    )
                }
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(40.dp),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(
                modifier = Modifier
                    .size(15.dp)
                    .offset(x = 4.dp, y = (11).dp)
                    .scale(scaleX = 1f, scaleY = 0.9f)
                    .rotate(5f)
            ) {
                drawArc(
                    color = Color.White,
                    startAngle = 0f,
                    sweepAngle = 90f,
                    useCenter = true,
                    size = Size(size.width * 2, size.height * 2),
                    topLeft = Offset(-size.width, -size.height)
                )
            }
            Icon(
                imageVector = Icons.Filled.Pending,
                contentDescription = "Comment",
                tint = Color.White,
                modifier = Modifier
                    .scale(scaleX = 1f, scaleY = 0.85f)
                    .matchParentSize()
            )
        }
        Text(
            text = formatCount(commentCount),
            color = Color.White,
            fontSize = 12.sp,
            lineHeight = 12.sp,
        )
    }
}


@Composable
fun MainInteractiveItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    numberOfInteraction: Long,
    isShowText: Boolean = true,
    name: String,
    tint: Color = Color.White.copy(alpha = 0.9f),
    onClick: () -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = name,
            tint = tint,
            modifier = Modifier
                .size(size = 40.dp)
                .clickable(onClick = onClick)
                .then(modifier)

        )
        if (isShowText)
            Text(
                text = formatCount(numberOfInteraction),
                color = Color.White,
                fontSize = 12.sp,
                style = MaterialTheme.typography.labelSmall,
            )
    }
}

@Composable
fun AuthorSection(
    avtarUrl: String?,
    isFollowing: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Avatar(
            avatarUrl = avtarUrl ?: "",
            avatarSize = 45,
        )
        Box(
            modifier = Modifier
                .padding(top = 40.dp)
                .background(
                    if (isFollowing) Color.White else RedHeart,
                    CircleShape
                )
                .clickable(onClick = onClick)
                .size(22.dp),
            contentAlignment = Alignment.Center

        ) {
            Icon(
                imageVector = if (isFollowing) Icons.Filled.Check else Icons.Filled.Add,
                contentDescription = "Follow",
                tint = if (isFollowing) RedHeart else Color.White,
                modifier = Modifier
                    .size(20.dp)

            )
        }
    }
}