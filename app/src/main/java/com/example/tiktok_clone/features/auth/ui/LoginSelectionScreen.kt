package com.example.tiktok.features.auth.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.brands.Facebook
import compose.icons.fontawesomeicons.brands.Google
import compose.icons.fontawesomeicons.solid.QuestionCircle

@Composable
fun LoginSelectionScreen(
    onSignUpClick: () -> Unit,
    onBack: () -> Unit,
    onPhoneEmailLoginClick: () -> Unit
) {
    Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Khi click X thì gọi onBack()
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.Close, null, modifier = Modifier.size(28.dp))
                }
                Icon(FontAwesomeIcons.Solid.QuestionCircle, null, tint = Color.Gray, modifier = Modifier.size(28.dp))
            }

            // Body
            Column(
                modifier = Modifier.weight(1f).padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                Text("Đăng nhập vào TikTok", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(32.dp))

                // Nút này gọi onPhoneEmailLoginClick
                CommonOptionButton("Số điện thoại/email/tên người dùng", Icons.Default.Person) {
                    onPhoneEmailLoginClick()
                }
                Spacer(modifier = Modifier.height(16.dp))
                CommonOptionButton("Tiếp tục với Facebook", FontAwesomeIcons.Brands.Facebook) {}
                Spacer(modifier = Modifier.height(16.dp))

                CommonOptionButton("Tiếp tục với Google", FontAwesomeIcons.Brands.Google) {}
                Spacer(modifier = Modifier.weight(1f))
            }

            // Footer
            CommonFooter(text = "Bạn không có tài khoản?", actionText = "Đăng ký", onActionClick = onSignUpClick)
        }
    }
}

@Composable
fun CommonOptionButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(2.dp), // Góc vuông nhẹ 2dp giống TikTok
        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color.Black,
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(20.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun CommonFooter(text: String, actionText: String, onActionClick: () -> Unit) {
    Column {
        HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFE0E0E0))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            contentAlignment = Alignment.Center
        ) {
            Row {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = actionText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFFFE2C55), // Màu đỏ TikTok
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .clickable { onActionClick() }
                        .padding(start = 4.dp)
                )
            }
        }
    }
}