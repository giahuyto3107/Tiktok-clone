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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- BẢNG MÀU CHUNG ---
val BgColor = Color(0xFFFFFFFF)
val SidebarColor = Color(0xFFF7F8FA)
val TableHeadBlue = Color(0xFF4A89DC)
val TextBlack = Color(0xFF333333)
val TextGray = Color(0xFF888888)
val BorderGray = Color(0xFFE0E0E0)
val TikTokBlue = Color(0xFF20D5EC)
val TikTokRed = Color(0xFFFE2C55)

// --- DATA MODEL (Để ở file này để dùng chung) ---
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

// --- MÀN HÌNH CHÍNH (CONTAINER) ---
@Composable
fun DashboardScreen() {
    var selectedUser by remember { mutableStateOf<AdminUser?>(null) }

    Surface(modifier = Modifier.fillMaxSize(), color = BgColor) {
        Row(modifier = Modifier.fillMaxSize()) {
            // 1. Sidebar
            SidebarSection(modifier = Modifier.width(250.dp).fillMaxHeight())

            // 2. Main Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .padding(32.dp)
            ) {
                if (selectedUser == null) {
                    // Hiện Bảng Danh Sách
                    DashboardContent(onUserClick = { user -> selectedUser = user })
                } else {
                    // Hiện Chi Tiết (Gọi từ file UserDetailManagement.kt)
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
        Spacer(modifier = Modifier.height(32.dp))
        StatsGridSection()
        Spacer(modifier = Modifier.height(40.dp))
        UserTableSection(onUserClick = onUserClick)
    }
}

// ==========================================
// CÁC COMPONENT CỦA DASHBOARD (Sidebar, Stats, Table...)
// ==========================================

@Composable
fun UserTableSection(onUserClick: (AdminUser) -> Unit) {
    val users = listOf(
        AdminUser("ID001", "01", "Nhật Tiến", "@tien12345", "tien@gmail.com", "27/11/2026", followers = "1.2M", likes = "50M"),
        AdminUser("ID002", "02", "Gia Huy", "@huy123", "ghuy@gmail.com", "27/01/2026", followers = "5K", likes = "100K", isVerified = true)
    )

    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("User table", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextBlack)
            // Filter UI giả lập
            Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333333)), shape = RoundedCornerShape(4.dp), modifier = Modifier.height(36.dp)) {
                Text("Filter", fontSize = 12.sp)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        Column(modifier = Modifier.fillMaxWidth().border(1.dp, BorderGray)) {
            // Header
            Row(modifier = Modifier.fillMaxWidth().background(TableHeadBlue).padding(vertical = 14.dp)) {
                TableHeaderCell("STT", 0.5f)
                TableHeaderCell("Name", 1.5f)
                TableHeaderCell("Annotation", 1.5f)
                TableHeaderCell("Email", 2f)
                TableHeaderCell("Created Date", 1.2f)
            }
            // Rows
            users.forEachIndexed { index, user ->
                TableRowItem(user = user, isEven = index % 2 == 0, onClick = { onUserClick(user) })
            }
        }
    }
}

@Composable
fun TableRowItem(user: AdminUser, isEven: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isEven) Color.White else Color(0xFFEEEEEE))
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableCell(user.stt, 0.5f)
        TableCell(user.name, 1.5f, isBold = true)
        TableCell(user.handle, 1.5f)
        TableCell(user.email, 2f)
        TableCell(user.date, 1.2f)
    }
    Divider(color = BorderGray, thickness = 0.5.dp)
}

// --- Sidebar & Helper Components ---
@Composable
fun SidebarSection(modifier: Modifier) {
    Column(modifier = modifier.background(SidebarColor).padding(20.dp)) {
        Text("TikTok Admin", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextBlack)
        Spacer(modifier = Modifier.height(50.dp))
        SidebarMenuItem("Dashboard", Icons.Default.Home, isSelected = true)
        SidebarMenuItem("Users", Icons.Default.Person)
        SidebarMenuItem("Settings", Icons.Default.Settings)
    }
}

@Composable
fun SidebarMenuItem(label: String, icon: ImageVector, isSelected: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).background(if (isSelected) Color.White else Color.Transparent, RoundedCornerShape(8.dp)).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = TextBlack, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = label, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium, color = TextBlack, fontSize = 14.sp)
    }
}

@Composable
fun TopBarSection() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        OutlinedTextField(
            value = "", onValueChange = {}, placeholder = { Text("Search", fontSize = 13.sp) },
            modifier = Modifier.width(260.dp).height(50.dp), shape = RoundedCornerShape(4.dp)
        )
    }
}

@Composable
fun StatsGridSection() {
    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
        StatCardItem(Modifier.weight(1f), "10.5M", "Total Users")
        StatCardItem(Modifier.weight(1f), "2.3K", "New Users")
        StatCardItem(Modifier.weight(1f), "8.2M", "Active Users")
    }
}

@Composable
fun StatCardItem(modifier: Modifier, mainValue: String, title: String) {
    Card(modifier = modifier.height(100.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
            Text(title, fontSize = 14.sp, color = TextGray)
            Text(mainValue, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextBlack)
        }
    }
}

@Composable
fun RowScope.TableHeaderCell(text: String, weight: Float) {
    Text(text, modifier = Modifier.weight(weight), color = Color.White, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, fontSize = 14.sp)
}

@Composable
fun RowScope.TableCell(text: String, weight: Float, isBold: Boolean = false) {
    Text(text, modifier = Modifier.weight(weight), color = TextBlack, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal, textAlign = TextAlign.Center, fontSize = 13.sp)
}