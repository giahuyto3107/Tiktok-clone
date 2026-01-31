package com.example.tiktok_clone.features.auth.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.ChevronLeft
import compose.icons.fontawesomeicons.solid.QuestionCircle

// --- COLORS ---
val TikTokRed = Color(0xFFFE2C55)
val TikTokGray = Color(0xFF86878B)
val BorderColor = Color(0xFFE0E0E0)

// --- HEADER ---
@Composable
fun AuthHeader(
    onBack: (() -> Unit)? = null, // Nếu null thì hiện dấu X, có hàm thì hiện mũi tên
    onClose: (() -> Unit)? = null,
    showHelp: Boolean = true
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBack != null) {
            Icon(
                imageVector = FontAwesomeIcons.Solid.ChevronLeft,
                contentDescription = "Back",
                modifier = Modifier.size(20.dp).clickable { onBack() }
            )
        } else if (onClose != null) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier.size(26.dp).clickable { onClose() }
            )
        } else {
            Spacer(modifier = Modifier.size(24.dp))
        }

        if (showHelp) {
            Icon(
                imageVector = FontAwesomeIcons.Solid.QuestionCircle,
                contentDescription = "Help",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier.size(26.dp).clickable { onClose?.invoke() }
            )
        }
    }
}

// --- FOOTER ---
@Composable
fun AuthFooter(
    text: String,
    actionText: String,
    onActionClick: () -> Unit
) {
    Column {
        HorizontalDivider(thickness = 0.5.dp, color = BorderColor)
        Box(
            modifier = Modifier.fillMaxWidth().height(70.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Text(
                    text = actionText,
                    color = TikTokRed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(start = 4.dp).clickable { onActionClick() }
                )
            }
        }
    }
}

// --- INPUT FIELDS (Dùng BasicTextField để fix lỗi padding) ---
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .height(44.dp)
            .border(1.dp, BorderColor, RoundedCornerShape(4.dp))
            .background(Color.White)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
            if (value.isEmpty()) {
                Text(placeholder, color = TikTokGray, fontSize = 14.sp)
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
                singleLine = true,
                visualTransformation = visualTransformation,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (trailingIcon != null) {
            trailingIcon()
        }
    }
}