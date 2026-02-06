package com.example.tiktok_clone.features.social.ui.share

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Cast
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material3.Icon
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Comment
import com.example.tiktok_clone.features.social.model.ShareItem
import com.example.tiktok_clone.ui.theme.GrayIcon
import com.example.tiktok_clone.ui.theme.TextPrimaryGray
import com.example.tiktok_clone.ui.theme.YellowSave

fun ShareItem.toUi(): ShareActionUi {
    return when (id) {
        "repost" ->
            ShareActionUi(
                id = this.id,
                action = this,
                iconStyle = ShareIconStyle.Vector(Icons.Outlined.Repeat, size = 36.dp),
                tint = Color.White,
                backgroundColor = YellowSave,
                modifier = Modifier.rotate(90f),
            )
        "copy_link" ->
            ShareActionUi(
                id = this.id,
                action = this,
                iconStyle = ShareIconStyle.Vector(Icons.Outlined.Link, 36.dp),
                tint = Color.White,
                backgroundColor = Color(0xFF3175FA),
                modifier = Modifier
                    .rotate(135f)
            )
        "facebook" ->
            ShareActionUi(
                id = this.id,
                action = this,
                iconStyle = ShareIconStyle.Letter("f", 60.sp),
                tint = Color.White,
                backgroundColor = Color(0xFF3175FA),
            )
        "facebook_lite" ->
            ShareActionUi(
                id = this.id,
                action = this,
                iconStyle = ShareIconStyle.Letter("f", 60.sp),
                tint = Color(0xFF3175FA),
                backgroundColor = Color.White,
            )
        "zalo" ->
            ShareActionUi(
                id = this.id,
                action = this,
                iconStyle = ShareIconStyle.Letter("Zalo", 20.sp),
                tint = Color.White,
                backgroundColor = Color(0xFF3175FA),
            )
        "sms"->
            ShareActionUi(
                id = this.id,
                action = this,
                iconStyle = ShareIconStyle.Vector(FontAwesomeIcons.Solid.Comment,36.dp),
                tint = Color.White,
                backgroundColor = Color.Green
            )
        "report" ->
            ShareActionUi(
                id = this.id,
                action = this,
                iconStyle = ShareIconStyle.Vector(Icons.Filled.Flag, 28.dp),
                tint = GrayIcon,
                backgroundColor = TextPrimaryGray.copy(alpha=0.2f)
            )
        "not_interested" ->
            ShareActionUi(
                id = this.id,
                action = this,
                iconStyle = ShareIconStyle.Vector(Icons.Filled.HeartBroken,28.dp),
                tint = GrayIcon,
                backgroundColor = TextPrimaryGray.copy(alpha=0.2f)
            )
        "speed" ->
            ShareActionUi(
                id = this.id,
                action = this,
                iconStyle = ShareIconStyle.Vector(Icons.Filled.Speed,28.dp),
                tint = GrayIcon,
                backgroundColor = TextPrimaryGray.copy(alpha=0.2f)
            )
        "add_to_story" ->
            ShareActionUi(
                id = this.id,
                action = this,
                iconStyle = ShareIconStyle.Vector(Icons.Outlined.AddCircleOutline,28.dp),
                tint = GrayIcon,
                backgroundColor = TextPrimaryGray.copy(alpha=0.2f)
            )
        "cast" ->
            ShareActionUi(
                id = this.id,
                action = this,
                iconStyle = ShareIconStyle.Vector(Icons.Outlined.Cast,28.dp),
                tint = GrayIcon,
                backgroundColor = TextPrimaryGray.copy(alpha=0.2f)
            )
        else ->
            ShareActionUi(
                id = this.id,
                action = this,
                iconStyle = ShareIconStyle.Vector(Icons.Default.Share, 24.dp),
                tint = GrayIcon,
                backgroundColor = TextPrimaryGray.copy(alpha=0.2f)
            )
    }
}
