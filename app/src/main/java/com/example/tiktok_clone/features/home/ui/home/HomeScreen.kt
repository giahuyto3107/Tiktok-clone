package com.example.tiktok_clone.features.home.ui.home

import androidx.annotation.DrawableRes
import com.example.tiktok_clone.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import com.example.tiktok_clone.core.navigation.AppNavigation
import com.example.tiktok_clone.core.utils.AppColors
import com.example.tiktok_clone.core.utils.AppConstants
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Envelope
import compose.icons.fontawesomeicons.solid.Home
import compose.icons.fontawesomeicons.solid.Search
import compose.icons.fontawesomeicons.solid.ShoppingBag
import compose.icons.fontawesomeicons.solid.User

@Composable
fun HomeScreen(
    onNavigationToCameraAccessScreen: () -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    val images = remember {
        listOf(
            R.drawable.apartment,
            R.drawable.cat,
            R.drawable.cherry_flower,
            R.drawable.city_post_office,
            R.drawable.independence_palace,
            R.drawable.river,
            R.drawable.road,
            R.drawable.street,
            R.drawable.uni,
            R.drawable.video,
        )
    }

    val pagerState = rememberPagerState(pageCount = { images.size })

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            VerticalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                Column {
                    Box(modifier = Modifier.fillMaxHeight(0.95f)) {
                        VideoSection(imageRes = images[page])

                        MiddleSection(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(
                                    bottom = AppConstants.SPACING_M.dp,
                                    end = AppConstants.SPACING_M.dp
                                )
                        )

                    }

                    VideoDescriptionSection(
                        userName = "User ${page+1}",
                        modifier = Modifier
                            .padding(
                                start = AppConstants.SPACING_M.dp,
                            )
                    )
                }
            }

            TopHeading(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        end = AppConstants.SPACING_M.dp,
                        top = AppConstants.FONT_TITLE_M.dp
                    )
                    .fillMaxWidth()
                    .safeDrawingPadding(),
            )
        }

        BottomNavigationBar(
            selectedIndex = selectedIndex,
            onTapSelected = { index -> selectedIndex = index },
            onCameraClick = onNavigationToCameraAccessScreen
        )
    }
}

@Composable
private fun TopHeading(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        Icon(
            imageVector = FontAwesomeIcons.Solid.Search,
            contentDescription = "Search",
            tint = AppColors.TEXT_ON_DARK,
            modifier = Modifier
                .size(size = AppConstants.FONT_TITLE_M.dp)
                .width(width = AppConstants.BORDER_THIN.dp)
        )
    }
}

@Composable
private fun VideoSection(
    @DrawableRes imageRes: Int,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = "Video Thumbnail",
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun BottomNavigationBar(
    selectedIndex: Int,
    onTapSelected: (Int) -> Unit,
    onCameraClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = AppConstants.SPACING_M.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        BottomNavigationItem(
            name = "Home",
            icon = FontAwesomeIcons.Solid.Home,
            isSelected = selectedIndex == 0,
            onTap = { onTapSelected(0) }
        )

        BottomNavigationItem(
            name = "Shop",
            icon = FontAwesomeIcons.Solid.ShoppingBag,
            isSelected = selectedIndex == 1,
            onTap = { onTapSelected(1) }
        )

        Image(
            painter = painterResource(id = R.drawable.camera_button_3x),
            contentDescription = "Camera button",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(bottom = AppConstants.SPACING_S.dp)
                .clickable { onCameraClick() }
        )

        BottomNavigationItem(
            name = "Inbox",
            icon = FontAwesomeIcons.Solid.Envelope,
            isSelected = selectedIndex == 3,
            onTap = { onTapSelected(3) }
        )

        BottomNavigationItem(
            name = "Profile",
            icon = FontAwesomeIcons.Solid.User,
            isSelected = selectedIndex == 4,
            onTap = { onTapSelected(4) }
        )
    }
}

@Composable
private fun BottomNavigationItem(
    name: String,
    icon: ImageVector,
    onTap: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.clickable { onTap() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = name,
            tint = if (isSelected) AppColors.TEXT_ON_DARK else AppColors.TEXT_SECONDARY,
            modifier = Modifier.size(size = AppConstants.FONT_TITLE_M.dp)
        )

        Text(
            name,
            color = if (isSelected) AppColors.TEXT_ON_DARK else AppColors.TEXT_SECONDARY,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Preview
@Composable
private fun PreviewHomeScreen() {
    AppNavigation()
}