package com.example.tiktok_clone.features.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.core.utils.AppColors
import com.example.tiktok_clone.core.utils.AppConstants
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
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        MainInteractiveItem(
            icon = FontAwesomeIcons.Solid.Heart,
            name = "Love",
            numberOfInteraction = 2293
        )
        Spacer(modifier = Modifier.size(AppConstants.FONT_TITLE_M.dp))

        MainInteractiveItem(
            icon = FontAwesomeIcons.Solid.CommentDots,
            name = "Comment",
            numberOfInteraction = 1584
        )
        Spacer(modifier = Modifier.size(AppConstants.FONT_TITLE_M.dp))

        MainInteractiveItem(
            icon = FontAwesomeIcons.Solid.Bookmark,
            name = "Save",
            numberOfInteraction = 112
        )
        Spacer(modifier = Modifier.size(AppConstants.FONT_TITLE_M.dp))

        MainInteractiveItem(
            icon = FontAwesomeIcons.Solid.Share,
            name = "Share",
            numberOfInteraction = 4302
        )
        Spacer(modifier = Modifier.size(AppConstants.FONT_TITLE_M.dp))
    }
}

@Composable
fun MainInteractiveItem(
    icon: ImageVector,
    numberOfInteraction: Int,
    name: String,
    modifier: Modifier = Modifier
) {
    Column(
//        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = name,
            tint = AppColors.TEXT_ON_DARK,
            modifier = Modifier.size(size = AppConstants.FONT_TITLE_M.dp)
        )

        Text(
            text = numberOfInteraction.toString(),
            color = AppColors.TEXT_ON_DARK,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}
