package com.example.tiktok_clone.features.social.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tiktok_clone.R
import com.example.tiktok_clone.core.utils.AppColors
import com.example.tiktok_clone.core.utils.AppConstants
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.Heart
import compose.icons.fontawesomeicons.regular.ThumbsDown
import compose.icons.fontawesomeicons.solid.Heart
import compose.icons.fontawesomeicons.solid.Search
import compose.icons.fontawesomeicons.solid.SortAmountDownAlt
import compose.icons.fontawesomeicons.solid.ThumbsDown
import compose.icons.fontawesomeicons.solid.Times

@Composable
fun CommentSheetContent(
    commentCount: Int = 1232130,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
            .padding(8.dp)

    ) {
        CommentHeader(
            commentCount = commentCount,
            Search = "Nam dẹp trai",
            modifier = Modifier.fillMaxWidth(),
            onClose = onClose
        )
        CommentLine(
            AvatarUrl = "https://yt3.ggp",
            userName = "Nam đẹp trai",
            time = "2 giờ",
            comment = "Nam đẹp trai",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun CommentHeader(
    commentCount: Int,
    Search: String,
    onClose: () -> Unit,
    modifier: Modifier
) {
    var isSort by remember { mutableStateOf(false) }

    val inLineContent = mapOf(
        "searchIcon" to InlineTextContent(
            Placeholder(
                width = 16.sp,
                height = 16.sp,
                placeholderVerticalAlign = PlaceholderVerticalAlign.AboveBaseline
            )
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                tint = Color.Blue,
                contentDescription = "search"
            )
        }
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Tìm kiếm: ")
                    withStyle(
                        style = SpanStyle(color = Color.Blue)
                    ) {
                        append(Search)
                    }
                    appendInlineContent("searchIcon", "[icon]")
                },
                inlineContent = inLineContent,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(4.dp)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.Bottom),

                ) {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    CommentItem(
                        icon = FontAwesomeIcons.Solid.SortAmountDownAlt,
                        onClick = {
                            isSort = !isSort
                        },
                        text = "Sắp xếp",
                        showText = false,
                        modifier = Modifier
                            .padding(4.dp)
                            .size(20.dp)
                    )
                    CommentItem(
                        icon = FontAwesomeIcons.Solid.Times,
                        onClick = onClose,
                        text = "Đóng",
                        showText = false,
                        modifier = Modifier
                            .padding(4.dp)
                            .size(24.dp)

                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = formatCommentCount(commentCount),
                modifier = Modifier.padding(4.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun formatCommentCount(
    commentCount: Int
): String {
    return if (commentCount < 1000) {
        commentCount.toString()
    } else {
        "${commentCount / 1000}k"
    }
}

@Composable
fun CommentItem(
    icon: ImageVector,
    onClick: () -> Unit,
    text: String,
    tint: Color = AppColors.TEXT_ON_LIGHT,
    showText: Boolean,
    modifier: Modifier
) {
    Row(
        modifier = Modifier.clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = modifier,
        )
        if (showText) {
            Box(
                modifier = Modifier.width(36.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    modifier = modifier,
                    color = tint
                )
            }
        }
    }
}

@Composable
fun CommentLine(
    AvatarUrl: String,
    userName: String,
    time: String,
    comment: String,
    modifier: Modifier
) {
    var isLiked by remember { mutableStateOf(false) }
    var isReply by remember { mutableStateOf(false) }
    var isDislike by remember { mutableStateOf(false) }
    var isShowReply by remember { mutableStateOf(false) }
    var likeCount by remember { mutableStateOf(10) }
    var replyCount by remember { mutableStateOf(20) }


    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Row(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .border(1.dp, Color.Gray, CircleShape)
                    .size(50.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterVertically)
            ) {
                Avatar(
                    avatarUrl = AvatarUrl,
                    modifier = Modifier.matchParentSize()

                )
            }
            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = userName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )
                Text(
                    text = comment,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = time,
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Trả lời",
                        modifier = Modifier
                            .clickable(onClick = {}),
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        CommentItem(
                            icon = if (isLiked) FontAwesomeIcons.Solid.Heart else FontAwesomeIcons.Regular.Heart,
                            tint = if (isLiked) Color.Red else Color.Black,
                            onClick = {
                                isLiked = !isLiked
                                if (isLiked) likeCount++ else likeCount = maxOf(0, likeCount - 1)
                            },

                            text = if (likeCount > 0) {
                                formatCount(likeCount)
                            } else "",
                            showText = true,
                            modifier = Modifier
                                .padding(4.dp)
                                .size(16.dp)
                        )
                        CommentItem(
                            icon = if (isDislike) FontAwesomeIcons.Solid.ThumbsDown else FontAwesomeIcons.Regular.ThumbsDown,
                            tint = if (isDislike) Color.Gray else Color.Black,
                            text = "Không thích",
                            onClick = {
                                isDislike = !isDislike
                                if (isDislike) likeCount = maxOf(0, likeCount - 1) else likeCount ++
                            },
                            showText = false,
                            modifier = Modifier
                                .padding(4.dp)
                                .size(16.dp)
                        )
                    }
                }
            }
        }
        Text(
            text = "Xem ${formatCount(replyCount)} câu trả lời",
            modifier = Modifier
                .clickable(onClick = {}),
        )
    }
}
// mai làm  tiếp

@Composable
fun Avatar(
    avatarUrl: String?,
    modifier: Modifier
) {
    if (avatarUrl != null) {
        AsyncImage(
            model = avatarUrl,
            contentDescription = "avatar",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.default_avatar),
            contentDescription = "default avatar",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }

}

fun formatCount(count: Int): String {
    return when {
        count >= 1_000_000 -> "%.1fM".format(count / 1_000_000.0)
        count >= 1_000 -> "%.1fk".format(count / 1_000.0)
        else -> count.toString()
    }
}