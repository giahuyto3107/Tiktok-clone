package com.example.tiktok_clone.features.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.media3.common.util.Log
import coil.compose.AsyncImage // Nhớ thêm thư viện Coil vào build.gradle
import com.example.tiktok_clone.features.profile.viewmodel.ProfileViewModel
import com.example.tiktok_clone.features.social.ui.components.Avatar
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import com.example.tiktok_clone.features.social.ui.SocialUiState
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthenticatedProfile(
    onLogout: () -> Unit,
    profileViewModel: ProfileViewModel = koinViewModel(),
    socialViewModel: SocialViewModel = koinViewModel()
) {
    val currentUser = profileViewModel.getProfileData()
    val displayName = currentUser?.displayName ?: "Người dùng TikTok"
    val email = currentUser?.email ?: "Chưa có email"
    val photoUrl = currentUser?.avtPhotoUrl

    socialViewModel.loadFollowing(currentUser?.id.toString())
    socialViewModel.loadFollowers(currentUser?.id.toString())
    val socialUiState by socialViewModel.uiState.collectAsState()
    val followingCount = (socialUiState as? SocialUiState.Success)?.data?.following?.size ?: 0
    val followersCount = (socialUiState as? SocialUiState.Success)?.data?.followers?.size ?: 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Ảnh đại diện
//        AsyncImage(
//            model = photoUrl,
//            contentDescription = "Avatar",
//            modifier = Modifier
//                .size(100.dp)
//                .clip(CircleShape)
//                .border(0.5.dp, Color.LightGray, CircleShape),
//            contentScale = ContentScale.Crop
//        )
        Avatar(
            avatarUrl = if (photoUrl == "null") null else photoUrl,
            avatarSize = 100,
        )

        // 2. Tên và Email
        Text(
            text = "@${displayName.replace(" ", "_").lowercase()}",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 12.dp),
            color = Color.Black
        )
        Text(text = email, fontSize = 14.sp, color = Color.Gray)

        // 3. Chỉ số (Following, Followers, Likes)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            ProfileStat(followingCount.toString(), "Đã follow")
            ProfileStat(followersCount.toString(), "Follower")
            ProfileStat("10.5M", "Thích")
        }

        // 4. Nút chức năng
        Row(modifier = Modifier.padding(horizontal = 20.dp)) {
            Button(
                onClick = { /* Edit profile */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE9E9E9)),
                modifier = Modifier.weight(1f)
            ) {
                Text("Sửa hồ sơ", color = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    onLogout()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.weight(1f)
            ) {
                Text("Đăng xuất", color = Color.White)
            }
        }
    }
}

@Composable
fun ProfileStat(value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 15.dp)
    ) {
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(text = label, color = Color.Gray, fontSize = 12.sp)
    }
}