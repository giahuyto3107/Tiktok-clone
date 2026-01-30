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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.features.auth.ui.components.AuthFooter
import com.example.tiktok_clone.features.auth.ui.components.AuthHeader
import com.example.tiktok_clone.features.auth.ui.components.BorderColor
import com.example.tiktok_clone.features.auth.ui.components.CustomTextField
import com.example.tiktok_clone.features.auth.ui.components.TikTokGray
import com.example.tiktok_clone.features.auth.ui.components.TikTokRed
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.regular.Eye
import compose.icons.fontawesomeicons.regular.EyeSlash

enum class LoginTab { PHONE_OTP, PHONE_PASS, EMAIL }

@Composable
fun LoginFormScreen(
    onBack: () -> Unit,
    onSignUpClick: () -> Unit
) {
    var currentTab by remember { mutableStateOf(LoginTab.PHONE_OTP) }
    var input1 by remember { mutableStateOf("") } // Phone or Email
    var input2 by remember { mutableStateOf("") } // OTP or Pass

    Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            AuthHeader(onBack = onBack, showHelp = true)

            Column(modifier = Modifier.weight(1f).padding(horizontal = 32.dp)) {
                Spacer(modifier = Modifier.height(30.dp))
                Text("Đăng nhập", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(32.dp))

                // --- TAB HEADER ---
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        if (currentTab == LoginTab.EMAIL) "Email / TikTok ID" else "Điện thoại",
                        fontWeight = FontWeight.Bold, fontSize = 14.sp
                    )
                    Text(
                        if (currentTab == LoginTab.EMAIL) "Đăng nhập bằng số điện thoại" else "Đăng nhập bằng email/ID",
                        fontSize = 12.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            currentTab = if (currentTab == LoginTab.EMAIL) LoginTab.PHONE_OTP else LoginTab.EMAIL
                        }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                // --- INPUT FIELDS ---
                if (currentTab != LoginTab.EMAIL) {
                    // Phone Input View
                    Row(
                        modifier = Modifier.fillMaxWidth().height(44.dp)
                            .border(1.dp, BorderColor, RoundedCornerShape(4.dp)).background(Color.White),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 12.dp)) {
                            Text("VN +84", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Icon(Icons.Default.ArrowDropDown, null)
                        }
                        Box(modifier = Modifier.width(1.dp).height(20.dp).background(BorderColor))
                        CustomTextField(
                            value = input1, onValueChange = { input1 = it },
                            placeholder = "Số điện thoại",
                            modifier = Modifier.weight(1f).border(0.dp, Color.Transparent)
                        )
                    }
                } else {
                    // Email Input View
                    CustomTextField(value = input1, onValueChange = { input1 = it }, placeholder = "Email hoặc TikTok ID")
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Pass or OTP View
                if (currentTab == LoginTab.PHONE_OTP) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CustomTextField(
                            value = input2, onValueChange = { input2 = it },
                            placeholder = "Nhập mã 6 số", modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = TikTokGray),
                            contentPadding = PaddingValues(0.dp)
                        ) { Text("Gửi mã") }
                    }
                } else {
                    // Password View
                    var isVisible by remember { mutableStateOf(false) }
                    CustomTextField(
                        value = input2, onValueChange = { input2 = it },
                        placeholder = "Mật khẩu",
                        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            Icon(
                                imageVector = if (isVisible) FontAwesomeIcons.Regular.Eye else FontAwesomeIcons.Regular.EyeSlash,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp).clickable { isVisible = !isVisible }
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- SWITCH OTP/PASS ---
                if (currentTab != LoginTab.EMAIL) {
                    Text(
                        text = if (currentTab == LoginTab.PHONE_OTP) "Đăng nhập bằng mật khẩu" else "Quên mật khẩu? | Đăng nhập bằng mã",
                        fontSize = 12.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            currentTab = if (currentTab == LoginTab.PHONE_OTP) LoginTab.PHONE_PASS else LoginTab.PHONE_OTP
                        }
                    )
                } else {
                    Text("Quên mật khẩu?", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if(input1.isNotEmpty() && input2.isNotEmpty()) TikTokRed else Color(0xFFEBEBEB)),
                    enabled = input1.isNotEmpty() && input2.isNotEmpty()
                ) {
                    Text("Đăng nhập", color = if(input1.isNotEmpty() && input2.isNotEmpty()) Color.White else TikTokGray, fontWeight = FontWeight.Bold)
                }
            }

            AuthFooter(text = "Bạn không có tài khoản?", actionText = "Đăng ký", onActionClick = onSignUpClick)
        }
    }
}