package com.example.tiktok_clone.features.home.home.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.example.tiktok_clone.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.features.post.data.model.Post
import com.example.tiktok_clone.features.post.viewmodel.PostViewModel
import org.koin.androidx.compose.koinViewModel
import com.example.tiktok_clone.features.social.model.User
import com.example.tiktok_clone.features.social.ui.share.ShareSheetContent
import com.example.tiktok_clone.features.social.ui.comment.CommentSheetContent
import com.example.tiktok_clone.features.social.ui.components.Avatar
import com.example.tiktok_clone.features.social.ui.components.formatCount
import com.example.tiktok_clone.features.social.model.SocialAction
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import com.example.tiktok_clone.ui.theme.RedHeart
import com.example.tiktok_clone.ui.theme.YellowSave
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Bookmark
import compose.icons.fontawesomeicons.solid.CommentDots
import compose.icons.fontawesomeicons.solid.Heart
import compose.icons.fontawesomeicons.solid.Share


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiddleSection(
    modifier: Modifier = Modifier,
    author: User,
    currentPost: Post,
    postViewModel: PostViewModel = koinViewModel(),
    socialViewModel: SocialViewModel = koinViewModel(),
) {
    var isLiked by remember { mutableStateOf(false) }
    var likeCount by remember { mutableLongStateOf(currentPost.likeCount) }
    var saveCount by remember { mutableLongStateOf(currentPost.saveCount) }
    val commentCount by remember { mutableLongStateOf(currentPost.commentCount) }
    var isSaved by remember { mutableStateOf(false) }
    var isOpenCommentSheet by remember { mutableStateOf(false) }
    var isOpenShareSheet by remember { mutableStateOf(false) }
    var isFollowing by remember { mutableStateOf(author.isFollowing) }

    Column(
        modifier = Modifier
            .fillMaxHeight(0.45f)
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        AuthorSection(
            avtarUrl = author.avatarUrl,
            isFollowing = isFollowing,
            onClick = {
//                isFollowing = !isFollowing
//                postViewModel.onSocialAction(SocialAction
//                    .Follow(author.id, author.id)
//                )
                null
            }
        )
        Spacer(modifier = Modifier.size(16.dp))
        MainInteractiveItem(
            icon = FontAwesomeIcons.Solid.Heart,
            tint = if (isLiked) RedHeart else Color.White.copy(alpha = 0.9f),
            name = "Love",
            numberOfInteraction = likeCount,
            onClick = {
                isLiked = !isLiked
                likeCount = if (isLiked) likeCount + 1 else likeCount - 1
            }
        )
        Spacer(modifier = Modifier.size(16.dp))

        MainInteractiveItem(
            icon = FontAwesomeIcons.Solid.CommentDots,
            name = "Comment",
            numberOfInteraction = commentCount,
            onClick = {
                isOpenCommentSheet = true
            }

        )
        if (isOpenCommentSheet) {
            CommentSheetContent(
                currentPost = currentPost,
                currentUser = author,
                onDismiss = {
                    isOpenCommentSheet = false
                }
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        MainInteractiveItem(
            icon = FontAwesomeIcons.Solid.Bookmark,
            tint = if (isSaved) YellowSave else Color.White.copy(alpha = 0.9f),
            name = "Save",
            numberOfInteraction = saveCount,
            onClick = {
                isSaved = !isSaved
                saveCount = if (isSaved) saveCount + 1 else saveCount - 1
            }
        )
        Spacer(modifier = Modifier.size(16.dp))

        MainInteractiveItem(
            icon = FontAwesomeIcons.Solid.Share,
            name = "Share",
            numberOfInteraction = 4302,
            onClick = {
                isOpenShareSheet = true
            }
        )
        if (isOpenShareSheet) {
            ShareSheetContent(
                currentUser = author,
                onDismiss = {
                    isOpenShareSheet = false
                }
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
    }
}


@Composable
fun MainInteractiveItem(
    icon: ImageVector,
    numberOfInteraction: Long,
    name: String,
    modifier: Modifier = Modifier,
    tint: Color = Color.White.copy(alpha = 0.9f),
    onClick: () -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .then(modifier)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = name,
            tint = tint,
            modifier = Modifier
                .size(size = dimensionResource(R.dimen.font_title_m))
                .clickable(onClick = onClick)

        )
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
        Box(
            modifier = Modifier
                .border(0.2.dp, Color.LightGray, CircleShape)
                .size(45.dp)
                .clip(CircleShape),
        ) {
            Avatar(
                avatarUrl = avtarUrl ?: "",
                modifier = Modifier
                    .matchParentSize()
            )
        }
        Box(
            modifier = Modifier
                .padding(top = 40.dp)
                .background(
                if (isFollowing) Color.White else RedHeart,
                CircleShape)
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