package com.example.tiktok_clone.features.social.ui.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiktok_clone.features.social.model.Comment
import com.example.tiktok_clone.features.social.ui.components.Avatar
import com.example.tiktok_clone.features.social.ui.components.CommentItem
import com.example.tiktok_clone.features.social.ui.components.formatCount

import com.example.tiktok_clone.features.social.viewModel.SocialAction
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
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
    viewModel: SocialViewModel = viewModel(),
    comment: Comment,
) {
    var isLiked = comment.isLiked
    var likeCount = comment.likeCount
    var isReply by remember { mutableStateOf(false) }
    var isDislike by remember { mutableStateOf(false) }
    var isShowReply by remember { mutableStateOf(false) }
    var replyCount by remember { mutableStateOf(comment.replyCount) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Row(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.Top
        ) {
            //Avatar
            Box(
                modifier = Modifier
                    .border(
                        0.2.dp,
                        Color.LightGray.copy(alpha = 0.5f),
                        CircleShape
                    )
                    .size(45.dp)
                    .clip(CircleShape)

            ) {
                Avatar(
                    avatarUrl = comment.AvatarUrl,
                    modifier = Modifier
                        .matchParentSize()
                )
            }
            //Avatar
            Spacer(modifier = Modifier.width(8.dp))

            //Comment content
            Column(
                modifier = Modifier
                    .align(Alignment.Top)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                // user name
                Text(
                    text = comment.userName,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color.Gray.copy(
                        alpha = 0.9f
                    ),

                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                            // loại bỏ khoảng trống thừa trên đầu text
                        )
                    ),
                    modifier = Modifier
                        .offset(y = (-3).dp)
                    // Tinh chỉnh dòng lên trên -3, số xấu vl
                )
                // user name

                //Comment
                Text(
                    text = comment.commentContent,
                    fontSize = 18.sp,
                )
                //Comment
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    //Comment create at (time)
                    Text(
                        text = comment.commentTime,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp,
                        color = Color.Gray.copy(alpha = 0.9f)
                    )
                    //Comment create at (time)

                    Spacer(modifier = Modifier.width(18.dp))
                    //reply acction
                    Text(
                        text = "Trả lời",
                        modifier = Modifier
                            .clickable(onClick = {}),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = Color.Gray.copy(alpha = 0.9f)
                    )
                    //reply acction
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ) {

                        //like
                        CommentItem(
                            icon = if (isLiked)
                                FontAwesomeIcons.Solid.Heart
                            else
                                FontAwesomeIcons.Regular.Heart,
                            tint = if (isLiked)
                                Color.Red
                            else
                                Color.Black,

                            onClick = {
                                isLiked = !isLiked
                                if (isLiked) {
                                    likeCount++
                                    isDislike = false
                                } else likeCount = maxOf(0, likeCount - 1)
                                viewModel.onAction(SocialAction.LikeComment(comment.id))

                            },

                            text = if (likeCount > 0) {
                                formatCount(likeCount)
                            } else "",
                            showText = true,
                            modifier = Modifier
                                .size(16.dp)
                                .align(Alignment.CenterVertically)

                        )
                        //like

                        //dislike
                        CommentItem(
                            icon = if (isDislike)
                                FontAwesomeIcons.Solid.ThumbsDown
                            else
                                FontAwesomeIcons.Regular.ThumbsDown,

                            tint = if (isDislike)
                                Color.Gray
                            else
                                Color.Black,

                            text = "Không thích",
                            onClick = {
                                isDislike = !isDislike
                                if (isDislike && isLiked) {
                                    isLiked = false
                                    likeCount = maxOf(0, likeCount - 1)
                                    viewModel.onAction(SocialAction.LikeComment(comment.id))

                                }
                            },
                            showText = false,
                            modifier = Modifier
                                .graphicsLayer(
                                    scaleX = -1f
                                )
                                .padding(1.dp)
                                .size(16.dp)
                        )
                        //dislike

                    }
                }
            }
            //Comment content
        }

        //reply count
        if (replyCount > 0) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 60.dp)
                    .clickable(onClick = {}),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height(0.2.dp)
                        .background(
                            Color.Gray.copy(alpha = 0.5f)
                        )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Xem ${formatCount(replyCount)} câu trả lời",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray.copy(alpha = 0.9f),
                    modifier = Modifier
                        .clickable(onClick = {})
                        .fillMaxWidth(),
                    textAlign = TextAlign.Start,
                )
            }
        }
        //reply count
    }
}