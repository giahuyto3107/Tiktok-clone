package com.example.tiktok_clone.features.profile.ui

import androidx.compose.foundation.background
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
    onChatClick: (userId: String) -> Unit = {}
) {
    val targetUser by otherUserProfileViewModel.targetUser.collectAsState()
    val followCounts by otherUserProfileViewModel.followCounts.collectAsState()
    val guestFollowers by otherUserProfileViewModel.guestFollowers.collectAsState()
    val guestFollowing by otherUserProfileViewModel.guestFollowing.collectAsState()
    val isLoading by otherUserProfileViewModel.isLoading.collectAsState()
    val currentUserId: String = profileViewModel.getProfileData()?.id.toString()
    val socialUiState by socialViewModel.uiState.collectAsState()
    val socialData = (socialUiState as? SocialUiState.Success)?.data
    val following = socialData?.following ?: emptySet()
    val isFollowing = following.contains(userId)
    val sourceFollowersCount =
        if (guestFollowers.isNotEmpty()) guestFollowers.size
        else followCounts?.followerCount ?: 0
    val sourceFollowingCount =
        if (guestFollowing.isNotEmpty()) guestFollowing.size
        else followCounts?.followingCount ?: 0
    var localIsFollowing by remember(userId) { mutableStateOf(isFollowing) }
    var localFollowersCount by remember(userId) { mutableIntStateOf(sourceFollowersCount) }

    LaunchedEffect(isFollowing, sourceFollowersCount) {
        localIsFollowing = isFollowing
        localFollowersCount = sourceFollowersCount
    }


    LaunchedEffect(userId) {
        otherUserProfileViewModel.loadUserProfile(userId)
        if (currentUserId.isNotBlank() && currentUserId != "null") {
            socialViewModel.loadFollowing(currentUserId)
        }
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
                // 1. Avatar
                Avatar(
                    avatarUrl = if (targetUser?.photoUrl == "null") null else targetUser?.photoUrl,
                    avatarSize = 100,
                )

                // 2. Name & Email
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

                // 3. Stats (Following, Followers, Likes)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    val followersCountStr = formatCount(localFollowersCount.toLong())
                    val followingCountStr = formatCount(sourceFollowingCount.toLong())

                    ProfileStat(followingCountStr, "Following")
                    ProfileStat(followersCountStr, "Followers")
                    ProfileStat("0", "Likes") // API currently doesn't provide total likes
                }

                // 4. Action Buttons (Follow/Unfollow & Message)
                Row(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Button(
                        onClick = {
                            val nextFollowing = !localIsFollowing
                            localIsFollowing = nextFollowing
                            localFollowersCount = if (nextFollowing) {
                                localFollowersCount + 1
                            } else {
                                (localFollowersCount - 1).coerceAtLeast(0)
                            }
                            socialViewModel.onAction(SocialAction.Follow(userId))
                            otherUserProfileViewModel.refreshGuestFollow(userId)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (localIsFollowing) Color(0xFFE9E9E9) else Color(0xFFFE2C55)
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = if (localIsFollowing) "Unfollow" else "Follow",
                            color = if (localIsFollowing) Color.Black else Color.White
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
