package com.example.tiktok_clone.features.social.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.tiktok_clone.R

// hàm load avatar
@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    avatarUrl: String? = "",

) {
    if (!avatarUrl.isNullOrEmpty()) {
        AsyncImage(
            model = avatarUrl,
            contentDescription = "avatar",
            modifier = Modifier
                .then(modifier),
            contentScale = ContentScale.Crop
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.default_avatar),
            contentDescription = "default avatar",
            modifier = Modifier
                .then(modifier),
            contentScale = ContentScale.Crop
        )
    }

}