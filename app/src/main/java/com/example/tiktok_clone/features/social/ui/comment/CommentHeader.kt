package com.example.tiktok_clone.features.social.ui.comment

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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.R.dimen
import com.example.tiktok_clone.features.social.ui.components.CommentItem
import com.example.tiktok_clone.features.social.ui.components.formatCount

// header bar comment
@Composable
fun CommentHeader(
    commentCount: Long,
    search: String,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
        ,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(24.dp)) {}

        Text(
            text = if (commentCount == 0L) "Bình luận" else "${formatCount(commentCount)} bình luận",
            modifier = Modifier.padding(4.dp),
            fontWeight = FontWeight.Bold,
            fontSize = dimensionResource(dimen.font_s).value.sp,
            color = Color.Black
        )
        CommentItem(
            icon = Icons.Outlined.Close,
            onClick = onClose,
            text = "Đóng",
            tint = Color.Black,
            showText = false,
            modifier = Modifier
                .size(24.dp)

        )
    }

}
