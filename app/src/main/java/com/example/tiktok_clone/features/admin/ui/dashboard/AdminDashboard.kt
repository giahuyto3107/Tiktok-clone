package com.example.tiktok_clone.features.admin.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.features.admin.ui.content_management.ContentManagementScreen
import com.example.tiktok_clone.features.admin.ui.dashboard.BgColor
import com.example.tiktok_clone.features.admin.ui.dashboard.BorderGray
import com.example.tiktok_clone.features.admin.ui.dashboard.SidebarColor
import com.example.tiktok_clone.features.admin.ui.dashboard.TextBlack
import com.example.tiktok_clone.features.admin.ui.dashboard.TextGray

// Enum để quản lý màn hình hiện tại
enum class AdminScreenType {
    USER_MANAGEMENT,
    CONTENT_MANAGEMENT
}

@Composable
fun AdminDashboardScreen() {
    // State lưu màn hình đang chọn (Mặc định là User)
    var currentScreen by remember { mutableStateOf(AdminScreenType.USER_MANAGEMENT) }

    Surface(modifier = Modifier.fillMaxSize(), color = BgColor) {
        Row(modifier = Modifier.fillMaxSize()) {
            // 1. SIDEBAR DÙNG CHUNG
            AdminSidebar(
                currentScreen = currentScreen,
                onMenuSelected = { currentScreen = it }
            )

            // 2. NỘI DUNG THAY ĐỔI THEO MENU
            Box(modifier = Modifier.weight(1f)) {
                when (currentScreen) {
                    AdminScreenType.USER_MANAGEMENT -> DashboardScreen()
                    AdminScreenType.CONTENT_MANAGEMENT -> ContentManagementScreen()
                }
            }
        }
    }
}

// Sidebar đã được tách ra để nhận callback chuyển trang
@Composable
fun AdminSidebar(
    currentScreen: AdminScreenType,
    onMenuSelected: (AdminScreenType) -> Unit
) {
    Column(
        modifier = Modifier
            .width(280.dp)
            .fillMaxHeight()
            .background(SidebarColor)
            .border(width = 1.dp, color = BorderGray)
            .padding(vertical = 32.dp, horizontal = 24.dp)
    ) {
        // Logo
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).background(Color.Black, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Home, null, tint = Color.White)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("TikTok Admin", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextBlack)
                Text("Management Panel", fontSize = 12.sp, color = TextGray)
            }
        }

        Spacer(modifier = Modifier.height(60.dp))

        // MENU ITEMS
        Text("MAIN MENU", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextGray, modifier = Modifier.padding(bottom = 16.dp))

        // Item 1: Users
        SidebarMenuItem(
            label = "Users Management",
            icon = Icons.Default.Person,
            isSelected = currentScreen == AdminScreenType.USER_MANAGEMENT,
            onClick = { onMenuSelected(AdminScreenType.USER_MANAGEMENT) }
        )

        // Item 2: Content
        SidebarMenuItem(
            label = "Content Management",
            icon = Icons.Default.Star, // Dùng tạm icon Star
            isSelected = currentScreen == AdminScreenType.CONTENT_MANAGEMENT,
            onClick = { onMenuSelected(AdminScreenType.CONTENT_MANAGEMENT) }
        )

        Spacer(modifier = Modifier.height(40.dp))
        Text("SETTINGS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextGray, modifier = Modifier.padding(bottom = 16.dp))
        SidebarMenuItem("Logout", Icons.Default.ExitToApp, isSelected = false, onClick = {})
    }
}

// Component Item menu có khả năng click
@Composable
fun SidebarMenuItem(label: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .background(
                color = if (isSelected) Color(0xFFF3F4F6) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) TextBlack else TextGray,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            color = if (isSelected) TextBlack else TextGray,
            fontSize = 15.sp
        )
    }
}

@Composable
fun TopBarSection(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(title, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextBlack)
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

@Preview(widthDp = 1440, heightDp = 900, showBackground = true)
@Composable
fun PreviewAdminDashboard() {
    AdminDashboardScreen()
}