package com.example.tiktok.features.auth.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.features.auth.ui.components.*


@Composable
fun SignUpFormScreen(
    onBack: () -> Unit,
    onLoginClick: () -> Unit
) {
    var phone by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }

    val isValid = phone.isNotEmpty() && otp.length == 6 && day.isNotEmpty()

    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AuthHeader(onBack = onBack, showHelp = false)

            Column(modifier = Modifier
                .weight(1f)
                .padding(horizontal = 24.dp)) {
                Spacer(modifier = Modifier.height(20.dp))
                Text("Đăng ký", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(32.dp))

                // Date Picker
                Text("Ngày sinh của bạn", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    DropdownFake("Tháng", month, Modifier.weight(1.2f)) { month = "1" }
                    DropdownFake("Ngày", day, Modifier.weight(1f)) { day = "1" }
                    DropdownFake("Năm", year, Modifier.weight(1.1f)) { year = "2000" }
                }
                Text(
                    "Ngày sinh sẽ không hiển thị công khai.",
                    fontSize = 12.sp,
                    color = TikTokGray,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Phone Input
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Điện thoại", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(
                        "Đăng ký bằng email",
                        color = TikTokGray,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .border(1.dp, BorderColor, RoundedCornerShape(4.dp))
                        .background(Color.White),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(modifier = Modifier.padding(horizontal = 12.dp)) {
                        Text("VN +84", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Icon(Icons.Default.ArrowDropDown, null)
                    }
                    Box(modifier = Modifier
                        .width(1.dp)
                        .height(20.dp)
                        .background(BorderColor))
                    CustomTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = "Số điện thoại",
                        modifier = Modifier
                            .weight(1f)
                            .border(0.dp, Color.Transparent),
                        keyboardType = KeyboardType.Phone
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // OTP Input
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CustomTextField(
                        value = otp,
                        onValueChange = { if (it.length <= 6) otp = it },
                        placeholder = "Mã 6 số",
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Number
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Gửi mã", fontWeight = FontWeight.Bold, color = TikTokGray)
                }

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isValid) TikTokRed else Color(
                            0xFFEBEBEB
                        )
                    ),
                    enabled = isValid
                ) {
                    Text(
                        "Tiếp",
                        fontWeight = FontWeight.Bold,
                        color = if (isValid) Color.White else TikTokGray
                    )
                }
            }

            AuthFooter(
                text = "Bạn đã có tài khoản?",
                actionText = "Đăng nhập",
                onActionClick = onLoginClick
            )
        }
    }
}

@Composable
fun DropdownFake(label: String, value: String, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .height(44.dp)
            .border(1.dp, BorderColor, RoundedCornerShape(4.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                if (value.isEmpty()) label else value,
                color = if (value.isEmpty()) TikTokGray else Color.Black,
                fontSize = 14.sp
            )
            Icon(Icons.Default.ArrowDropDown, null, tint = Color.Black)
        }
    }
}