package com.example.tiktok_clone.features.post.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.R

@Composable
fun BackButton(
    color: Color,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(
                top = dimensionResource(R.dimen.spacing_xxl),
                start = dimensionResource(R.dimen.spacing_m),
            )
            .clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Black.copy(alpha = 0.3f), // Semi-transparent black
            modifier = Modifier
                .size(dimensionResource(R.dimen.font_title_s))
                .offset(x = 1.dp, y = 1.5.dp) // This creates the "Drop" effect
                .blur(radius = 1.dp) // Softens the shadow
        )

        // 2. The Main Icon Layer (White)
        Icon(
            imageVector = icon,
            contentDescription = "Back",
            tint = color,
            modifier = Modifier
                .size(dimensionResource(R.dimen.font_title_s))
        )
    }
}

@Composable
fun NextButton(
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.red),
        ),
        onClick = onNext,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = "Next")
    }
}

@Composable
fun PostButton(
    onPost: () -> Unit,
    enabled: Boolean = true,
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.red),
        ),
        onClick = { onPost() },
        enabled = enabled,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row {
            Icon(
                imageVector = Icons.Default.ArrowCircleUp,
                contentDescription = "Post",
                tint = Color.White,)
            Spacer(Modifier.width(dimensionResource(R.dimen.spacing_s)))

            Text(text = "Post")
        }
    }
}