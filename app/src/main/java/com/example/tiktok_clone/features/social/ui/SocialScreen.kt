package com.example.tiktok_clone.features.social.ui


import android.app.Activity
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import androidx.compose.ui.res.colorResource
import com.example.tiktok_clone.R
import compose.icons.AllIcons
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.Bell
import compose.icons.fontawesomeicons.regular.Bookmark
import compose.icons.fontawesomeicons.regular.Heart
import compose.icons.fontawesomeicons.solid.*
import com.example.tiktok_clone.R.drawable
import com.example.tiktok_clone.features.social.model.Post
import com.example.tiktok_clone.features.social.model.SocialViewModel
import com.example.tiktok_clone.features.social.model.FakePostData.posts
import androidx.compose.ui.Alignment


sealed class ActivityAction {
    data class OpenAddFriend(val id: String) : ActivityAction()
    data class OpenProfileViews(val id: String) : ActivityAction()
    data class OpenProfileShare(val id: String) : ActivityAction()
    data class OpenSetting(val id: String) : ActivityAction()
    data class OpenProfileAssets(val id: String) : ActivityAction()
    data class OpenFollowings(val id: String) : ActivityAction()
    data class OpenFollower(val id: String) : ActivityAction()
    data class OpenBio(val id: String) : ActivityAction()
    data class OpenReels(val id: String) : ActivityAction()
    data class OpenPrivate(val id: String) : ActivityAction()
    data class OpenShare(val id: String) : ActivityAction()
    data class OpenSave(val id: String) : ActivityAction()
    data class OpenLiked(val id: String) : ActivityAction()

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SocialScreen(
    user: Boolean,
    viewModel: SocialViewModel = viewModel(),
    modifier: Modifier
) {
    val postList = viewModel.posts.collectAsState().value
    val headerHight = 30.dp

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        HeaderBar(
            user = user,
            onBackClick = { /*TODO*/ },
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(top=10.dp)
                .height(headerHight)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            item {
                UserProfile(
                    userName = "chấm hỏi to đùng",
                    nickname = "anamdudu",
                    bio = "chấm hỏi to đùng",
                    user = user,
                    avatarUrl = null,
                    modifier = Modifier
                        .background(Color.White)
                )
            }
            stickyHeader {
                ActivityList(
                    onClick = { /*TODO*/ }
                )
            }
            item {
                PostGrid(
                    postList = postList,
                    modifier = Modifier
                        .background(Color.White)
                )
            }
        }

    }

}


