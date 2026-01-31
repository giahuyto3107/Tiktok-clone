package com.example.tiktok_clone.features.home.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun MiddleSection(
    modifier: Modifier = Modifier
) {
    var isLiked by remember { mutableStateOf(false) }
    var likeCount by remember { mutableIntStateOf(2293) }
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        MainInteractiveItem(
            icon = FontAwesomeIcons.Solid.Heart,
            name = "Love",
            numberOfInteraction = likeCount,
            isLiked = isLiked,
            onHeartClick = {
                isLiked = !isLiked
                likeCount = if (isLiked) likeCount + 1 else likeCount - 1
            }
        )
        Spacer(modifier = Modifier.size(dimensionResource(R.dimen.font_title_m)))

        MainInteractiveItem(
            icon = FontAwesomeIcons.Solid.CommentDots,
            name = "Comment",
            numberOfInteraction = 1584
        )
        Spacer(modifier = Modifier.size(dimensionResource(R.dimen.font_title_m)))

        MainInteractiveItem(
            icon = FontAwesomeIcons.Solid.Bookmark,
            name = "Save",
            numberOfInteraction = 112
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
    isLiked: Boolean = false,
    onHeartClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .clickable { onHeartClick?.invoke() },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = name,
            tint = if (name == "Love" && isLiked) Color.Red else colorResource(R.color.text_on_dark),
            modifier = Modifier.size(size = dimensionResource(R.dimen.font_title_m))
        )

        Text(
            text = numberOfInteraction.toString(),
            color = colorResource(R.color.text_on_dark),
            style = MaterialTheme.typography.labelSmall,
        )
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