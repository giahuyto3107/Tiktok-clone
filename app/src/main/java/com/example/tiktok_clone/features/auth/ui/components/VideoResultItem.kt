package com.example.tiktok_clone.features.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.features.auth.model.VideoResult

@Composable
fun VideoResultItem(video: VideoResult) {
    Column {

        // 🎥 Thumbnail
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(9f / 16f)
                .clip(RoundedCornerShape(8.dp))
        ) {

            Image(
                painter = painterResource(id = video.thumbnail),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // 🔊 Icon âm lượng
            Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(6.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // 📝 Tiêu đề
        Text(
            text = video.title,
            fontSize = 13.sp,
            maxLines = 2
        )

        Spacer(modifier = Modifier.height(4.dp))

        // 👤 Author + ❤️ Like
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = video.author,
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(14.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = video.likes.toString(),
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}