@Composable
fun HeaderBar(
    user: Boolean,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    //left side
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (user) {
            HeaderBarItem(
                name = "add friend",
                icon = FontAwesomeIcons.Solid.ArrowLeft,
                iconColor = Color.Black,
                textColor = Color.Black,
                showText = false,
                onClick = onBackClick,
                modifier = Modifier.size(24.dp)
            )
        } else {
            HeaderBarItem(
                name = "back",
                icon = FontAwesomeIcons.Solid.ArrowLeft,
                iconColor = Color.Black,
                textColor = Color.Black,
                showText = false,
                onClick = onBackClick,
                modifier = Modifier.size(24.dp)
            )
        }

        //right side
        Row(
            modifier = Modifier.fillMaxWidth(if (user) 0.3f else 0.15f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            if (user) {
                HeaderBarItem(
                    name = "profile views",
                    icon = FontAwesomeIcons.Solid.Eye,
                    iconColor = Color.Black,
                    textColor = colorResource(R.color.text_on_light),
                    onClick = { /*TODO*/ },
                    showText = false,
                    modifier = Modifier.size(24.dp)
                )
                HeaderBarItem(
                    name = "profile share",
                    icon = FontAwesomeIcons.Solid.Share,
                    onClick = { /*TODO*/ },
                    iconColor = Color.Black,
                    textColor = colorResource(R.color.text_on_light),
                    showText = false,
                    modifier = Modifier.size(24.dp)
                )
                HeaderBarItem(
                    name = "assets",
                    icon = FontAwesomeIcons.Solid.Bars,
                    showText = false,
                    iconColor = Color.Black,
                    textColor = colorResource(R.color.text_on_light),
                    onClick = { /*TODO*/ },
                    modifier = Modifier.size(24.dp)
                )
            } else {
                HeaderBarItem(
                    name = "profile notification",
                    icon = FontAwesomeIcons.Regular.Bell,
                    showText = false,
                    iconColor = Color.Black,
                    textColor = colorResource(R.color.text_on_light),
                    onClick = { /*TODO*/ },
                    modifier = Modifier.size(24.dp)
                )
                HeaderBarItem(
                    name = "profile share",
                    icon = FontAwesomeIcons.Solid.Share,
                    showText = false,
                    iconColor = Color.Black,
                    textColor = colorResource(R.color.text_on_light),
                    onClick = { /*TODO*/ },
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }

}

@Composable
fun HeaderBarItem(
    name: String,
    icon: ImageVector,
    iconColor: Color,
    textColor: Color,
    showText: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = modifier
        )
        if (showText) {
            Text(
                text = name,
                color = textColor,
            )
        }
    }
}

@Composable
fun UserProfile(
    nickname: String,
    userName: String,
    bio: String,
    user: Boolean,
    avatarUrl: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                Avatar(
                    avatarUrl,
                    modifier = Modifier
                        .size(100.dp)
                        .graphicsLayer(
                            scaleX = 0.9f,
                            scaleY = 0.9f,
                            shape = CircleShape,
                            clip = true
                        )
                        .border(
                            width = 0.5.dp,
                            color = Color.Gray,
                            shape = CircleShape
                        )
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = modifier.fillMaxWidth(0.1f)) { }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = modifier
                            .widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.65f)
                    ) {
                        Text(
                            text = nickname,
                            color = colorResource(R.color.text_on_light),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = modifier
                                .align(Alignment.Center)
                        )
                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = modifier
                            .padding(start = 4.dp)
                            .width(40.dp)
                            .background(
                                color = Color.LightGray.copy(alpha = 0.2f),
                                shape = CircleShape
                            )

                    ) {
                        Icon(
                            imageVector = FontAwesomeIcons.Solid.PencilAlt,
                            contentDescription = "edit",
                            modifier = modifier
                                .padding(6.dp)
                                .size(12.dp)
                        )
                    }
                }
                Text(
                    text = "@$userName",
                    color = Color.LightGray,
                )
                Row(
                    modifier = modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FollowInfo(
                        following = 100,
                        followers = 100,
                        likes = 100,
                        modifier = modifier
                            .padding(4.dp)
                    )

                }
                Bio(
                    bio = bio,
                    user = user,
                    modifier = modifier
                        .padding(4.dp)

                )
            }

        }
    }
}

@Composable
fun Avatar(
    avatarUrl: String?,
    modifier: Modifier = Modifier
) {
    if (avatarUrl != null) {
        AsyncImage(
            model = avatarUrl,
            contentDescription = "avatar",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    } else {
        Image(
            painter = painterResource(id = drawable.ic_default_avatar),
            contentDescription = "default avatar",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }

}

@Composable
fun FollowInfo(
    following: Int,
    followers: Int,
    likes: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(0.8f),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Title(
            title = "following",
            count = following,
            modifier = modifier.weight(1f)
        )
        Title(
            title = "followers",
            count = followers,
            modifier = modifier.weight(1f)
        )
        Title(
            title = "likes",
            count = likes,
            modifier = modifier.weight(1f)
        )
    }
}

@Composable
fun Title(
    title: String,
    count: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = count.toString(),
            color = colorResource(R.color.text_on_light),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            text = title,
            color = Color.LightGray,
        )
    }
}

