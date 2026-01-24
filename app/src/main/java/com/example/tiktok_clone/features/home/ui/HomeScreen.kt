package com.example.tiktok_clone.features.home.ui

import com.example.tiktok_clone.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.core.utils.AppColors
import com.example.tiktok_clone.core.utils.AppConstants
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Bookmark
import compose.icons.fontawesomeicons.solid.CommentDots
import compose.icons.fontawesomeicons.solid.Envelope
import compose.icons.fontawesomeicons.solid.Heart
import compose.icons.fontawesomeicons.solid.Home
import compose.icons.fontawesomeicons.solid.Search
import compose.icons.fontawesomeicons.solid.Share
import compose.icons.fontawesomeicons.solid.ShoppingBag
import compose.icons.fontawesomeicons.solid.User

@Composable
fun HomeScreen() {
    var selectedIndex by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        VideoSection()
        Column(
            modifier = Modifier
                .padding(
                    horizontal = AppConstants.SPACING_M.dp,
                    vertical = AppConstants.SPACING_M.dp,
                )
                .fillMaxSize()
        ) {
            TopHeading()
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                MiddleSection()
            }
            BottomNavigationBar()
        }
    }
}

@Composable
fun TopHeading() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.End
    ) {
        Icon(
            imageVector = FontAwesomeIcons.Solid.Search,
            contentDescription = "Search",
            tint = AppColors.TEXT_ON_DARK,
            modifier = Modifier.size(size = AppConstants.FONT_TITLE_M.dp)
        )
    }
}

@Composable
fun VideoSection() {
    Image(
        painter = painterResource(id = R.drawable.video),
        contentDescription = "Main background",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun MiddleSection() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        Icon(
            imageVector = FontAwesomeIcons.Solid.Heart,
            contentDescription = "Search",
            tint = AppColors.TEXT_ON_DARK,
            modifier = Modifier.size(size = AppConstants.FONT_TITLE_M.dp)
        )
        Spacer(modifier = Modifier.size(AppConstants.FONT_TITLE_M.dp))
        Icon(
            imageVector = FontAwesomeIcons.Solid.CommentDots,
            contentDescription = "Search",
            tint = AppColors.TEXT_ON_DARK,
            modifier = Modifier.size(size = AppConstants.FONT_TITLE_M.dp)
        )
        Spacer(modifier = Modifier.size(AppConstants.FONT_TITLE_M.dp))
        Icon(
            imageVector = FontAwesomeIcons.Solid.Bookmark,
            contentDescription = "Search",
            tint = AppColors.TEXT_ON_DARK,
            modifier = Modifier.size(size = AppConstants.FONT_TITLE_M.dp)
        )
        Spacer(modifier = Modifier.size(AppConstants.FONT_TITLE_M.dp))
        Icon(
            imageVector = FontAwesomeIcons.Solid.Share,
            contentDescription = "Search",
            tint = AppColors.TEXT_ON_DARK,
            modifier = Modifier.size(size = AppConstants.FONT_TITLE_M.dp)
        )
        Spacer(modifier = Modifier.size(AppConstants.FONT_TITLE_M.dp))
    }
}



@Composable
fun BottomNavigationBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        BottomNavigationItem(
            name = "Home",
            icon = FontAwesomeIcons.Solid.Home
        )

        BottomNavigationItem(
            name = "Shop",
            icon = FontAwesomeIcons.Solid.ShoppingBag
        )

        Image(
            painter = painterResource(id = R.drawable.camera_button),
            contentDescription = "Main background",
//            modifier = Modifier.size(size = AppConstants.FONT_TITLE_S.dp),
            contentScale = ContentScale.Crop
        )

        BottomNavigationItem(
            name = "Inbox",
            icon = FontAwesomeIcons.Solid.Envelope
        )

        BottomNavigationItem(
            name = "Profile",
            icon = FontAwesomeIcons.Solid.User
        )
    }
}

@Composable
fun BottomNavigationItem(
    name: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = name,
            tint = AppColors.TEXT_ON_DARK,
            modifier = Modifier.size(size = AppConstants.FONT_TITLE_M.dp)
        )

        Text(
            "Home",
            color = AppColors.TEXT_ON_DARK,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    HomeScreen()
}