package com.example.tiktok_clone.features.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.colorResource
import com.example.tiktok_clone.R

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onNavigateToLogin: () -> Unit = {},
    onNavigationToProfileScreen: () -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 1. Phần Header
            ProfileHeader()

            // Đường kẻ mờ phân cách header
            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)

            // 2. Phần Body
            ProfileBody(
                modifier = Modifier.weight(1f), // Chiếm hết không gian còn lại
                onLoginClick = onNavigateToLogin
            )
        }
    }
}

@Composable
fun ProfileHeader(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Tiêu đề căn giữa
        Text(
            text = "Profile",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            color = Color.Black
        )

        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(28.dp),
            tint = Color.Black
        )
    }
}

@Composable
fun ProfileBody(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon người dùng lớn
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "User Placeholder",
            modifier = Modifier.size(60.dp),
            tint = Color.DarkGray // Màu xám tối
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Text mô tả
        Text(
            text = "Log into existing account",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Nút Login màu đỏ
        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(2.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = TikTokRed,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Log into existing account",
                style = MaterialTheme.typography.bodyLarge,
                color = colorResource(R.color.text_on_dark),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun PreviewProfileScreen() {
    ProfileScreen()
}