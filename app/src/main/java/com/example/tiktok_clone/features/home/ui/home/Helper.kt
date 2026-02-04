package com.example.tiktok_clone.features.home.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import com.example.tiktok_clone.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiktok_clone.features.social.ui.share.ShareSheetContent
import com.example.tiktok_clone.features.social.ui.comment.CommentSheetContent
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Bookmark
import compose.icons.fontawesomeicons.solid.CommentDots
import compose.icons.fontawesomeicons.solid.Heart
import compose.icons.fontawesomeicons.solid.Share


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiddleSection(
    viewModel: SocialViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val comments by viewModel.comments.collectAsState()
    var isLiked by remember { mutableStateOf(false) }
    var likeCount by remember { mutableIntStateOf(2293) }
    var saveCount by remember { mutableIntStateOf(123) }
    val commentCount = comments.size
    var isSaved by remember { mutableStateOf(false) }
    var isOpenCommentSheet by remember { mutableStateOf(false) }
    var isOpenShareSheet by remember { mutableStateOf(false) }


    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        MainInteractiveItem(
            icon = FontAwesomeIcons.Solid.Heart,
            tint = if (isLiked) Color.Red else Color.White,
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
                onDismiss = {
                    isOpenCommentSheet = false
                }
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        MainInteractiveItem(
            icon = FontAwesomeIcons.Solid.Bookmark,
            tint = if (isSaved) Color.Yellow else Color.White,
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
    numberOfInteraction: Int,
    name: String,
    modifier: Modifier = Modifier,
    tint: Color = Color.White,
    onClick: () -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = name,
            tint = tint,
            modifier = Modifier
                .size(size = dimensionResource(R.dimen.font_title_s))
                .clickable(onClick = onClick)
        )

        Text(
            text = formatCount(numberOfInteraction),
            color = Color.White,
            style = MaterialTheme.typography.labelSmall,
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

@Composable
fun VideoDescriptionSection(
    userName: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = userName,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
    }
}

@Composable
fun CustomDragHandle(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Spacer(
            modifier = Modifier
                .size(width = 100.dp, height = 5.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(50.dp)
                )
        )
    }
}
