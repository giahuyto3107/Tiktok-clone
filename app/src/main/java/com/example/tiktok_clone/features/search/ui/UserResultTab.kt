package com.example.tiktok_clone.features.search.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.tiktok_clone.features.profile.viewmodel.ProfileViewModel
import com.example.tiktok_clone.features.search.model.UserItem
import com.example.tiktok_clone.features.social.data.model.SocialAction
import com.example.tiktok_clone.features.social.ui.components.Avatar
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserResultTab(
    users: List<UserItem>,
    onAvatarClick: (String) -> Unit = {},
    socialViewModel: SocialViewModel = koinViewModel(),
    profileViewModel: ProfileViewModel = koinViewModel(),
) {
    val currentUserId: String = profileViewModel.getProfileData()?.id.toString()
    val socialGraph by socialViewModel.socialGraphUiState.collectAsState()
    val following = socialGraph.following

    LaunchedEffect(currentUserId) {
        if (currentUserId.isNotBlank() && currentUserId != "null") {
            socialViewModel.loadFollowing(currentUserId)
        }
    }

    if (users.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Không có người dùng", color = Color.Gray)
        }
        return
    }

    LazyColumn {
        items(users, key = { it.uid }) { user ->
            val targetUserId = user.uid
            val isCurrentUser = targetUserId == currentUserId
            val isFollowing = following.contains(targetUserId)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {}
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Avatar(
                    avatarUrl = user.avatar,
                    avatarSize = 52,
                    modifier = Modifier
                        .clickable{ onAvatarClick(targetUserId) }
                )
                Spacer(Modifier.width(12.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onAvatarClick(targetUserId) },
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    val handleText = user.handle.removePrefix("@")
                    Text(
                        user.displayName.ifBlank { user.handle },
                        fontSize = 15.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                    )
                    Text(
                        "@$handleText",
                        fontSize = 13.sp,
                        lineHeight = 16.sp,
                        color = Color.Gray,
                        maxLines = 1,
                    )
                    Text(
                        "${formatCompactCount(user.followerCount)} follower · ${
                            formatCompactCount(
                                user.totalLikes
                            )
                        } lượt thích",
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        color = Color.Gray,
                    )
                }

                if (isCurrentUser) {
                    Button(
                        onClick = {},
                        enabled = false,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEEEEEE),
                            contentColor = Color.Gray,
                        ),
                        modifier = Modifier.padding(start = 4.dp),
                    ) {
                        Text("Bạn", fontSize = 12.sp)
                    }
                } else if (isFollowing) {
                    Button(
                        onClick = { socialViewModel.onAction(SocialAction.Follow(targetUserId)) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEEEEEE),
                            contentColor = Color.Black,
                        ),
                        modifier = Modifier.padding(start = 4.dp),
                    ) {
                        Text("Unfollow", fontSize = 12.sp)
                    }
                } else {
                    Button(
                        onClick = { socialViewModel.onAction(SocialAction.Follow(targetUserId)) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF2E63),
                            contentColor = Color.White,
                        ),
                        modifier = Modifier.padding(start = 4.dp),
                    ) {
                        Text("Follow", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
