package com.example.tiktok_clone.features.social.ui.share

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ShareAnotherApp(
    modifier: Modifier = Modifier,
    icon: ImageVector?,
    iconSize: Int = 32,
    appIconByLetter: String = "",
    fontSize: Int = 16,
    appName: String,
    tint: Color = Color.Black,
    backgroundColor: Color = Color.White,
    onClick: (() -> Unit)? = null,
    typeOfReasonOption: String? = ""
) {
    val reasonType = listOf("report", "not_interested", "cast", "speed")
    var isOpen by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.clickable(onClick = {
            isOpen = !isOpen
            onClick?.invoke()
        }),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .border(width = 0.2.dp, Color.LightGray.copy(alpha = 0.5f), CircleShape)
                .size(50.dp)
                .clip(CircleShape)
                .background(backgroundColor)
                .clickable(
                    onClick = {
                        isOpen = !isOpen
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier
                        .size(iconSize.dp)
                        .align(Alignment.Center)
                        .then(modifier)
                )
            } else {
                Text(
                    text = appIconByLetter,
                    fontSize = fontSize.sp,
                    fontWeight = FontWeight.Medium,
                    color = tint,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                )
            }
        }
        Text(
            text = appName,
            fontSize = 12.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(55.dp),
            maxLines = 2,
            lineHeight = 14.sp,
            overflow = TextOverflow.Ellipsis
        )
    }
    if (typeOfReasonOption in reasonType && isOpen)
        ReasonOption(
            onDismiss = { isOpen = false },
            typeOfReasonOption = typeOfReasonOption,
            modifier = Modifier
        )
}