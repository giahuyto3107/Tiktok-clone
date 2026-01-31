package com.example.tiktok_clone.features.social.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.core.utils.AppColors
import com.example.tiktok_clone.features.social.ui.Avatar

@Composable
fun CommentInput(
    avatarUrl: String?,
    modifier: Modifier
)
{
//    val galleryLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContract.GetContent()
//    ) {
//        uri -> Uri? ->
//        uri?.let{onAction(CommentAction.AddComment(it))}
//    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Avatar(
            avatarUrl = avatarUrl,
            modifier = Modifier
                .padding(4.dp)
        )
        Box(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(0.7f)
                .height(1.dp)
        ) {
            Text(
                text = "Nhập bình luận...",
                color = AppColors.TEXT_ON_DARK,
            )

        }
    }
}