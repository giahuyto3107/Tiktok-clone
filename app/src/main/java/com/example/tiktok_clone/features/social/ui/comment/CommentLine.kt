package com.example.tiktok_clone.features.social.ui.comment

import android.R
import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.dimensionResource
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
import com.example.tiktok_clone.features.social.ui.components.toDateString
import com.example.tiktok_clone.R.dimen
import com.example.tiktok_clone.features.social.viewModel.SocialAction
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import com.example.tiktok_clone.ui.theme.RedHeart
import com.example.tiktok_clone.ui.theme.TextPrimaryGray
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.Heart
import compose.icons.fontawesomeicons.regular.ThumbsDown
import compose.icons.fontawesomeicons.solid.Heart
import compose.icons.fontawesomeicons.solid.ThumbsDown
import com.example.tiktok_clone.ui.theme.TextSecondary


// hàm load 1 dòng comment
@Composable
fun CommentLine(
    viewModel: SocialViewModel = viewModel(),
    comment: Comment,
    modifier: Modifier = Modifier
) {
    var isLiked by remember { mutableStateOf(comment.isLiked) }
    var likeCount by remember { mutableStateOf(comment.likeCount) }
    var isReply by remember { mutableStateOf(false) }
    var isDislike by remember { mutableStateOf(false) }
    var isShowReply by remember { mutableStateOf(false) }
    var replyCount by remember { mutableStateOf(comment.replyCount) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .padding(horizontal = 8.dp)
            .then(modifier),
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
                    avatarUrl = comment.author.avatarUrl,
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
            ) {
                // user name
                Text(
                    text = comment.author.userName,
                    fontWeight = FontWeight.Medium,
                    fontSize = dimensionResource(dimen.font_l).value.sp,
                    color = TextPrimaryGray,

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
                    text = comment.content,
                    fontSize = 18.sp,
                )
                //Comment

                Spacer(Modifier.size(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    //Comment create at (time)
                    Text(
                        text = comment.createdAt.toDateString(),
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp,
                        color = TextPrimaryGray
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
                        color = TextPrimaryGray
                    )
                    //reply acction
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ) {

                        //like
                        CommentItem(
                            onClick = {
                                viewModel.onAction(SocialAction.LikeComment(comment.id))
                                isLiked = !isLiked
                                if (isLiked) {
                                    likeCount++
                                    isDislike = false
                                } else likeCount = maxOf(0, likeCount - 1)

                            },
                            icon = if (isLiked)
                                FontAwesomeIcons.Solid.Heart
                            else
                                FontAwesomeIcons.Regular.Heart,
                            tint = if (isLiked)
                                RedHeart
                            else
                                TextPrimaryGray,


                            text = if (likeCount > 0) {
                                formatCount(likeCount)
                            } else "",
                            showText = true,
                            modifier = Modifier
                                .size(18.dp)
                                .align(Alignment.CenterVertically)

                        )
                        //like

                        //dislike
                        CommentItem(
                            icon = if (isDislike)
                                FontAwesomeIcons.Solid.ThumbsDown
                            else
                                FontAwesomeIcons.Regular.ThumbsDown,

                            tint = TextPrimaryGray,

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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp),
            ) {
                if (isShowReply) {
                    val replys = viewModel.getReply(comment.id)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        for (reply in replys) {
                            CommentLine(
                                comment = reply,
                                modifier = Modifier.scale(0.9f)
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(start = 40.dp)
                        .clickable(onClick = {}),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .height(0.2.dp)
                            .background(TextPrimaryGray.copy(alpha = 0.5f))
                    )
                    Text(
                        text = if (isReply)
                            "Ẩn"
                        else
                            "Xem ${formatCount(replyCount)} câu trả lời",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimaryGray,
                        modifier = Modifier
                            .clickable(onClick = {
                                isReply = !isReply
                                isShowReply = !isShowReply
                            })
                            .fillMaxWidth(),
                        textAlign = TextAlign.Start,
                    )
                }
            }
            //reply count
        }
    }
}
