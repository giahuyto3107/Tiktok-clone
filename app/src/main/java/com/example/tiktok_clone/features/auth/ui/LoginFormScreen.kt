package com.example.tiktok.features.auth.screens

import android.app.Activity
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.features.auth.ui.components.*
import com.example.tiktok_clone.features.profile.viewmodel.ProfileViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.regular.Eye
import compose.icons.fontawesomeicons.regular.EyeSlash
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.util.concurrent.TimeUnit

enum class LoginTab { PHONE_OTP, PHONE_PASS, EMAIL }

@Composable
fun LoginFormScreen(
    onBack: () -> Unit,
    onSignUpClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    profileViewModel: ProfileViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() }

    // UI States
    var currentTab by remember { mutableStateOf(LoginTab.PHONE_OTP) }
    var input1 by remember { mutableStateOf("") } // Số điện thoại hoặc Email
    var input2 by remember { mutableStateOf("") } // OTP hoặc Mật khẩu

    // Auth States
    var verificationId by remember { mutableStateOf("") }
    var isAuthenticating by remember { mutableStateOf(false) }
    var isPasswordVisible by remember { mutableStateOf(false) }

    // Helper to wrap success
    val handleLoginSuccess = {
        profileViewModel.refreshProfile()
        onLoginSuccess()
    }

    // Firebase Phone Auth Callbacks
    val callbacks = remember {
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // Tự động đăng nhập nếu Firebase khớp mã tự động
                signInWithCredential(credential, auth, handleLoginSuccess) { isAuthenticating = false }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                isAuthenticating = false
                Toast.makeText(context, "Lỗi gửi mã: ${e.localizedMessage}", Toast.LENGTH_LONG)
                    .show()
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                isAuthenticating = false
                verificationId = id
                Toast.makeText(context, "Mã OTP đã được gửi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                AuthHeader(onBack = onBack, showHelp = true)

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 32.dp)
                ) {
                    Spacer(modifier = Modifier.height(30.dp))
                    Text("Đăng nhập", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(32.dp))

                    // --- TAB HEADER ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (currentTab == LoginTab.EMAIL) "Email / TikTok ID" else "Điện thoại",
                            fontWeight = FontWeight.Bold, fontSize = 14.sp
                        )
                        Text(
                            text = if (currentTab == LoginTab.EMAIL) "Đăng nhập bằng số điện thoại" else "Đăng nhập bằng email/ID",
                            fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TikTokRed,
                            modifier = Modifier.clickable {
                                input1 = ""; input2 = "" // Reset input khi đổi tab
                                currentTab =
                                    if (currentTab == LoginTab.EMAIL) LoginTab.PHONE_OTP else LoginTab.EMAIL
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    // --- INPUT 1: SĐT hoặc EMAIL ---
                    if (currentTab != LoginTab.EMAIL) {
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
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(20.dp)
                                    .background(BorderColor)
                            )
                            CustomTextField(
                                value = input1, onValueChange = { input1 = it },
                                placeholder = "Số điện thoại",
                                modifier = Modifier
                                    .weight(1f)
                                    .border(0.dp, Color.Transparent)
                            )
                        }
                    } else {
                        CustomTextField(
                            value = input1,
                            onValueChange = { input1 = it },
                            placeholder = "Email hoặc TikTok ID"
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // --- INPUT 2: OTP hoặc MẬT KHẨU ---
                    if (currentTab == LoginTab.PHONE_OTP) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CustomTextField(
                                value = input2, onValueChange = { if (it.length <= 6) input2 = it },
                                placeholder = "Nhập mã 6 số", modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            TextButton(
                                onClick = {
                                    if (input1.isNotEmpty()) {
                                        isAuthenticating = true
                                        val formattedPhone =
                                            if (input1.startsWith("+")) input1 else "+84${
                                                input1.removePrefix("0")
                                            }"
                                        val options = PhoneAuthOptions.newBuilder(auth)
                                            .setPhoneNumber(formattedPhone)
                                            .setTimeout(60L, TimeUnit.SECONDS)
                                            .setActivity(context as Activity)
                                            .setCallbacks(callbacks)
                                            .build()
                                        PhoneAuthProvider.verifyPhoneNumber(options)
                                    }
                                },
                                enabled = !isAuthenticating
                            ) {
                                Text(
                                    "Gửi mã",
                                    fontWeight = FontWeight.Bold,
                                    color = if (!isAuthenticating) TikTokRed else TikTokGray
                                )
                            }
                        }
                    } else {
                        CustomTextField(
                            value = input2, onValueChange = { input2 = it },
                            placeholder = "Mật khẩu",
                            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                Icon(
                                    imageVector = if (isPasswordVisible) FontAwesomeIcons.Regular.Eye else FontAwesomeIcons.Regular.EyeSlash,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable { isPasswordVisible = !isPasswordVisible }
                                )
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- CHUYỂN ĐỔI CHẾ ĐỘ NHẬP ---
                    if (currentTab != LoginTab.EMAIL) {
                        Text(
                            text = if (currentTab == LoginTab.PHONE_OTP) "Đăng nhập bằng mật khẩu" else "Quên mật khẩu? | Đăng nhập bằng mã",
                            fontSize = 12.sp, fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                input2 = ""
                                currentTab =
                                    if (currentTab == LoginTab.PHONE_OTP) LoginTab.PHONE_PASS else LoginTab.PHONE_OTP
                            }
                        )
                    } else {
                        Text("Quên mật khẩu?", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // --- NÚT ĐĂNG NHẬP CHÍNH ---
                    Button(
                        onClick = {
                            isAuthenticating = true
                            when (currentTab) {
                                LoginTab.PHONE_OTP -> {
                                    val credential =
                                        PhoneAuthProvider.getCredential(verificationId, input2)
                                    signInWithCredential(credential, auth, handleLoginSuccess) {
                                        isAuthenticating = false
                                        Toast.makeText(context, "Sai mã OTP", Toast.LENGTH_LONG)
                                            .show()
                                    }
                                }

                                LoginTab.PHONE_PASS, LoginTab.EMAIL -> {
                                    // Firebase dùng chung hàm cho cả SĐT+Pass (nếu đã link) hoặc Email+Pass
                                    // Tuy nhiên, logic chuẩn nhất cho TikTok là xử lý Email/Pass ở đây
                                    auth.signInWithEmailAndPassword(input1, input2)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) handleLoginSuccess()
                                            else {
                                                isAuthenticating = false
                                                Toast.makeText(
                                                    context,
                                                    "Sai email hoặc mật khẩu",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (input1.isNotEmpty() && input2.isNotEmpty()) TikTokRed else Color(
                                0xFFEBEBEB
                            )
                        ),
                        enabled = input1.isNotEmpty() && input2.isNotEmpty() && !isAuthenticating
                    ) {
                        Text("Đăng nhập", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                AuthFooter(
                    text = "Bạn không có tài khoản?",
                    actionText = "Đăng ký",
                    onActionClick = onSignUpClick
                )
            }
        }

        // --- LỚP PHỦ LOADING ---
        if (isAuthenticating) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = TikTokRed)
            }
        }
    }
}

private fun signInWithCredential(
    credential: PhoneAuthCredential,
    auth: FirebaseAuth,
    onSuccess: () -> Unit,
    onFailure: () -> Unit
) {
    auth.signInWithCredential(credential).addOnCompleteListener { task ->
        if (task.isSuccessful) onSuccess() else onFailure()
    }
}