package com.example.tiktok_clone.features.auth.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.brands.Apple
import compose.icons.fontawesomeicons.brands.Facebook
import compose.icons.fontawesomeicons.brands.Google
import compose.icons.fontawesomeicons.brands.Line
import compose.icons.fontawesomeicons.solid.Comment
import compose.icons.fontawesomeicons.solid.QuestionCircle

@Composable
fun SelectSignUpScreen(
    onPhoneEmailClick: () -> Unit = {}, // Callback khi chọn dòng đầu tiên
    onGoogleClick: () -> Unit = {},      // Callback khi chọn dòng thứ 2
    onLoginClick: () -> Unit = {}       // Callback chuyển sang màn Login
) {
    Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 1. Header (Giống Login)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    modifier = Modifier.size(28.dp)
                )
                Icon(
                    imageVector = FontAwesomeIcons.Solid.QuestionCircle,
                    contentDescription = "Help",
                    modifier = Modifier.size(28.dp),
                    tint = Color.Gray
                )
            }

            // 2. Body
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 32.dp) // Padding chuẩn 32dp giống Login
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "Đăng ký TikTok",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Danh sách các nút (Style giống hệt LoginOptionButton)

                // Nút 1: Số điện thoại -> Bắt sự kiện click để chuyển Form
                SignUpOptionButton(
                    text = "Số điện thoại/email/tên người dùng",
                    icon = Icons.Default.Person,
                    onClick = onPhoneEmailClick
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Các nút khác
                SignUpOptionButton("Tiếp tục với Facebook", FontAwesomeIcons.Brands.Facebook) {}
                Spacer(modifier = Modifier.height(16.dp))
                SignUpOptionButton(
                    "Tiếp tục với Google",
                    FontAwesomeIcons.Brands.Google,
                    onGoogleClick
                )
                Spacer(modifier = Modifier.height(16.dp))
                SignUpOptionButton("Tiếp tục với LINE", FontAwesomeIcons.Brands.Line) {}
                Spacer(modifier = Modifier.height(16.dp))
                SignUpOptionButton("Tiếp tục với KakaoTalk", FontAwesomeIcons.Solid.Comment) {}

                Spacer(modifier = Modifier.height(40.dp))

                // Điều khoản dịch vụ
                TermsText()

                Spacer(modifier = Modifier.height(20.dp))
            }

            // 3. Footer (Giống LoginFooter)
            Column {
                HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFE0E0E0))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp), // Chiều cao footer chuẩn Login
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Bạn đã có tài khoản? ",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Đăng nhập",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFFFE2C55), // Màu đỏ TikTok
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .clickable { onLoginClick() }
                                .padding(start = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

// Component nút bấm (Style giống hệt LoginOptionButton bên LoginScreen)
@Composable
fun SignUpOptionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(2.dp), // Bo góc nhẹ 2dp giống Login
        border = BorderStroke(1.dp, Color(0xFFE0E0E0)), // Viền xám nhạt
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
fun TermsText() {
    val annotatedString = buildAnnotatedString {
        append("Bằng việc tiếp tục với tài khoản có vị trí tại ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("Việt Nam") }
        append(", bạn đồng ý với ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("Điều khoản dịch vụ") }
        append(", đồng thời xác nhận rằng bạn đã đọc ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("Chính sách quyền riêng tư") }
        append(" của chúng tôi.")
    }

    Text(
        text = annotatedString,
        style = MaterialTheme.typography.bodySmall,
        color = Color.Gray,
        textAlign = TextAlign.Center,
        lineHeight = 18.sp
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSelectSignUp() {
    SelectSignUpScreen(onPhoneEmailClick = {}, onLoginClick = {})
}