package com.example.tiktok_clone.features.home.ui.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tiktok_clone.core.navigation.AppNavigation
import com.example.tiktok_clone.R
import com.example.tiktok_clone.features.social.model.Post
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel

@Composable
fun HomeScreen(
    onSearchTap: () -> Unit = {}
) {
//    val images = remember {
//        listOf(
//            R.drawable.apartment,
//            R.drawable.cat,
//            R.drawable.cherry_flower,
//            R.drawable.city_post_office,
//            R.drawable.independence_palace,
//            R.drawable.river,
//            R.drawable.road,
//            R.drawable.street,
//            R.drawable.uni,
//            R.drawable.video,
//        )
//    }
    val viewModel: SocialViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadPosts()
    }
    //lấy dữ liệu post ừ fake data
    val posts = uiState.posts
    val pagerState = rememberPagerState(pageCount = { posts.size})

    Box(modifier = Modifier.fillMaxSize()) {
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Column {
                Box(modifier = Modifier
                    .fillMaxHeight()) {
                    VideoSection(thumbnailUrl = posts[page].thumbnailUrl)

                    MiddleSection(
                        currentUserId = "u1", // lấy từ user login, chưa có user login nên gán tạm
                        viewModel = viewModel,
                        currentPost = posts[page],
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(
                                bottom = dimensionResource(R.dimen.spacing_m),
                                end = dimensionResource(R.dimen.spacing_m)
                            )
                    )
                    VideoDescriptionSection(
                        userName = posts[page].author.userName,
                        description = posts[page].description,
                        modifier = Modifier
                            .padding(start = dimensionResource(R.dimen.spacing_m),)
                            .align(Alignment.BottomStart)
                    )
                }
            }
        }

        TopHeading(
            onSearchTap = onSearchTap,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    end = dimensionResource(R.dimen.spacing_m),
                    top = dimensionResource(R.dimen.font_title_m)
                )
                .fillMaxWidth()
                .safeDrawingPadding(),
        )
    }
}

@Composable
private fun TopHeading(
    modifier: Modifier = Modifier,
    onSearchTap: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = colorResource(R.color.text_on_dark),
            modifier = Modifier
                .size(size = dimensionResource(R.dimen.font_title_m))
                .width(width = dimensionResource(R.dimen.border_thin))
                .clickable(
                    onClick = { onSearchTap() }
                )
        )
    }
}
@Composable
fun VideoSection(
    thumbnailUrl: String?,
    modifier: Modifier = Modifier
) {
    // cập nhật lấy ảnh từ url
    AsyncImage(
        model = thumbnailUrl,
        contentDescription = "Video Thumbnail",
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Preview (showBackground = true)
@Composable
private fun PreviewHomeScreen() {
    AppNavigation()
}