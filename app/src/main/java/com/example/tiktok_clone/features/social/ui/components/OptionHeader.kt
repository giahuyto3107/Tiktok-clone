package com.example.tiktok_clone.features.social.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReasonOptionHeader(
    typeOfReasonOption: String,
    modifier: Modifier = Modifier,
    onClose: () -> Unit
) {
    Box(
        modifier = modifier
            .padding(start = 12.dp, end = 12.dp, top = 10.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = typeOfReasonOption,
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.Center)
        )

        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "Close",
            tint = Color.Black,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable(onClick = onClose)
        )
    }
}