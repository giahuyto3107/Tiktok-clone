package com.example.tiktok_clone.features.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import com.example.tiktok_clone.R
import com.example.tiktok_clone.core.navigation.AppNavigation
import com.example.tiktok_clone.features.home.ui.components.MiddleSection
import com.example.tiktok_clone.features.home.ui.components.VideoDescriptionSection
import com.example.tiktok_clone.features.home.ui.components.VideoPlayer
import com.example.tiktok_clone.features.home.viewmodel.HomeViewModel
import com.example.tiktok_clone.features.post.data.model.PostType
import com.example.tiktok_clone.features.profile.viewmodel.ProfileViewModel
import com.example.tiktok_clone.features.notification.data.model.SocialNotificationAction
import com.example.tiktok_clone.features.notification.viewModel.NotificationViewModel
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import com.example.tiktok_clone.features.social.ui.SocialUiState

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = koinViewModel(),
    profileViewModel: ProfileViewModel = koinViewModel(),
    socialViewModel: SocialViewModel = koinViewModel(),
    notificationViewModel: NotificationViewModel = koinViewModel(),
    onSearchTap: () -> Unit = {}
) {

    val posts by homeViewModel.posts.collectAsState()
    val users by homeViewModel.users.collectAsState()
    val isLoading by homeViewModel.isLoading.collectAsState()
    val error by homeViewModel.error.collectAsState()
    val context = LocalContext.current

    val currentUserId: String = profileViewModel.getProfileData()?.id.toString()
    val socialUiState by socialViewModel.uiState.collectAsState()
    val socialData = (socialUiState as? SocialUiState.Success)?.data
    val currentUser = socialData?.currentUser
    val postStates = socialData?.postStates ?: emptyMap()
    val following = socialData?.following ?: emptySet()

    LaunchedEffect(currentUserId) {
        if (currentUserId.isBlank() || currentUserId == "null") return@LaunchedEffect
        socialViewModel.getCurrentUser(currentUserId)
        socialViewModel.getFriends(currentUserId)
        socialViewModel.loadFollowers(currentUserId)
        socialViewModel.loadFollowing(currentUserId)
    }
    LaunchedEffect(Unit) {
        notificationViewModel.onAction(SocialNotificationAction.LoadNotifications)
    }

    // Single shared ExoPlayer instance
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_ONE    // Loop each video
            volume = 1f
        }
    }


    // Release player on dispose
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
            socialViewModel.disconnectPostRealtime()     // Khi post focus thay đổi
        }
    }
    val pagerState = rememberPagerState(pageCount = { posts.size })

    val currentPost = posts.getOrNull(pagerState.currentPage)
    val currentPostId = currentPost?.id?.toString()
    val nextPostId = posts.getOrNull(pagerState.currentPage + 1)?.id?.toString()

    // Ưu tiên load dữ liệu cho post hiện tại trước.
    // Chỉ rerun khi `currentPostId` đổi để tránh burst request khi `posts` update.
    LaunchedEffect(currentPostId) {
        if (currentPostId.isNullOrBlank()) return@LaunchedEffect

        socialViewModel.loadPostState(currentPostId)
        socialViewModel.connectPostRealtime(currentPostId)

        // Prefetch next comment sau 300ms để giảm cạnh tranh network/render.
        if (!nextPostId.isNullOrBlank()) {
            delay(300)
            socialViewModel.loadComments(nextPostId)
        }
    }
    // Infinite scroll: load more when near end
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { currentPage ->
            if (posts.isNotEmpty() && currentPage >= posts.size - 2) {
                homeViewModel.loadMore()
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (isLoading && posts.isEmpty()) {
            // Loading state — show spinner instead of black screen
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        } else if (posts.isEmpty()) {
            // Empty state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = error ?: "No posts yet",
                    color = Color.White
                )
            }
        } else {
            VerticalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                beyondViewportPageCount = 0 // Only compose the current page
            ) { page ->
                Column {
                    Box(modifier = Modifier.fillMaxHeight()) {
                        val currentPost = posts.getOrNull(page)
                        // Show image or video based on post type
                        if (currentPost?.type == PostType.IMAGE) {
                            AsyncImage(
                                model = currentPost.mediaUrl,
                                contentDescription = "Image Post",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            VideoPlayer(
                                exoPlayer = exoPlayer,
                                mediaUrl = currentPost?.mediaUrl ?: "",
                                thumbnailUrl = currentPost?.thumbnailUrl,
                                isCurrentPage = pagerState.currentPage == page
                            )
                        }

                        posts.getOrNull(page)?.let { currentPost ->
                            val author = users[currentPost.userId]
                            // Social actions sidebar (like, comment, share, save)
                            author?.let { user ->
                                MiddleSection(
                                    author = user,
                                    currentPost = currentPost,
                                    currentUser = currentUser,
                                    following = following,
                                    postState = postStates[currentPost.id.toString()],
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(
                                            bottom = dimensionResource(R.dimen.spacing_m),
                                            end = dimensionResource(R.dimen.spacing_m)
                                        )
                                )
                            }

                            // Caption and username overlay
                            VideoDescriptionSection(
                                userName = author?.userName ?: currentPost.userId,
                                caption = currentPost.caption,
                                modifier = Modifier
                                    .padding(
                                        start = dimensionResource(R.dimen.spacing_m),
                                        end = dimensionResource(R.dimen.spacing_xxxxl)
                                    )
                                    .align(Alignment.BottomStart)
                            )
                        }
                    }
                }
            }
        } // else (posts not empty)

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

@Preview(showBackground = true)
@Composable
private fun PreviewHomeScreen() {
    AppNavigation()
}