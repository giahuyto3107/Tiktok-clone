package com.example.tiktok_clone.features.social.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tiktok_clone.R
import com.example.tiktok_clone.features.social.ui.components.CommentInput
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.Heart
import compose.icons.fontawesomeicons.regular.ThumbsDown
import compose.icons.fontawesomeicons.solid.Heart
import compose.icons.fontawesomeicons.solid.SortAmountDownAlt
import compose.icons.fontawesomeicons.solid.ThumbsDown
import compose.icons.fontawesomeicons.solid.Times

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentSheetContent(
    viewModel: SocialViewModel = viewModel(),
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val comments by viewModel.comments.collectAsState()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )


    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        dragHandle = { BottomSheetDefaults.DragHandle() },
        modifier = Modifier

    ) {
        Column(modifier = Modifier
            .fillMaxHeight(0.6f)
            .fillMaxWidth()
            .navigationBarsPadding()
        ) {
            CommentHeader(
                commentCount = comments.size,
                Search = "Search",
                onClose = onDismiss,
                modifier = Modifier
            )
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(comments.size) { index ->
                    CommentLine(
                        AvatarUrl = comments[index].AvatarUrl,
                        userName = comments[index].userName,
                        time = comments[index].commentTime,
                        comment = comments[index].comment,
                        likeCount = comments[index].likeCount,
                        replyCount = comments[index].replyCount,
                        modifier = Modifier
                    )
                }
            }
            CommentBottomBar(
                viewModel = viewModel,
                modifier = Modifier.fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.ime)
            )
        }
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
                placeholderVerticalAlign = PlaceholderVerticalAlign.AboveBaseline //căn đáy của vật thể nằm khớp trên đường chân chữ (baseline) để không bị lún xuống phần đuôi của các ký tự có nét móc dưới.
            )
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                tint = Color.Blue.copy(alpha = 0.8f),
                contentDescription = "search"
            )
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
        //horizontalAlignment = Alignment.CenterHorizontally
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
                        style = SpanStyle(color = Color.Blue.copy(alpha = 1.5f))
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
                text = "${formatCount(commentCount)} bình luận",
                modifier = Modifier.padding(4.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
        }
    }
}


@Composable
fun CommentItem(
    icon: ImageVector,
    onClick: () -> Unit,
    text: String,
    tint: Color = Color.Black,
    showText: Boolean,
    modifier: Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = modifier.clickable(onClick = onClick),
        )
        if (showText) {
            Text(
                text = text,
                modifier = Modifier
                    .padding(start = 4.dp, end = 8.dp)
                    .width(30.dp),
                fontSize = 12.sp,
                color = tint,
                textAlign = TextAlign.Start,
            )
        }
    }
}

@Composable
fun CommentBottomBar(
    viewModel: SocialViewModel = viewModel(),
    modifier: Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        tonalElevation = 4.dp,
        shadowElevation = 8.dp

    ) {
        CommentInput(
            viewModel = viewModel,
            modifier = Modifier
        )
    }
}

@Composable
fun CommentLine(
    AvatarUrl: String,
    userName: String,
    time: String,
    comment: String,
    likeCount: Int = 0,
    replyCount: Int = 0,
    modifier: Modifier
) {
    var isLiked by remember { mutableStateOf(false) }
    var isReply by remember { mutableStateOf(false) }
    var isDislike by remember { mutableStateOf(false) }
    var isShowReply by remember { mutableStateOf(false) }
    var likeCount by remember { mutableStateOf(likeCount) }
    var replyCount by remember { mutableStateOf(replyCount) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Row(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .border(1.dp, Color.Gray, CircleShape)
                    .size(45.dp)
                    .clip(CircleShape)
                //.align(Alignment.Top)
            ) {
                Avatar(
                    avatarUrl = AvatarUrl,
                    modifier = Modifier
                        .matchParentSize()
                )
            }
            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .align(Alignment.Top)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = userName,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color.Gray.copy(alpha = 0.9f),
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false // loại bỏ khoảng trống thừa trên đầu text
                        )
                    ),
                    modifier = Modifier.offset(y = (-3).dp) // Tinh chỉnh font lên trên -3, số xấu vl
                )
                Text(
                    text = comment,
//                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
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
                    Spacer(modifier = Modifier.width(10.dp))
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
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        CommentItem(
                            icon = if (isLiked) FontAwesomeIcons.Solid.Heart else FontAwesomeIcons.Regular.Heart,
                            tint = if (isLiked) Color.Red else Color.Black,
                            onClick = {
                                isLiked = !isLiked
                                if (isLiked) {
                                    likeCount++
                                    isDislike = false
                                } else likeCount = maxOf(0, likeCount - 1)
                            },

                            text = if (likeCount > 0) {
                                formatCount(likeCount)
                            } else "",
                            showText = true,
                            modifier = Modifier
                                .size(16.dp)
                                .align(Alignment.CenterVertically)

                        )
                        CommentItem(
                            icon = if (isDislike) FontAwesomeIcons.Solid.ThumbsDown else FontAwesomeIcons.Regular.ThumbsDown,
                            tint = if (isDislike) Color.Gray else Color.Black,
                            text = "Không thích",
                            onClick = {
                                isDislike = !isDislike
                                if (isDislike && isLiked) {
                                    isLiked = false
                                    likeCount = maxOf(0, likeCount - 1)
                                }
                            },
                            showText = false,
                            modifier = Modifier
                                .padding(1.dp)
                                .size(16.dp)
                        )
                    }
                }
            }
        }
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
                        .background(Color.Gray.copy(alpha = 0.5f))
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Xem ${formatCount(replyCount)} câu trả lời",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray.copy(alpha = 0.9f),
                    modifier = Modifier
                        .clickable(onClick = {})
                        .fillMaxWidth(),
                    textAlign = TextAlign.Start,
                )
            }
        }
    }
}

@Composable
fun Avatar(
    avatarUrl: String?,
    modifier: Modifier
) {
    if (!avatarUrl.isNullOrEmpty()) {
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