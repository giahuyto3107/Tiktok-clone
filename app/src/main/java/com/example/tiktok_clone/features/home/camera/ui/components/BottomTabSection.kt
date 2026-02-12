package com.example.tiktok_clone.features.home.camera.ui.components

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.R
import kotlinx.coroutines.launch

@Composable
fun BottomTabSection(
    hasPermission: Boolean,
    latestImageUri: Uri?,
    onGalleryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(modifier = Modifier
            .padding(
                start = dimensionResource(R.dimen.spacing_m),
                top = dimensionResource(R.dimen.spacing_s)
            )
        ) {
            GalleryThumbnail(
                latestImageUri = latestImageUri,
                onClick = onGalleryClick,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }

        PostCategorySlider(
            hasPermission = hasPermission,
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = dimensionResource(R.dimen.spacing_s))
        )
    }
}

@Composable
private fun PostCategorySlider(
    hasPermission: Boolean,
    modifier: Modifier = Modifier
) {
    val options = listOf("POST", "CREATE", "LIVE")
    val pagerState = rememberPagerState(pageCount = { options.size }, initialPage = 0)

    val scope = rememberCoroutineScope()

    val itemWidth = 90.dp
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val horizontalPadding = (screenWidth - itemWidth) / 2

    HorizontalPager(
        state = pagerState,
        userScrollEnabled = hasPermission,
        pageSize = PageSize.Fixed(itemWidth),
        contentPadding = PaddingValues(horizontal = horizontalPadding),
        modifier = modifier
    ) { page ->
        PostCategorySliderItem(
            name = options[page],
            isSelected = pagerState.currentPage == page,
            onTap = {
                scope.launch {
                    pagerState.animateScrollToPage(page)
                }
            }
        )
    }
}

@Composable
fun PostCategorySliderItem(
    name: String,
    isSelected: Boolean,
    onTap: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onTap() }
    ) {
        Text(
            text = name,
            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.5f),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}