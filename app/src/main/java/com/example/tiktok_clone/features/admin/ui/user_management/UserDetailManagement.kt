package com.example.tiktok_clone.features.admin.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Lưu ý: AdminUser và các màu (BgColor, TextBlack...) đã được định nghĩa ở file UserManagement.kt
// Nếu bạn thấy báo đỏ, hãy đảm bảo 2 file này CÙNG PACKAGE.

@Composable
fun UserProfileDetailScreen(
    user: AdminUser,
    onBack: () -> Unit
) {
    var currentUser by remember { mutableStateOf(user) }

    Column(modifier = Modifier.fillMaxWidth()) {

        // 1. TOP BAR (BACK BUTTON)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderGray),
                shape = RoundedCornerShape(4.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = TextBlack, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Back to List", color = TextBlack)
            }

            Spacer(modifier = Modifier.weight(1f))

            if (currentUser.isBanned) StatusBadge("BANNED", TikTokRed)
            else StatusBadge("ACTIVE", Color(0xFF4CAF50))
        }

        Spacer(modifier = Modifier.height(30.dp))

        // 2. MAIN CARD
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderGray, RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(32.dp)
        ) {
            // -- LEFT COLUMN --
            Row(modifier = Modifier.weight(1.5f)) {
                Box(
                    modifier = Modifier.size(120.dp).clip(CircleShape).background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, null, modifier = Modifier.size(80.dp), tint = Color.White)
                }

                Spacer(modifier = Modifier.width(24.dp))

                Column(verticalArrangement = Arrangement.Center) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(currentUser.name, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextBlack)
                        if (currentUser.isVerified) {
                            Spacer(modifier = Modifier.width(10.dp))
                            Icon(Icons.Default.Check, null, tint = TikTokBlue, modifier = Modifier.size(24.dp))
                        }
                    }
                    Text(currentUser.handle, fontSize = 16.sp, color = TextGray)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Bio:", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextBlack)
                    Text(currentUser.bio, fontSize = 14.sp, color = TextGray)
                }
            }

            Box(modifier = Modifier.width(1.dp).height(120.dp).background(BorderGray).padding(horizontal = 24.dp))

            // -- RIGHT COLUMN --
            Column(
                modifier = Modifier.weight(1f).padding(start = 32.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    DesktopStatItem("Followers", currentUser.followers)
                    DesktopStatItem("Following", currentUser.following)
                    DesktopStatItem("Likes", currentUser.likes)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(
                        onClick = { currentUser = currentUser.copy(isVerified = !currentUser.isVerified) },
                        colors = ButtonDefaults.buttonColors(containerColor = if (currentUser.isVerified) Color.Gray else TikTokBlue),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.weight(1f).height(44.dp)
                    ) {
                        Icon(Icons.Default.Check, null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (currentUser.isVerified) "Revoke Tick" else "Grant Tick")
                    }

                    Button(
                        onClick = { currentUser = currentUser.copy(isBanned = !currentUser.isBanned) },
                        colors = ButtonDefaults.buttonColors(containerColor = if (currentUser.isBanned) Color.Gray else TikTokRed),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.weight(1f).height(44.dp)
                    ) {
                        Icon(Icons.Default.Lock, null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (currentUser.isBanned) "Unban User" else "Ban User")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 3. SYSTEM INFO
        Text("System Information", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextBlack)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth().border(1.dp, BorderGray, RoundedCornerShape(8.dp)).background(Color.White)
        ) {
            DesktopDetailCell("User ID", currentUser.id, Modifier.weight(1f))
            Box(modifier = Modifier.width(1.dp).height(60.dp).background(BorderGray))
            DesktopDetailCell("Email Address", currentUser.email, Modifier.weight(1.5f))
            Box(modifier = Modifier.width(1.dp).height(60.dp).background(BorderGray))
            DesktopDetailCell("Joined Date", currentUser.date, Modifier.weight(1f))
        }
    }
}

@Composable
fun DesktopStatItem(label: String, value: String) {
    Column {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextBlack)
        Text(label, fontSize = 13.sp, color = TextGray)
    }
}

@Composable
fun DesktopDetailCell(label: String, value: String, modifier: Modifier) {
    Column(modifier = modifier.padding(20.dp), verticalArrangement = Arrangement.Center) {
        Text(label, fontSize = 12.sp, color = TextGray, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, fontSize = 15.sp, color = TextBlack, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun StatusBadge(text: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
            .border(1.dp, color, RoundedCornerShape(4.dp))
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text(text, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}