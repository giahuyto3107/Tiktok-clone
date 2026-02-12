package com.example.tiktok_clone.features.admin.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ==========================================
// 1. SHARED RESOURCES (DATA & COLORS)
// ==========================================

// Bảng màu dùng chung
val BgColor = Color(0xFFF0F2F5)
val SidebarColor = Color(0xFFFFFFFF)
val TextBlack = Color(0xFF1F2937)
val TextGray = Color(0xFF6B7280)
val BorderGray = Color(0xFFE5E7EB)
val TikTokBlue = Color(0xFF20D5EC)
val TikTokRed = Color(0xFFFE2C55)

// Data Model dùng chung
data class AdminUser(
    val id: String,
    val stt: String,
    val name: String,
    val handle: String,
    val email: String,
    val date: String,
    val avatarUrl: String = "",
    val bio: String = "No bio provided.",
    val followers: String = "0",
    val following: String = "0",
    val likes: String = "0",
    var isVerified: Boolean = false,
    var isBanned: Boolean = false
)

// ==========================================
// 2. MAIN SCREEN (NAVIGATOR)
// ==========================================

@Composable
fun DashboardScreen() {
    // State quản lý: null = hiện bảng, có user = hiện chi tiết
    var selectedUser by remember { mutableStateOf<AdminUser?>(null) }

    Surface(modifier = Modifier.fillMaxSize(), color = BgColor) {
        Row(modifier = Modifier.fillMaxSize()) {
            // 1. Sidebar (Cố định bên trái)

            // 2. Main Content (Thay đổi nội dung)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .padding(40.dp)
            ) {
                if (selectedUser == null) {
                    // HIỆN BẢNG
                    DashboardContent(onUserClick = { user -> selectedUser = user })
                } else {
                    // HIỆN CHI TIẾT (Gọi từ file UserDetailManagement.kt)
                    UserProfileDetailScreen(
                        user = selectedUser!!,
                        onBack = { selectedUser = null }
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardContent(onUserClick: (AdminUser) -> Unit) {
    Column {
        TopBarSection()
        Spacer(modifier = Modifier.height(40.dp))
        StatsGridSection()
        Spacer(modifier = Modifier.height(40.dp))
        UserTableSection(onUserClick = onUserClick)
    }
}

// ==========================================
// 3. UI COMPONENTS (SIDEBAR, TOPBAR, TABLE)
// ==========================================


@Composable
fun TopBarSection() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Column {
            Text("Dashboard Overview", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextBlack)
            Text("Welcome back, Admin", fontSize = 14.sp, color = TextGray)
        }
        OutlinedTextField(
            value = "", onValueChange = {},
            placeholder = { Text("Search anything...", fontSize = 14.sp, color = TextGray) },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = TextGray) },
            modifier = Modifier.width(350.dp).height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White, focusedContainerColor = Color.White,
                unfocusedBorderColor = BorderGray, focusedBorderColor = TextGray
            ),
            singleLine = true
        )
    }
}

@Composable
fun StatsGridSection() {
    Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
        StatCardItem(Modifier.weight(1f), "10.5M", "Total Users", Color(0xFFE3F2FD), TikTokBlue)
        StatCardItem(Modifier.weight(1f), "2.3K", "New Users", Color(0xFFFFEBEE), TikTokRed)
        StatCardItem(Modifier.weight(1f), "8.2M", "Active Users", Color(0xFFE8F5E9), Color(0xFF4CAF50))
    }
}

