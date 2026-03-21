package com.example.tiktok_clone.features.social.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.tiktok_clone.R

// hàm load avatar
@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    avatarSize: Int = 50,
    avatarUrl: String? = "",

    ) {
    Box(
        modifier = Modifier
            .border(0.2.dp, Color.LightGray, CircleShape)
            .size(avatarSize.dp)
            .clip(CircleShape)
    ) {
        if (!avatarUrl.isNullOrEmpty()) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = "avatar",
                modifier = Modifier
                    .matchParentSize()
                    .then(modifier),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.default_avatar),
                contentDescription = "default avatar",
                modifier = Modifier
                    .matchParentSize()
                    .then(modifier),
                contentScale = ContentScale.Crop
            )
        }
    }

}