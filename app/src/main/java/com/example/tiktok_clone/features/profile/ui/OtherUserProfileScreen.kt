package com.example.tiktok_clone.features.profile.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.features.auth.ui.ProfileStat
import com.example.tiktok_clone.features.profile.viewmodel.OtherUserProfileViewModel
import com.example.tiktok_clone.features.profile.viewmodel.ProfileViewModel
import com.example.tiktok_clone.features.social.data.model.SocialAction
import com.example.tiktok_clone.features.social.ui.SocialUiState
import com.example.tiktok_clone.features.social.ui.components.Avatar
import com.example.tiktok_clone.features.social.ui.components.formatCount
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun OtherUserProfileScreen(
    userId: String,
    onBack: () -> Unit,
    otherUserProfileViewModel: OtherUserProfileViewModel = koinViewModel(),
    socialViewModel: SocialViewModel = koinViewModel(),
    profileViewModel: ProfileViewModel = koinViewModel(),
    onNavigateToSelfProfile: () -> Unit,
    onChatClick: (userId: String) -> Unit = {}
) {

    val targetUser by otherUserProfileViewModel.targetUser.collectAsState()
    val isLoading by otherUserProfileViewModel.isLoading.collectAsState()
    val currentUserId: String = profileViewModel.getProfileData()?.id.toString()

    // Follow counts từ SocialViewModel (optimistic update)
    val followCountsMap by socialViewModel.followCountsMap.collectAsState()
    val targetUserFollowCounts = followCountsMap[userId]
    val followersCountValue = targetUserFollowCounts?.followerCount ?: 0
    val followingCountValue = targetUserFollowCounts?.followingCount ?: 0

    // Trạng thái follow
    val socialUiState by socialViewModel.uiState.collectAsState()
    val socialData = (socialUiState as? SocialUiState.Success)?.data
    val following = socialData?.following ?: emptySet()
    val isFollowing = following.contains(userId)

    if (currentUserId == userId) {
        LaunchedEffect(Unit) {
            onNavigateToSelfProfile()
        }
        return
    }

    LaunchedEffect(userId, currentUserId) {
        if (currentUserId == userId) return@LaunchedEffect
        otherUserProfileViewModel.loadUserProfile(userId)
        if (currentUserId.isNotBlank() && currentUserId != "null") {
            socialViewModel.loadFollowing(currentUserId)
        }
        socialViewModel.getFollowCounts(userId)
    }

    Scaffold(
        topBar = {
            OtherProfileHeader(
                username = targetUser?.displayName ?: targetUser?.email ?: "User",
                onBack = onBack
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar
                Avatar(
                    avatarUrl = if (targetUser?.photoUrl == "null") null else targetUser?.photoUrl,
                    avatarSize = 100,
                )

                // Name & Email
                Text(
                    text = "@${(targetUser?.displayName ?: "user").replace(" ", "_").lowercase()}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 12.dp),
                    color = Color.Black
                )
                Text(
                    text = targetUser?.email ?: "",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                // Stats (số follower được cập nhật ngay khi follow/unfollow)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ProfileStat(formatCount(followingCountValue.toLong()), "Following")
                    ProfileStat(formatCount(followersCountValue.toLong()), "Followers")
                    ProfileStat("0", "Likes")
                }

                // Action Buttons (Follow/Unfollow & Message)
                if (currentUserId != userId) {
                    Row(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Button(
                            onClick = {
                                socialViewModel.onAction(SocialAction.Follow(userId))
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isFollowing) Color(0xFFE9E9E9) else Color(0xFFFE2C55)
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = if (isFollowing) "Unfollow" else "Follow",
                                color = if (isFollowing) Color.Black else Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { onChatClick(userId) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE9E9E9)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Message", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtherProfileHeader(username: String, onBack: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = username,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}