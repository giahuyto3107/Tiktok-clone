package com.example.tiktok_clone.features.social.ui.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.features.social.data.model.Comment
import com.example.tiktok_clone.features.social.ui.components.Avatar
import com.example.tiktok_clone.features.social.ui.components.formatCount
import com.example.tiktok_clone.features.social.ui.components.toDateString
import com.example.tiktok_clone.R.dimen
import com.example.tiktok_clone.features.social.data.model.SocialAction
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import com.example.tiktok_clone.ui.theme.RedHeart
import org.koin.androidx.compose.koinViewModel
import com.example.tiktok_clone.ui.theme.TextPrimaryGray
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
    modifier: Modifier = Modifier,
    viewModel: SocialViewModel = koinViewModel(),
    commentRoot: Comment,
    isRoot: Boolean = true,
) {

    var replyCount by remember { mutableLongStateOf(commentRoot.replyCount) }
    val user = viewModel.getUser(commentRoot.userId)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Avatar(
            avatarUrl = user.avatarUrl,
            avatarSize = if (isRoot) 40 else 30,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            CommentContent(
                user = user,
                comment = commentRoot
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = commentRoot.createdAt.toDateString(),
                    fontWeight = FontWeight.Light,
                    fontSize = 10.sp,
                    lineHeight = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Trả lời",
                    modifier = Modifier
                        .clickable(onClick = {}),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.weight(1f))
                CommentReact(
                    viewModel = viewModel,
                    commentRoot = commentRoot
                )
            }
            CommentChild(
                replyCount = replyCount,
                viewModel = viewModel,
                commentRoot = commentRoot,
            )
        }
    }
}

@Composable
fun CommentChild(
    replyCount: Long,
    viewModel: SocialViewModel = koinViewModel(),
    commentRoot: Comment,
) {
    var isShowReply by remember { mutableStateOf(false) }
    var isReply by remember { mutableStateOf(false) }
    if (replyCount > 0) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = (-25).dp),
            horizontalAlignment = Alignment.Start
        ) {
            if (isShowReply) {
                val replies: List<Comment> = viewModel.getReply(commentRoot.id)
                for (reply in replies) {
                    CommentLine(
                        commentRoot = reply,
                        isRoot = false
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.Start,
            ) {
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height(0.5.dp)
                        .background(TextPrimaryGray.copy(alpha = 0.5f))
                )
                Text(
                    text = if (isReply)
                        "Ẩn"
                    else
                        "Xem ${formatCount(replyCount)} câu trả lời",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimaryGray,
                    modifier = Modifier
                        .clickable(onClick = {
                            isReply = !isReply
                            isShowReply = !isShowReply
                        }),
                )
            }
        }
    }
}

@Composable
fun CommentReact(
    viewModel: SocialViewModel = koinViewModel(),
    commentRoot: Comment,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        var isLiked by remember { mutableStateOf(commentRoot.isLiked) }
        var likeCount by remember { mutableLongStateOf(commentRoot.likeCount) }
        var isReply by remember { mutableStateOf(false) }
        var isDislike by remember { mutableStateOf(false) }
        Icon(
            imageVector = if (isLiked)
                FontAwesomeIcons.Solid.Heart
            else
                FontAwesomeIcons.Regular.Heart,
            contentDescription = null,
            tint = if (isLiked)
                RedHeart
            else
                TextPrimaryGray,
            modifier = Modifier
                .size(16.dp)
                .clickable(
                    onClick = {
                        viewModel.onAction(
                            SocialAction.LikeComment(commentRoot.id)
                        )
                        isLiked = !isLiked
                        if (isLiked) {
                            likeCount++
                            isDislike = false
                        } else likeCount = maxOf(0, likeCount - 1)

                    }
                )
        )
        Text(
            text = if (likeCount > 0) {
                formatCount(likeCount)
            } else "",
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
            imageVector = if (isDislike)
                FontAwesomeIcons.Solid.ThumbsDown
            else
                FontAwesomeIcons.Regular.ThumbsDown,
            contentDescription = null,
            tint = TextPrimaryGray,
            modifier = Modifier
                .graphicsLayer(
                    scaleX = -1f
                )
                .padding(1.dp)
                .size(16.dp)
                .clickable(onClick = {
                    isDislike = !isDislike
                    if (isDislike && isLiked) {
                        isLiked = false
                        likeCount = maxOf(0, likeCount - 1)
                        viewModel.onAction(
                            SocialAction.LikeComment(commentRoot.id)
                        )

                    }
                }
                )
        )
    }
}

@Composable
fun CommentContent(
    user: User,
    comment: Comment
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = user.userName,
            fontWeight = FontWeight.Medium,
            fontSize = dimensionResource(dimen.font_xs).value.sp,
            color = TextPrimaryGray,

            style = TextStyle(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            ),
            modifier = Modifier
                .offset(y = (-3).dp)
        )
        Text(
            text = comment.content,
            fontSize = dimensionResource(dimen.font_m).value.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}