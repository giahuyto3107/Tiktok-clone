package com.example.tiktok_clone.features.social.fakeData

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import com.example.tiktok_clone.features.social.model.App
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Comment

object FakeAppData {
    val apps = listOf(
        App(
            modifier = Modifier.Companion
                .rotate(90f),
            icon = Icons.Filled.Repeat,
            appName = "Đăng lại",
            backgroundColor = Color(0xFFFEC20D),
            onClick = {}
        ),
        App(
            modifier = Modifier.Companion.rotate(-45f),
            icon = Icons.Filled.Link,
            appName = "Sao chép liên kết",
            backgroundColor = Color(0xFF3175FA),
            onClick = {}
        ),
        App(
            fontSize = 60,
            appIconByLetter = "f",
            appName = "Facebook",
            backgroundColor = Color(0xFF3175FA),
            onClick = {}
        ),
        App(
            fontSize = 60,
            appIconByLetter = "f",
            appName = "Lite",
            tint = Color(0xFF3175FA),
            backgroundColor = Color.Companion.White,
            onClick = {}
        ),
        App(
            fontSize = 20,
            appIconByLetter = "Zalo",
            appName = "Zalo",
            backgroundColor = Color(0xFF3175FA),
            onClick = {}
        ),
        App(
            icon = FontAwesomeIcons.Solid.Comment,
            iconSize = 32,
            appName = "SMS",
            backgroundColor = Color.Companion.Green,
            onClick = {}
        ),
    )
}