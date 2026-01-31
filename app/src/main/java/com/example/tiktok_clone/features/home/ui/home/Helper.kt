package com.example.tiktok_clone.features.home.ui.home

import android.util.Log
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import com.example.tiktok_clone.R
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
    var commentCount by remember { mutableIntStateOf(comments.size) }
    var isSaved by remember { mutableStateOf(false) }
    var isOpenCommentSheet by remember { mutableStateOf(false) }
    var isOpenShareSheet by remember { mutableStateOf(false) }

    var sheetState =
        rememberModalBottomSheetState(
            skipPartiallyExpanded = false,
            confirmValueChange = { true })

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        MainInteractiveItem(
            icon = FontAwesomeIcons.Solid.Heart,
            tint = if (isLiked) Color.Red else AppColors.TEXT_ON_DARK,
            name = "Love",
            numberOfInteraction = likeCount,
            onClick = {
                isLiked = !isLiked
                likeCount = if (isLiked) likeCount + 1 else likeCount - 1
            }
        )
        Spacer(modifier = Modifier.size(dimensionResource(R.dimen.font_title_m)))

        MainInteractiveItem(
            icon = FontAwesomeIcons.Solid.CommentDots,
            name = "Comment",
            numberOfInteraction = commentCount,
            onClick = {
                isOpenCommentSheet = true
            }
        )
        //open commentSheet
        if (isOpenCommentSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    isOpenCommentSheet = false
                },
                sheetState = sheetState,
                dragHandle = {
                    CustomDragHandle(
                        onDrag = {}
                    )
                },
                containerColor = Color.White,
                contentColor = Color.Black,
            ) {
                CommentSheetContent(
                    viewModel = viewModel,
                    onClose = {
                        isOpenCommentSheet = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.size(AppConstants.FONT_TITLE_M.dp))
        Spacer(modifier = Modifier.size(dimensionResource(R.dimen.font_title_m)))

        MainInteractiveItem(
            icon = FontAwesomeIcons.Solid.Bookmark,
            tint = if (isSaved) Color.Yellow else AppColors.TEXT_ON_DARK,
            name = "Save",
            numberOfInteraction = saveCount,
            onClick = {
                isSaved = !isSaved
                saveCount = if (isSaved) saveCount + 1 else saveCount - 1
            }
        )
        Spacer(modifier = Modifier.size(dimensionResource(R.dimen.font_title_m)))

        MainInteractiveItem(
            icon = FontAwesomeIcons.Solid.Share,
            name = "Share",
            numberOfInteraction = 4302
        )
        Spacer(modifier = Modifier.size(dimensionResource(R.dimen.font_title_m)))
    }
}

@Composable
fun MainInteractiveItem(
    icon: ImageVector,
    numberOfInteraction: Int,
    name: String,
    modifier: Modifier = Modifier,
    tint: Color = AppColors.TEXT_ON_DARK,
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
                .size(size = AppConstants.FONT_TITLE_M.dp)
                .clickable(onClick = onClick)
        )

        Text(
            text = formatCount(numberOfInteraction),
            color = AppColors.TEXT_ON_DARK,
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
            color = colorResource(R.color.text_on_dark)
        )
    }
}

@Composable
fun CustomDragHandle(
    onDrag: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(
            modifier = Modifier
                .size(width = 8.dp, height = 4.dp)
                .background(
                    color = Color.Gray.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(50.dp)
                )
        )
    }
}