@Composable
fun Bio(
    bio: String?,
    user: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(0.7f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if (!bio.isNullOrEmpty()) {
            Text(
                text = bio,
                color = colorResource(R.color.text_on_light),
                fontSize = 15.sp,
                maxLines = 2,
                textAlign = TextAlign.Center,
            )
        } else {
            if (user == true) {
                Row(
                    modifier = Modifier
                        .background(
                            color = Color.LightGray.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(100.dp)
                        )
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = FontAwesomeIcons.Solid.Plus,
                        contentDescription = "add bio",
                        modifier = modifier.size(15.dp)
                    )
                    Text(
                        text = "add bio",
                        color = colorResource(R.color.text_on_light),
                    )
                    Icon(
                        imageVector = FontAwesomeIcons.Solid.DotCircle,
                        contentDescription = "dot",
                        modifier = modifier
                            .size(2.dp),
                    )
                    Icon(
                        imageVector = FontAwesomeIcons.Regular.Heart,
                        contentDescription = "ex:",
                        modifier = modifier
                            .size(15.dp),
                        tint = Color.Red
                    )

                    Text(
                        text = "toi dep trai",
                        color = Color.LightGray,
                    )
                }
            }
        }
    }
}

@Composable
fun ActivityScreen(
    user: Boolean,
    modifier: Modifier = Modifier
) {
    SocialScreen(
        user = user,
        modifier = modifier
            .background(Color.White)
            .fillMaxSize()
            .safeDrawingPadding()
    )
}

@Composable
fun ActivityList(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 5.dp, bottom = 5.dp, start = 15.dp, end = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Row(
                modifier = Modifier.height(30.dp)

            ) {
                Column(
                ) {
                    HeaderBarItem(
                        icon = FontAwesomeIcons.Solid.Bars,
                        name = "back",
                        iconColor = Color.Black,
                        textColor = Color.Black,
                        showText = false,
                        onClick = onClick,
                        modifier = Modifier
                            .size(12.dp)
                            .rotate(45f)
                    )
                    HeaderBarItem(
                        icon = FontAwesomeIcons.Solid.Bars,
                        name = "back",
                        iconColor = Color.Black,
                        textColor = Color.Black,
                        showText = false,
                        onClick = onClick,
                        modifier = Modifier
                            .size(12.dp)
                            .rotate(45f)
                    )
                }
                HeaderBarItem(
                    icon = FontAwesomeIcons.Solid.SortDown,
                    name = "back",
                    iconColor = Color.Black,
                    textColor = Color.Black,
                    showText = false,
                    onClick = onClick,
                    modifier = Modifier
                        .align(Alignment.Top)
                        .size(16.dp)

                )
            }
            HeaderBarItem(
                icon = FontAwesomeIcons.Solid.Lock,
                name = "back",
                iconColor = Color.Black,
                textColor = Color.Black,
                showText = false,
                onClick = onClick,
                modifier = Modifier.size(24.dp)
            )
            HeaderBarItem(
                icon = FontAwesomeIcons.Solid.Retweet,
                name = "back",
                iconColor = Color.Black,
                textColor = Color.Black,
                showText = false,
                onClick = onClick,
                modifier = Modifier
                    .size(24.dp)
                    .rotate(45f)
            )
            HeaderBarItem(
                icon = FontAwesomeIcons.Regular.Bookmark,
                name = "back",
                iconColor = Color.Black,
                textColor = Color.Black,
                showText = false,
                onClick = onClick,
                modifier = Modifier.size(24.dp)
            )
            HeaderBarItem(
                icon = FontAwesomeIcons.Regular.Heart,
                name = "back",
                iconColor = Color.Black,
                textColor = Color.Black,
                showText = false,
                onClick = onClick,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun PostGrid(
    postList: List<Post>,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        postList.chunked(3).forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                rowItems.forEach { post ->
                    PostItem(
                        post = post,
                        modifier = Modifier.weight(1f)
                    )
                }
                repeat(3 - rowItems.size) {
                    Box(
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun PostItem(
    post: Post,
    modifier: Modifier = Modifier
) {
    val imageModel: Any =
        if (post.imageUrl.isEmpty())
            drawable.ic_default_avatar
        else
            post.imageUrl
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .background(Color.White)
    ) {
        AsyncImage(
            model = imageModel,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .aspectRatio(1f)
                .padding(2.dp)
        )
    }
}