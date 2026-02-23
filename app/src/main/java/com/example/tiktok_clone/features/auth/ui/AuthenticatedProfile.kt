package com.example.tiktok_clone.features.auth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage // Nhớ thêm thư viện Coil vào build.gradle
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AuthenticatedProfile(onLogout: () -> Unit) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val displayName = currentUser?.displayName ?: "Người dùng TikTok"
    val email = currentUser?.email ?: "Chưa có email"
    val photoUrl = currentUser?.photoUrl?.toString() ?: "https://www.gravatar.com/avatar/00000000000000000000000000000000?d=mp&f=y"

    Column(
        modifier = Modifier.fillMaxSize().padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Ảnh đại diện
        AsyncImage(
            model = photoUrl,
            contentDescription = "Avatar",
            modifier = Modifier.size(100.dp).clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        // 2. Tên và Email
        Text(text = "@${displayName.replace(" ", "_").lowercase()}", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(top = 12.dp))
        Text(text = email, fontSize = 14.sp, color = Color.Gray)

        // 3. Chỉ số (Following, Followers, Likes)
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp), horizontalArrangement = Arrangement.Center) {
            ProfileStat("128", "Đang follow")
            ProfileStat("1.2M", "Follower")
            ProfileStat("10.5M", "Thích")
        }

        // 4. Nút chức năng
        Row(modifier = Modifier.padding(horizontal = 20.dp)) {
            Button(onClick = { /* Edit profile */ }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE9E9E9)), modifier = Modifier.weight(1f)) {
                Text("Sửa hồ sơ", color = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                FirebaseAuth.getInstance().signOut()
                onLogout()
            }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red), modifier = Modifier.weight(1f)) {
                Text("Đăng xuất", color = Color.White)
            }
        }
    }
}

@Composable
fun ProfileStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 15.dp)) {
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(text = label, color = Color.Gray, fontSize = 12.sp)
    }
}