package com.example.tiktok_clone.features.auth.ui

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.features.profile.viewmodel.ProfileViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.brands.Facebook
import compose.icons.fontawesomeicons.brands.Google
import compose.icons.fontawesomeicons.solid.QuestionCircle
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginSelectionScreen(
    onSignUpClick: () -> Unit,
    onBack: () -> Unit,
    onPhoneEmailLoginClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    profileViewModel: ProfileViewModel = koinViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val googleAuthHelper = remember { GoogleAuthUiHelper(context) }
    val facebookAuthHelper = remember { FacebookAuthUiHelper() }

    val loginManager = LoginManager.getInstance()
    val callbackManager = remember { CallbackManager.Factory.create() }

    // Trạng thái chờ xử lý đăng nhập
    var isAuthenticating by remember { mutableStateOf(false) }

    val handleLoginSuccess = {
        profileViewModel.refreshProfile()
        onLoginSuccess()
    }

    val launcher = rememberLauncherForActivityResult(
        loginManager.createLogInActivityResultContract(callbackManager, null)
    ) {
        // CallbackManager xử lý kết quả thông qua đăng ký bên dưới
    }

    DisposableEffect(Unit) {
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                coroutineScope.launch {
                    val firebaseResult =
                        facebookAuthHelper.signInWithFacebookCredential(result.accessToken)
                    firebaseResult.onSuccess {
                        // Thành công: Chuyển thẳng về Home qua AppNavigation
                        handleLoginSuccess()
                    }.onFailure { e ->
                        isAuthenticating = false
                        Toast.makeText(context, "Lỗi Firebase: ${e.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onCancel() {
                isAuthenticating = false
            }

            override fun onError(error: FacebookException) {
                isAuthenticating = false
                Toast.makeText(context, "Lỗi Facebook: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
        onDispose { loginManager.unregisterCallback(callbackManager) }
    }

    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // --- GIAO DIỆN CHÍNH ---
            Column(modifier = Modifier.fillMaxSize()) {
                // 1. Header
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
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { onBack() } // Gán sự kiện quay lại
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
                        .padding(horizontal = 32.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(40.dp))
                    Text(
                        text = "Đăng nhập vào TikTok",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    // Phương thức 1: Phone/Email
                    CommonOptionButton("Số điện thoại/email/tên người dùng", Icons.Default.Person) {
                        if (!isAuthenticating) onPhoneEmailLoginClick()
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Phương thức 2: Facebook
                    CommonOptionButton("Tiếp tục với Facebook", FontAwesomeIcons.Brands.Facebook) {
                        if (!isAuthenticating) {
                            isAuthenticating = true
                            launcher.launch(listOf("email", "public_profile"))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Phương thức 3: Google
                    CommonOptionButton("Tiếp tục với Google", FontAwesomeIcons.Brands.Google) {
                        if (!isAuthenticating) {
                            isAuthenticating = true
                            coroutineScope.launch {
                                val result = googleAuthHelper.signInGoogle()
                                result.onSuccess {
                                    onLoginSuccess() // Chuyển trang khi Google thành công
                                }.onFailure { e ->
                                    isAuthenticating = false
                                    Toast.makeText(
                                        context,
                                        "Lỗi Google: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }

                // 3. Footer
                CommonFooter(
                    text = "Bạn không có tài khoản?",
                    actionText = "Đăng ký",
                    onActionClick = { if (!isAuthenticating) onSignUpClick() }
                )
            }

            // --- LỚP PHỦ LOADING (Hiện lên khi isAuthenticating = true) ---
            if (isAuthenticating) {
                Surface(
                    color = Color.White.copy(alpha = 0.8f), // Làm mờ nhẹ nền
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = Color(0xFFFE2C55)) // Màu hồng TikTok
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Đang xử lý...",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
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
        shape = RoundedCornerShape(2.dp),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color.Black,
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.Unspecified // Giữ màu gốc của Icon (Google/Facebook)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                ),
                color = Color.Black,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.size(20.dp))
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    ),
                    color = Color.Gray
                )
                Text(
                    text = actionText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFFFE2C55),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    ),
                    modifier = Modifier
                        .clickable { onActionClick() }
                        .padding(start = 4.dp)
                )
            }
        }
    }
}