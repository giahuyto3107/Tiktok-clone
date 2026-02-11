package com.example.tiktok_clone.features.auth.ui

import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.brands.Facebook
import compose.icons.fontawesomeicons.brands.Google
import compose.icons.fontawesomeicons.solid.QuestionCircle
import kotlinx.coroutines.launch

@Composable
fun LoginSelectionScreen(
    onSignUpClick: () -> Unit,
    onBack: () -> Unit,
    onPhoneEmailLoginClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val googleAuthHelper = remember { GoogleAuthUiHelper(context) }

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
            // Body
            Column(
                modifier = Modifier.weight(1f).padding(horizontal = 32.dp).verticalScroll(
                    rememberScrollState()
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                Text("Đăng nhập vào TikTok", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.Black, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(32.dp))

                // Nút này gọi onPhoneEmailLoginClick
                CommonOptionButton("Số điện thoại/email/tên người dùng", Icons.Default.Person) {
                    onPhoneEmailLoginClick()
                }
                Spacer(modifier = Modifier.height(16.dp))
                CommonOptionButton("Tiếp tục với Facebook", FontAwesomeIcons.Brands.Facebook) {}
                Spacer(modifier = Modifier.height(16.dp))

//                Spacer(modifier = Modifier.height(32.dp))
                CommonOptionButton(
                    "Tiếp tục với Google",
                    FontAwesomeIcons.Brands.Google,
                    {
                        coroutineScope.launch {
                            val result = googleAuthHelper.signInGoogle()
                            result.onSuccess { user ->
//                                onGoogleSignInClick()
                                Log.d("SignIn", "User signed in: $user")

                            }.onFailure { e ->
                                Toast.makeText(
                                    context,
                                    "Sign in Failed: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(32.dp))
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
        shape = RoundedCornerShape(2.dp),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color.Black,
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.Black
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                ),
                color = Color.Black,
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
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
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    ),
                    color = Color.Black
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