package com.example.tiktok_clone.features.inbox.ui.inbox

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
// Header man inbox
fun InboxHeader(
    modifier: Modifier = Modifier,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 14.dp, top = 12.dp)
            .then(modifier),
        horizontalArrangement = Arrangement.Center,
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Hộp thư",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Row(
                modifier = Modifier
                    .width(28.dp)
                    .height(18.dp)
                    .background(
                        color = Color.LightGray.copy(0.2f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .align(Alignment.CenterVertically),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.Circle,
                    contentDescription = "Status",
                    tint = Color.Green,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(10.dp)
                )
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Status",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(20.dp)
                        .offset(x = (-2).dp)
                )
            }
        }
    }
}