@Composable
fun StatCardItem(modifier: Modifier, mainValue: String, title: String, bgColor: Color, iconColor: Color) {
    Card(
        modifier = modifier.height(140.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderGray.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(8.dp).background(iconColor, RoundedCornerShape(50)))
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, fontSize = 14.sp, color = TextGray, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(mainValue, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = TextBlack)
            Text("+12% from yesterday", fontSize = 12.sp, color = Color(0xFF10B981), fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun UserTableSection(onUserClick: (AdminUser) -> Unit) {
    val users = listOf(
        AdminUser("ID001", "01", "Nhật Tiến", "@tien12345", "tien@gmail.com", "27/11/2026", followers = "1.2M", likes = "50M"),
        AdminUser("ID002", "02", "Gia Huy", "@huy123", "ghuy@gmail.com", "27/01/2026", followers = "5K", likes = "100K", isVerified = true),
        AdminUser("ID003", "03", "Minh An", "@anminh99", "anminh@gmail.com", "20/05/2026", followers = "120K", likes = "2M"),
        AdminUser("ID004", "04", "Bảo Ngọc", "@bngoc_cute", "ngocb@gmail.com", "15/08/2026", followers = "900", likes = "5K", isBanned = true)
    )

    Column(
        modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(12.dp)).border(1.dp, BorderGray, RoundedCornerShape(12.dp)).padding(24.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Recent Registered Users", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextBlack)

            // Search + Filter
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = "", onValueChange = {},
                    placeholder = { Text("Search user", fontSize = 13.sp) },
                    modifier = Modifier.width(200.dp).height(40.dp),
                    singleLine = true, // FIX LỖI PADDING
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White, focusedContainerColor = Color.White,
                        unfocusedBorderColor = BorderGray
                    )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderGray),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Icon(Icons.Default.List, null, modifier = Modifier.size(16.dp), tint = TextBlack)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Filter", color = TextBlack)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Table
        Column(modifier = Modifier.fillMaxWidth().border(1.dp, BorderGray, RoundedCornerShape(8.dp)).clip(RoundedCornerShape(8.dp))) {
            Row(modifier = Modifier.fillMaxWidth().background(Color(0xFFF9FAFB)).padding(vertical = 16.dp, horizontal = 24.dp)) {
                TableHeaderCell("STT", 0.5f)
                TableHeaderCell("User Name", 2.0f)
                TableHeaderCell("Handle", 1.5f)
                TableHeaderCell("Email", 2.5f)
                TableHeaderCell("Created Date", 1.5f)
                TableHeaderCell("Status", 1.0f)
            }
            Divider(color = BorderGray, thickness = 1.dp)

            users.forEach { user ->
                TableRowItem(user = user, onClick = { onUserClick(user) })
            }
        }
    }
}

@Composable
fun TableRowItem(user: AdminUser, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color.White).clickable { onClick() }.padding(vertical = 16.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableCell(user.stt, 0.5f)
        Row(modifier = Modifier.weight(2.0f), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(50)).background(Color(0xFFE5E7EB)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(user.name, fontWeight = FontWeight.SemiBold, color = TextBlack, fontSize = 14.sp)
            if (user.isVerified) {
                Spacer(modifier = Modifier.width(6.dp))
                Icon(Icons.Default.Check, null, tint = TikTokBlue, modifier = Modifier.size(16.dp))
            }
        }
        TableCell(user.handle, 1.5f)
        TableCell(user.email, 2.5f)
        TableCell(user.date, 1.5f)
        Box(modifier = Modifier.weight(1.0f)) {
            if (user.isBanned) StatusBadgeSmall("Banned", TikTokRed) else StatusBadgeSmall("Active", Color(0xFF10B981))
        }
    }
    Divider(color = BorderGray, thickness = 1.dp)
}

@Composable
fun RowScope.TableHeaderCell(text: String, weight: Float) {
    Text(text.uppercase(), modifier = Modifier.weight(weight), color = TextGray, fontWeight = FontWeight.Bold, fontSize = 12.sp)
}

@Composable
fun RowScope.TableCell(text: String, weight: Float) {
    Text(text, modifier = Modifier.weight(weight), color = TextBlack, fontSize = 14.sp, fontWeight = FontWeight.Normal)
}

@Composable
fun StatusBadgeSmall(text: String, color: Color) {
    Box(modifier = Modifier.background(color.copy(alpha = 0.1f), RoundedCornerShape(100.dp)).padding(horizontal = 10.dp, vertical = 4.dp)) {
        Text(text, color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(widthDp = 1440, heightDp = 900, showBackground = true)
@Composable
fun PreviewDesktopDashboard() {
    DashboardScreen()
}