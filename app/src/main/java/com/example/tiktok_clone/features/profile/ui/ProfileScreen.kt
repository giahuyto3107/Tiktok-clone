package com.example.tiktok_clone.features.profile.ui



import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val TiktokRed = Color(0xFFEA4359)

@Composable

fun ProfileScreen(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit

) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White // Đặt nền trắng giống ảnh
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            ProfileHeader()
            // Divider mờ ngăn cách header và body
            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)

            ProfileBody(onLoginClick = onLoginClick)
        }

    }

}



@Composable

fun ProfileHeader(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp) // Thêm padding cho thoáng
    ) {
        // Tiêu đề Profile căn giữa
        Text(
            text = "Profile",
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            color = Color.Black
        )

        // Icon Menu căn phải
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
            .padding(16.dp), // Padding tổng thể
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon User lớn
        Icon(
            imageVector = Icons.Default.Person, // Hoặc dùng Icons.Rounded.AccountCircle sẽ tròn hơn
            contentDescription = "User",
            modifier = Modifier
                .size(60.dp) // Tăng kích thước icon
                .padding(bottom = 16.dp),
            tint = Color.DarkGray
        )

        Text(
            text = "Log into existing account",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Nút Login màu đỏ
        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = TiktokRed,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(4.dp)
        ) {

            Text(
                text = "Login",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )

        }

    }

}

@Preview(showBackground = true)
@Composable

private fun PreviewProfileScreen() {
    ProfileScreen(onLoginClick = {})
}