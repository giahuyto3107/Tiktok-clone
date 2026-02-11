package com.example.tiktok_clone.features.social.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// hàm icon
@Composable
fun CommentItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    onClick: () -> Unit,
    text: String,
    tint: Color = Color.Black,
    showText: Boolean,

) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier
                .clickable(onClick = onClick)
                .then(modifier),
        )
        if (showText) {
            Text(
                text = text,
                modifier = Modifier
                    .padding(start = 4.dp, end = 8.dp)
                    .width(30.dp),
                fontSize = 13.sp,
                color = tint,
                textAlign = TextAlign.Start,
            )
        }
    }
}