package com.example.tiktok_clone.features.social.ui.share

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.features.social.ui.components.CommentItem

@Composable
// Header sheet share
fun ShareHeader(
    modifier: Modifier = Modifier,
    onClose: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        Box(Modifier.size(30.dp))
        Text(
            text = "Gửi đến",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
        )
        CommentItem(
            icon = Icons.Outlined.Close,
            onClick = onClose,
            text = "Đóng",
            showText = false,
            modifier = Modifier
                .padding(4.dp)
                .size(30.dp)
        )

    }
}